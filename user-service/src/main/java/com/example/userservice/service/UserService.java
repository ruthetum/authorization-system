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
     * 이메일로 회원 가입 인증 코드 전송
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

    // 이메일 중복 확인
    private void validateEmailDuplicate(String email) {
        userRepository.findByEmail(email)
                .ifPresent((user -> {
                    throw new EmailDuplicateException();
                }));
    }

    // 6자리 난수로 구성된 인증 코드 받기
    private String getAuthCode() {
        return Integer.toString(
                ThreadLocalRandom.current().nextInt(100000, 1000000)
        );
    }

    // Todo 메일 전송 비동기처리
    // 인증번호 전송
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

    // Todo 이메일 발송자 하드코딩 수정
    private MimeMessage createMessageForm(String to, String code, MailType mailType) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            message.addRecipients(Message.RecipientType.TO, to);
            if (mailType.equals(MailType.VERIFY))
                message.setSubject("회원가입 인증 코드");
            else
                message.setSubject("비밀번호 초기화");

            String msg="";
            msg+= "<div style='margin:100px;'>";
            if (mailType.equals(MailType.VERIFY)) {
                msg+= "<h1> 회원가입 인증 코드입니다. </h1>";
                msg+= "<br>";
                msg+= "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
                msg+= "<br>";
                msg+= "<br>";
                msg+= "<h3>인증 코드 : <strong>";
            } else {
                msg+= "<h1> 초기화된 비밀번호입니다. </h1>";
                msg+= "<br>";
                msg+= "<p>아래 코드로 로그인해주세요<p>";
                msg+= "<br>";
                msg+= "<br>";
                msg+= "<h3>비밀번호 : <strong>";
            }
            msg+= code+"</strong></h3><br/> ";
            msg+= "<p>감사합니다.<p>";
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

        // Salt 값 생성
        PasswordSalt salt = new PasswordSalt();

        // 유저 생성
        User newUser = User.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                Role.PERSONAL,
                salt
        );
        userRepository.save(newUser);
    }

    // 회원가입 요청 값 유효성 검사
    private void validateSignupRequest(
            String email,
            String authCode,
            String password,
            String passwordCheck
    ) {
        LocalDateTime now = LocalDateTime.now();
        // 비밀번호 체크 동일 여부 검사
        if (!password.equals(passwordCheck))
            throw new NotCorrespondPasswordCheckException();
        // 이메일 중복 검사
        validateEmailDuplicate(email);
        // 이메일 인증 코드 확인 (가장 최근 + 3분 이내)
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

        // 메일 전송
        sendMessage(request.getEmail(), tempPassword, MailType.PASSWORD_CLEAR);
    }

    public SigninResponse signin(SigninRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EmptyUserException());

        // salt 값 더해서 암호화
        String userSaltKid = user.getPasswordSalt().getKid();
        String userSaltValue = PasswordSaltKey.getValue(userSaltKid);
        String encryptedPassword =
                PasswordEncoder.encrypt(request.getPassword(), userSaltValue);

        // 패스워드 확인
        if (!user.getPassword().equals(encryptedPassword)) {
            throw new NotValidPasswordException();
        }

        // 토큰 생성 및 발급
        Pair<String, String> tokens = getNewToken(user);
        return new SigninResponse(tokens.getFirst(), tokens.getSecond());
    }

    private Pair<String, String> getNewToken(User user) {
        // 토큰 생성 및 발급
        Pair<String, String> tokens = JwtUtils.createToken(user);

        // refresh 토큰 redis 저장
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(user.getEmail(), tokens.getSecond());

        return tokens;
    }


    public SigninResponse tokenRefresh(TokenRefreshRequest request) {
        LocalDateTime now = LocalDateTime.now();

        // Todo filter 처리
        // access token에서 이메일 확인
        String email = getEmailByToken(request.getAccessToken());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmptyUserException());

        // 캐시에 email 조회해서 저장된 refresh 토큰 조회 및 비교
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String savedRefreshToken = valueOperations.get(email);

        if (Objects.isNull(savedRefreshToken) ||
            !savedRefreshToken.equals(request.getRefreshToken())) {
            throw new NonValidRefreshToken();
        }

        // refresh 토큰 만료된 경우 삭제
        LocalDateTime exp = LocalDateTime.now();
        if (exp.isBefore(now)) {
            redisTemplate.delete(email);
            throw new ExpiredRefreshToken();
        }

        // 새로운 토큰 발행
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
