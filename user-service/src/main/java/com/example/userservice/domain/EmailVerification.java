package com.example.userservice.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "emailVerification")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_verification_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(columnDefinition = "CHAR(6)", nullable = false)
    private String code;

    private Boolean status;

    public EmailVerification(String email, String code) {
        this.email = email;
        this.code = code;
        this.status = false;
    }
}
