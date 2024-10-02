package com.study.contactapi.controllers;

import com.study.contactapi.domain.user.AccountConfirmationToken;
import com.study.contactapi.domain.user.Login;
import com.study.contactapi.domain.user.User;
import com.study.contactapi.dto.LoginBodyDTO;
import com.study.contactapi.dto.ResendAccountConfirmationTokenBodyDto;
import com.study.contactapi.infra.security.TokenService;
import com.study.contactapi.repositories.AccountConfirmationTokenRepository;
import com.study.contactapi.repositories.LoginRepository;
import com.study.contactapi.repositories.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
class AuthControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private AccountConfirmationTokenRepository accountConfirmationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        loginRepository.deleteAll();
        userRepository.deleteAll();
        accountConfirmationTokenRepository.deleteAll();
    }

    @Test
    @DisplayName("Should login successfully")
    void login() {
        Login login = new Login("fake@fake.com", passwordEncoder.encode("fake-password"));

        login.setAccount_activated_at(new Date());

        User user = new User("Fake Name", "Fake name", login);

        userRepository.save(user);

        LoginBodyDTO loginBodyDTO = new LoginBodyDTO(login.getEmail(),"fake-password");

        given()
                .contentType(ContentType.JSON)
                .body(loginBodyDTO)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.OK.value()).assertThat().body(containsString("token"));

    }

    @Test
    @DisplayName("Should confirm account successfully")
    void confirmAccount() {
        Login login = new Login("fake@fake.com", passwordEncoder.encode("fake-password"));

        User user = new User("Fake Name", "Fake name", login);

        userRepository.save(user);

        String accountConfirmationTokenToken = tokenService.generateConfirmationToken(login.getId());

        AccountConfirmationToken accountConfirmationToken = new AccountConfirmationToken(accountConfirmationTokenToken, login, true);

        accountConfirmationTokenRepository.save(accountConfirmationToken);

        given()
                .contentType(ContentType.JSON)
                .when()
                .post(String.format("/auth/confirm/%s", accountConfirmationTokenToken))
                .then()
                .statusCode(HttpStatus.OK.value());

        AccountConfirmationToken accountConfirmationTokenFound = accountConfirmationTokenRepository.findByToken(accountConfirmationTokenToken);

        assertThat(accountConfirmationTokenFound.is_active()).isEqualTo(false);
    }


    @Test
    @DisplayName("Should resend account confirmation token successfully")
    void resendAccountConfirmationToken() {
        Login login = new Login("fake@fake.com", passwordEncoder.encode("fake-password"));

        User user = new User("Fake Name", "Fake name", login);

        userRepository.save(user);

        String accountConfirmationTokenToken = tokenService.generateConfirmationToken(login.getId());

        AccountConfirmationToken accountConfirmationToken = new AccountConfirmationToken(accountConfirmationTokenToken, login, true);

        accountConfirmationTokenRepository.save(accountConfirmationToken);

        ResendAccountConfirmationTokenBodyDto resendAccountConfirmationTokenBodyDto = new ResendAccountConfirmationTokenBodyDto(login.getEmail());

        given()
                .contentType(ContentType.JSON)
                .body(resendAccountConfirmationTokenBodyDto)
                .when()
                .post("/auth/resend-confirmation-token")
                .then().log().body()
                .statusCode(HttpStatus.OK.value());

        AccountConfirmationToken accountConfirmationTokenFound = accountConfirmationTokenRepository.findByToken(accountConfirmationTokenToken);

        assertThat(accountConfirmationTokenFound.is_active()).isEqualTo(false);
    }
}