package com.study.contactapi.controllers;

import com.study.contactapi.dto.CreateUserBodyDTO;
import com.study.contactapi.repositories.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");@LocalServerPort

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should register user successfully")
    void registerUser() {
        CreateUserBodyDTO createUserBodyDTO = new CreateUserBodyDTO("fake@fake.com", "fake", "fake", "fake-password");

        given()
                .contentType(ContentType.JSON)
                .body(createUserBodyDTO)
                .when()
                .post("/user/register")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("first_name", equalTo(createUserBodyDTO.first_name()))
                .body("last_name", equalTo(createUserBodyDTO.last_name()))
                .body("email", equalTo(createUserBodyDTO.email()));
    }
}