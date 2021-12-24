package com.example.userservice.domain;

import com.example.userservice.domain.type.role.Role;
import com.example.userservice.domain.type.role.RoleAttributeConverter;
import com.example.userservice.domain.type.userStatus.UserStatus;
import com.example.userservice.domain.type.userStatus.UserStatusAttributeConverter;
import com.example.userservice.util.PasswordEncoder;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(columnDefinition = "CHAR(64)", nullable = false)
    private String password;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "password_salt_id")
    private PasswordSalt passwordSalt;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(columnDefinition = "TINYINT", nullable = false)
    @Convert(converter = RoleAttributeConverter.class)
    private String role;

    @Column(name = "status", columnDefinition = "TINYINT", nullable = false)
    @Convert(converter = UserStatusAttributeConverter.class)
    private String status;

    public static User createUser(
            String email,
            String password,
            String name,
            Role role,
            PasswordSalt passwordSalt
    ) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setRole(role.getDescription());
        user.setStatus(UserStatus.NORMAL.getDescription());
        user.setPasswordSalt(passwordSalt);
        user.setPassword(PasswordEncoder.encrypt(password, passwordSalt.getValue()));
        return user;
    }
}
