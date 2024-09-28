package com.study.contactapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.study.contactapi.dto.CreateUserBodyDTO;
import com.study.contactapi.dto.CreatedUserResponseDTO;
import com.study.contactapi.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController()
@RequestMapping("/user")
@Tag(name = "User Controller", description = "Controller that manages the user information")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(description = "Create an user", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreatedUserResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "User already exists", content = @Content(mediaType = "application/json")),
    })
    @PostMapping("/register")
    public ResponseEntity<CreatedUserResponseDTO> createUser(@Validated @RequestBody CreateUserBodyDTO createUserDto) {
        CreatedUserResponseDTO createdUser = userService.createUser(createUserDto);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}