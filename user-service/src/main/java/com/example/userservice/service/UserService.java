package com.example.userservice.service;

import com.example.userservice.config.jwt.JwtUtils;
import com.example.userservice.config.key.PasswordSaltKey;
import com.example.userservice.domain.EmailVerification;
import com.example.userservice.domain.PasswordSalt;
import com.example.userservice.domain.User;
import com.example.userservice.domain.type.role.Role;
import com.example.userservice.domain.type.userStatus.UserStatus;
import com.example.userservice.dto.request.*;
import com.example.userservice.dto.response.SigninResponse;
import com.example.userservice.dto.response.UserDetailResponse;
import com.example.userservice.dto.response.UserListResponse;
import com.example.userservice.exception.common.InternalServerErrorException;
import com.example.userservice.exception.user.*;
import com.example.userservice.repository.EmailVerificationRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.util.PasswordEncoder;
import com.example.userservice.vo.MailType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.util.Pair;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.userservice.config.jwt.JwtUtils.getEmailByToken;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final RedisTemplate redisTemplate;
    private final JavaMailSender mailSender;

    /**
     * updated 21.12.16.
     * ???????????? ?????? ?????? ?????? ?????? ??????
     */
    @Transactional
    public void sendAuthCodeByEmail(EmailVerifyRequest request) {
        String email = request.getEmail();

        validateEmailDuplicate(email);

        String authCode = getAuthCode();
        EmailVerification newEmailVerification =
                new EmailVerification(email, authCode);
        emailVerificationRepository.save(newEmailVerification);

        sendMessage(email, authCode, MailType.VERIFY);
    }

    // ????????? ?????? ??????
    private void validateEmailDuplicate(String email) {
        userRepository.findByEmail(email)
                .ifPresent((user -> {
                    throw new EmailDuplicateException();
                }));
    }

    // 6?????? ????????? ????????? ?????? ?????? ??????
    private String getAuthCode() {
        return Integer.toString(
                ThreadLocalRandom.current().nextInt(100000, 1000000)
        );
    }

    // Todo ?????? ?????? ???????????????
    // ???????????? ??????
    private void sendMessage(String to, String code, MailType mailType) {
        MimeMessage message = null;
        try {
            if (mailType.equals(MailType.VERIFY))
                message = createMessageForm(to, code, mailType);
            else
                message = createMessageForm(to, code, mailType);
            try {
                mailSender.send(message);
            } catch(MailException e){
                e.printStackTrace();
                throw new SendMailException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }

    // Todo ????????? ????????? ???????????? ??????
    private MimeMessage createMessageForm(String to, String code, MailType mailType) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            message.addRecipients(Message.RecipientType.TO, to);
            if (mailType.equals(MailType.VERIFY))
                message.setSubject("???????????? ?????? ??????");
            else
                message.setSubject("???????????? ?????????");

            String msg="";
            msg+= "<div style='margin:100px;'>";
            if (mailType.equals(MailType.VERIFY)) {
                msg+= "<h1> ???????????? ?????? ???????????????. </h1>";
                msg+= "<br>";
                msg+= "<p>?????? ????????? ???????????? ????????? ????????? ??????????????????<p>";
                msg+= "<br>";
                msg+= "<br>";
                msg+= "<h3>?????? ?????? : <strong>";
            } else {
                msg+= "<h1> ???????????? ?????????????????????. </h1>";
                msg+= "<br>";
                msg+= "<p>?????? ????????? ?????????????????????<p>";
                msg+= "<br>";
                msg+= "<br>";
                msg+= "<h3>???????????? : <strong>";
            }
            msg+= code+"</strong></h3><br/> ";
            msg+= "<p>???????????????.<p>";
            msg+= "</div>";
            message.setText(msg, "utf-8", "html");
            message.setFrom(new InternetAddress("dong011758@gmail.com","Heedong"));

            return message;
        } catch (Exception e) {
            throw new SendMailException();
        }
    }

    @Transactional
    public void signup(SignupRequest request) {
        validateSignupRequest(
                request.getEmail(),
                request.getEmailAuthCode(),
                request.getPassword(),
                request.getPasswordCheck()
        );

        // Salt ??? ??????
        PasswordSalt salt = new PasswordSalt();

        // ?????? ??????
        User newUser = User.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                Role.PERSONAL,
                salt
        );
        userRepository.save(newUser);
    }

    // ???????????? ?????? ??? ????????? ??????
    private void validateSignupRequest(
            String email,
            String authCode,
            String password,
            String passwordCheck
    ) {
        LocalDateTime now = LocalDateTime.now();
        // ???????????? ?????? ?????? ?????? ??????
        if (!password.equals(passwordCheck))
            throw new NotCorrespondPasswordCheckException();
        // ????????? ?????? ??????
        validateEmailDuplicate(email);
        // ????????? ?????? ?????? ?????? (?????? ?????? + 3??? ??????)
        EmailVerification emailVerification =
                emailVerificationRepository.findTop1ByEmailOrderByCreatedAtDesc(email)
                    .orElseThrow(() -> new EmptyEmailVerificationException());
        if (emailVerification.getStatus())
            throw new EmptyEmailVerificationException();
        if (ChronoUnit.MINUTES.between(emailVerification.getCreatedAt(), now) > 3)
            throw new ExpiredAuthCodeException();
        if (!emailVerification.getCode().equals(authCode))
            throw new NotValidEmailAuthCodeException();

        emailVerification.setStatus(true);
    }

    @Transactional
    public void clearPassword(ClearPasswordRequest request) {

        User user = userRepository.findByEmailAndName(request.getEmail(), request.getName())
                .orElseThrow(() -> new EmptyUserException());
        String tempPassword = getAuthCode();
        tempPassword += getAuthCode();

        String tempEncryptedPassword = PasswordEncoder.encrypt(tempPassword, user.getPasswordSalt().getValue());
        user.setPassword(tempEncryptedPassword);

        // ?????? ??????
        sendMessage(request.getEmail(), tempPassword, MailType.PASSWORD_CLEAR);
    }

    public SigninResponse signin(SigninRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EmptyUserException());

        // salt ??? ????????? ?????????
        String userSaltKid = user.getPasswordSalt().getKid();
        String userSaltValue = PasswordSaltKey.getValue(userSaltKid);
        String encryptedPassword =
                PasswordEncoder.encrypt(request.getPassword(), userSaltValue);

        // ???????????? ??????
        if (!user.getPassword().equals(encryptedPassword)) {
            throw new NotValidPasswordException();
        }

        // ?????? ?????? ??? ??????
        Pair<String, String> tokens = getNewToken(user);
        return new SigninResponse(tokens.getFirst(), tokens.getSecond());
    }

    private Pair<String, String> getNewToken(User user) {
        // ?????? ?????? ??? ??????
        Pair<String, String> tokens = JwtUtils.createToken(user);

        // refresh ?????? redis ??????
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(user.getEmail(), tokens.getSecond());

        return tokens;
    }


    public SigninResponse tokenRefresh(TokenRefreshRequest request) {
        LocalDateTime now = LocalDateTime.now();

        // Todo filter ??????
        // access token?????? ????????? ??????
        String email = getEmailByToken(request.getAccessToken());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmptyUserException());

        // ????????? email ???????????? ????????? refresh ?????? ?????? ??? ??????
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String savedRefreshToken = valueOperations.get(email);

        if (Objects.isNull(savedRefreshToken) ||
            !savedRefreshToken.equals(request.getRefreshToken())) {
            throw new NonValidRefreshToken();
        }

        // refresh ?????? ????????? ?????? ??????
        LocalDateTime exp = LocalDateTime.now();
        if (exp.isBefore(now)) {
            redisTemplate.delete(email);
            throw new ExpiredRefreshToken();
        }

        // ????????? ?????? ??????
        Pair<String, String> tokens = getNewToken(user);
        return new SigninResponse(tokens.getFirst(), request.getRefreshToken());
    }

    public UserDetailResponse getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EmptyUserException());

        /**
        if (user.getRole().equals(Role.PERSONAL) &&
            !user.getId().equals(userId)) {
            throw new NotAuthorizationException();
        }
        */

        return new UserDetailResponse(user);
    }

    public UserDetailResponse getUserDetailByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmptyUserException());
        return new UserDetailResponse(user);
    }

    public long countNormalUser() {
        return userRepository.countByStatus(
                UserStatus.NORMAL.getDescription()
        );
    }

    public List<UserListResponse.UserInfo> getUsersByPage(int page) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        return userRepository.findAll(pageable)
                .getContent().stream()
                .map(UserListResponse.UserInfo::new)
                .collect(Collectors.toList());

    }
}
