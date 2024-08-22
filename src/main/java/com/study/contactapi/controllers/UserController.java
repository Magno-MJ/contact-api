package com.study.contactapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.study.contactapi.dto.CreateUserBodyDTO;
import com.study.contactapi.dto.CreatedUserResponseDTO;
import com.study.contactapi.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping 
  public ResponseEntity<String> getUser(){
    return ResponseEntity.ok("sucesso!");
  }

  @PostMapping("/register")
  public ResponseEntity<CreatedUserResponseDTO> createUser(@Validated @RequestBody CreateUserBodyDTO createUserDto) {
    CreatedUserResponseDTO createdUser =  userService.createUser(createUserDto);
    
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }
}