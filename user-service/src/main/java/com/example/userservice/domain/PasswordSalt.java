package com.example.userservice.domain;

import com.example.userservice.config.key.PasswordSaltKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.util.Pair;

import javax.persistence.*;

@Entity
@Table(name = "passwordSalt")
@Getter
@Setter
public class PasswordSalt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "password_salt_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "passwordSalt", fetch = FetchType.LAZY)
    private User user;

    @Column(length = 10, nullable = false)
    private String kid;

    @Column(length = 30, nullable = false)
    private String value;

    public PasswordSalt() {
        Pair<String, String> key = PasswordSaltKey.getRandomKey();
        this.kid = key.getFirst();
        this.value = key.getSecond();
    }
}
