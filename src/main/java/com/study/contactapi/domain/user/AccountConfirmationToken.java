package com.study.contactapi.domain.user;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "account_confirmation_token")
@Getter
@Setter
@NoArgsConstructor
public class AccountConfirmationToken {
    @Id()
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String token;

    @ManyToOne(optional = false)
    @PrimaryKeyJoinColumn(name = "login_id", referencedColumnName = "id")
    private Login login;

    @Column(columnDefinition = "BOOLEAN default true")
    private boolean is_active = true;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE default NOW()")
    private Date created_at;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE default NOW()")
    private Date updated_at;

    public AccountConfirmationToken(String token, Login login, boolean is_active) {
        this.token = token;
        this.login = login;
        this.is_active = is_active;
    }
}
