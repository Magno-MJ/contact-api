package com.study.contactapi.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.study.contactapi.domain.user.AccountConfirmationToken;
import com.study.contactapi.domain.user.Login;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
class AccountConfirmationTokenRepositoryTest {

    @Autowired
    AccountConfirmationTokenRepository accountConfirmationTokenRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should disable by token successfully")
    void disableByToken() {
        Login login = new Login("fake@mail.com", "fake");

        this.loginRepository.save(login);

        String accountConfirmToken = "fake-token";

        AccountConfirmationToken accountConfirmationToken = new AccountConfirmationToken(accountConfirmToken, login, true);

        this.createAccountConfirmationToken(accountConfirmationToken);

        this.accountConfirmationTokenRepository.disableByToken(accountConfirmToken);

        AccountConfirmationToken token = this.accountConfirmationTokenRepository.findByToken(accountConfirmToken);

        assertThat(token.is_active()).isFalse();
    }

    @Test
    @DisplayName("Should disable by login_id successfully")
    void disableByLoginId() {
        Login login = new Login("fake@mail.com", "fake");

        this.loginRepository.save(login);

        String accountConfirmToken = "fake-token";

        AccountConfirmationToken accountConfirmationToken = new AccountConfirmationToken(accountConfirmToken, login, true);

        this.createAccountConfirmationToken(accountConfirmationToken);

        this.accountConfirmationTokenRepository.disableByLoginId(login.getId());

        AccountConfirmationToken token = this.accountConfirmationTokenRepository.findByToken(accountConfirmToken);

        assertThat(token.is_active()).isFalse();
        
    }

    private AccountConfirmationToken createAccountConfirmationToken(AccountConfirmationToken accountConfirmationToken) {
        this.entityManager.persist(accountConfirmationToken);
        return accountConfirmationToken;
    }
}

