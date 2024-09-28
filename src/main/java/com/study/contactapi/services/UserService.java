package com.study.contactapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.study.contactapi.domain.user.AccountConfirmationToken;
import com.study.contactapi.domain.user.Login;
import com.study.contactapi.domain.user.User;
import com.study.contactapi.dto.CreateUserBodyDTO;
import com.study.contactapi.dto.CreatedUserResponseDTO;
import com.study.contactapi.http.exceptions.UserAlreadyExistsException;
import com.study.contactapi.infra.security.TokenService;
import com.study.contactapi.mail.EmailService;
import com.study.contactapi.repositories.AccountConfirmationTokenRepository;
import com.study.contactapi.repositories.LoginRepository;
import com.study.contactapi.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountConfirmationTokenRepository accountConfirmationTokenRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public CreatedUserResponseDTO createUser(CreateUserBodyDTO createUserDto) {
        boolean userAlreadyExists = loginRepository.findByEmail(createUserDto.email()).isPresent();

        if (userAlreadyExists) {
            throw new UserAlreadyExistsException();
        }

        Login createdLogin = new Login(createUserDto.email(), passwordEncoder.encode(createUserDto.password()));

        User createdUser = new User(createUserDto.first_name(), createUserDto.last_name(), createdLogin);

        userRepository.save(createdUser);

        String confirmationToken = this.tokenService.generateConfirmationToken(createdLogin.getId());

        AccountConfirmationToken accountConfirmationToken = new AccountConfirmationToken(confirmationToken, createdLogin, true);

        accountConfirmationTokenRepository.save(accountConfirmationToken);

        this.emailService.sendAccountConfirmationMail(createdLogin.getEmail(), accountConfirmationToken.getToken());

        return new CreatedUserResponseDTO(createdUser);
    }
}
