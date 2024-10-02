package com.study.contactapi.controllers;

import com.study.contactapi.domain.user.Contact;
import com.study.contactapi.domain.user.Login;
import com.study.contactapi.domain.user.User;
import com.study.contactapi.dto.CreateContactBodyDTO;
import com.study.contactapi.dto.UpdateContactBodyDTO;
import com.study.contactapi.infra.security.TokenService;
import com.study.contactapi.repositories.AccountConfirmationTokenRepository;
import com.study.contactapi.repositories.ContactRepository;
import com.study.contactapi.repositories.LoginRepository;
import com.study.contactapi.repositories.UserRepository;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
class ContactControllerTest {

    @LocalServerPort
    private Integer port;

    @AfterEach
    void afterEach() {
        loginRepository.deleteAll();
        userRepository.deleteAll();;
        contactRepository.deleteAll();
    }

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AccountConfirmationTokenRepository accountConfirmationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;


    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        userRepository.deleteAll();
        contactRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a contact successfully")
    void createContact() {
        Login login = new Login("fake@fake.com", passwordEncoder.encode("fake-password"));

        login.setAccount_activated_at(new Date());

        User user = new User("Fake Name", "Fake name", login);

        userRepository.save(user);

        CreateContactBodyDTO createContactBodyDTO = new CreateContactBodyDTO("Fake Name", "Fake Name", "99999999999");

        String token = tokenService.generateToken(login);

        given()
                .contentType(ContentType.JSON)
                .body(createContactBodyDTO)
                .header("Authorization", String.format("Bearer %s", token))
                .when()
                .post("/contact")
                .then().log().body()
                .statusCode(HttpStatus.CREATED.value())
                .body("first_name", equalTo(createContactBodyDTO.first_name()))
                .body("last_name", equalTo(createContactBodyDTO.last_name()))
                .body("phone_number", equalTo(createContactBodyDTO.phone_number()));

    }


    @Test
    @DisplayName("Should find all contacts successfully")
    void findAllContacts() {
        Login login = new Login("fake@fake.com", passwordEncoder.encode("fake-password"));

        login.setAccount_activated_at(new Date());

        User user = new User("Fake Name", "Fake name", login);

        userRepository.save(user);

        Contact contact = new Contact("Fake Name", "Fake Name", "99999999999", user);

        contactRepository.save(contact);

        String token = tokenService.generateToken(login);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", String.format("Bearer %s", token))
                .when()
                .get("/contact");

        response.then().statusCode(HttpStatus.OK.value());

        List<Map<String, Object>> contacts = response.as(new TypeRef<>() {});

        assertThat(contacts.get(0).get("first_name")).isEqualTo(contact.getFirst_name());
        assertThat(contacts.get(0).get("last_name")).isEqualTo(contact.getLast_name());
        assertThat(contacts.get(0).get("phone_number")).isEqualTo(contact.getPhone_number());
    }


    @Test
    @DisplayName("Should find a contact by id successfully")
    void findContactById() {
        Login login = new Login("fake@fake.com", passwordEncoder.encode("fake-password"));

        login.setAccount_activated_at(new Date());

        User user = new User("Fake Name", "Fake name", login);

        userRepository.save(user);

        Contact contact = new Contact("Fake Name", "Fake Name", "99999999999", user);

        contactRepository.save(contact);

        String token = tokenService.generateToken(login);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", String.format("Bearer %s", token))
                .when()
                .get(String.format("/contact/%s", contact.getId()))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("first_name", equalTo(contact.getFirst_name()))
                .body("last_name", equalTo(contact.getLast_name()))
                .body("phone_number", equalTo(contact.getPhone_number()));

    }


    @Test
    @DisplayName("Should update a contact by id successfully")
    void updateContactById() {
        Login login = new Login("fake@fake.com", passwordEncoder.encode("fake-password"));

        login.setAccount_activated_at(new Date());

        User user = new User("Fake Name", "Fake name", login);

        userRepository.save(user);

        Contact contact = new Contact("Fake Name", "Fake Name", "99999999999", user);

        contactRepository.save(contact);

        String token = tokenService.generateToken(login);

        UpdateContactBodyDTO updateContactBodyDTO = new UpdateContactBodyDTO();

        updateContactBodyDTO.setFirst_name("Updated Name");

        System.out.println(updateContactBodyDTO.getFirst_name());
        System.out.println(contact.getId());

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", String.format("Bearer %s", token))
                .body(updateContactBodyDTO)
                .then().log().body()
                .when()
                .patch(String.format("/contact/%s", contact.getId()))
                .then()
                .statusCode(HttpStatus.OK.value());

        Contact updatedContact = contactRepository.findById(contact.getId()).get();

        System.out.println(updatedContact);
        assertThat(updatedContact.getFirst_name()).isEqualTo("Updated Name");
    }



    @Test
    @DisplayName("Should delete a contact by id successfully")
    void deleteContactById() {
        Login login = new Login("fake@fake.com", passwordEncoder.encode("fake-password"));

        login.setAccount_activated_at(new Date());

        User user = new User("Fake Name", "Fake name", login);

        userRepository.save(user);

        Contact contact = new Contact("Fake Name", "Fake Name", "99999999999", user);

        contactRepository.save(contact);

        String token = tokenService.generateToken(login);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", String.format("Bearer %s", token))
                .then().log().body()
                .when()
                .delete(String.format("/contact/%s", contact.getId()))
                .then()
                .statusCode(HttpStatus.OK.value());

        Optional<Contact> updatedContact = contactRepository.findById(contact.getId());

        assertThat(updatedContact.isPresent()).isEqualTo(false);
    }
}