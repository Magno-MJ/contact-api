package com.study.contactapi.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.study.contactapi.domain.user.User;

public interface UserRepository extends JpaRepository<User, String> {
}
