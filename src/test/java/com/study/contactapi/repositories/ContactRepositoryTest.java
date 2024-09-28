package com.study.contactapi.repositories;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.study.contactapi.domain.user.Contact;
import com.study.contactapi.domain.user.Login;
import com.study.contactapi.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
public class ContactRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactRepository contactRepository;

    @Test
    @DisplayName("Should find by id and user id successfully")
    void findByIdAndUserIdCase1() {
        Login login = new Login("fake@mail.com", "fake");

        User user = new User("fake name", "fake name", login);

        this.userRepository.save(user);

        Contact contact = new Contact("fake name", "fake name", "9999999999", user);

        this.createContact(contact);

        Optional<Contact> contactFound = this.contactRepository.findByIdAndUserId(contact.getId(), user.getId());

        assertThat(contactFound.isPresent()).isTrue();
    }


    @Test
    @DisplayName("Should not find by id and user id")
    void findByIdAndUserIdCase2() {
        Optional<Contact> contactFound = this.contactRepository.findByIdAndUserId("fake-id", "fake-id");

        assertThat(contactFound.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should find all by contact user id")
    void findAllByContactUserIdCase1() {
        Login login = new Login("fake@mail.com", "fake");

        User user = new User("fake name", "fake name", login);

        this.userRepository.save(user);

        Contact contact = new Contact("fake name", "fake name", "9999999999", user);

        this.createContact(contact);

        List<Contact> contactFound = this.contactRepository.findAllByContactUserId(user.getId());

        assertThat(!contactFound.isEmpty()).isTrue();
    }


    @Test
    @DisplayName("Should not find all by contact user id")
    void findAllByContactUserIdCase2() {
        List<Contact> contactFound = this.contactRepository.findAllByContactUserId("fake-id");

        assertThat(!contactFound.isEmpty()).isFalse();
    }


    private Contact createContact(Contact contact) {
        this.entityManager.persist(contact);
        return contact;
    }
}
