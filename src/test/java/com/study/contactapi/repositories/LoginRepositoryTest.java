package com.study.contactapi.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import com.study.contactapi.domain.user.Login;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
public class LoginRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    LoginRepository loginRepository;

    @Test
    @DisplayName("Should activate user login by id successfully")
    void activateUserLoginById() {
        Login login = this.createLogin(new Login("fake@mail.com", "fake-password"));

        this.loginRepository.activateUserLoginById(login.getId());

        Login activatedLogin = this.loginRepository.findById(login.getId()).get();

        assertThat(activatedLogin.getAccount_activated_at()).isToday();
    }


    private Login createLogin(Login login) {
        this.entityManager.persist(login);
        return login;
    }
}
