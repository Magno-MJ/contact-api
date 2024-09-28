package com.study.contactapi.infra.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.inOrder;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.auth0.jwt.JWT;
import com.study.contactapi.domain.user.Login;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;

public class TokenServiceTest {
    @InjectMocks
    @Autowired
    TokenService tokenService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(tokenService, "secret", "contact-api-secret");
    }

    @Test
    @DisplayName("Should generate token successfully")
    void generateTokenCase1() {
        String generatedToken = "fake-token";

        try (MockedStatic<JWT> mockStaticJWT = mockStatic(JWT.class)) {
            JWTCreator.Builder builderMock = mock(JWTCreator.Builder.class);

            Login login = new Login("fake@mail.com", "fake-password");

            when(builderMock.withIssuer("contact-api")).thenReturn(builderMock);
            when(builderMock.withSubject(login.getEmail())).thenReturn(builderMock);
            when(builderMock.withExpiresAt(any(Instant.class))).thenReturn(builderMock);
            when(builderMock.sign(any(Algorithm.HMAC256("contact-api-secret").getClass()))).thenReturn(generatedToken);

            mockStaticJWT.when(JWT::create).thenReturn(builderMock);

            String result = this.tokenService.generateToken(login);

            assertThat(result).isEqualTo(generatedToken);

            mockStaticJWT.verify(JWT::create, times(1));

            verify(builderMock, times(1)).withIssuer("contact-api");
            verify(builderMock, times(1)).withSubject(login.getEmail());
            verify(builderMock, times(1)).withExpiresAt(any(Instant.class));
            verify(builderMock, times(1)).sign(any(Algorithm.HMAC256("contact-api-secret").getClass()));
        }
    }

    @Test
    @DisplayName("Should throw if generate token fails")
    void generateTokenCase2() {
        try (MockedStatic<JWT> mockStaticJWT = mockStatic(JWT.class)) {
            JWTCreator.Builder builderMock = mock(JWTCreator.Builder.class, RETURNS_DEEP_STUBS);
            when(builderMock.withIssuer("contact-api")
                    .withSubject(any(String.class))
                    .withExpiresAt(any(Instant.class))
                    .sign(any(Algorithm.class)))
                    .thenThrow(JWTCreationException.class);

            mockStaticJWT.when(JWT::create).thenReturn(builderMock);

            Login login = new Login("fake@mail.com", "fake-password");

            Exception exception = Assertions.assertThrows(RuntimeException.class, () -> this.tokenService.generateToken(login));

            Assertions.assertEquals("Error while authenticating", exception.getMessage());
        }
    }


    @Test
    @DisplayName("Should generate confirmation token successfully")
    void generateConfirmationTokenCase1() {
        String generatedToken = "fake-token";

        try (MockedStatic<JWT> mockStaticJWT = mockStatic(JWT.class)) {
            JWTCreator.Builder builderMock = mock(JWTCreator.Builder.class);

            String userId = "fake-id";

            when(builderMock.withIssuer("contact-api")).thenReturn(builderMock);
            when(builderMock.withSubject(userId)).thenReturn(builderMock);
            when(builderMock.withExpiresAt(any(Instant.class))).thenReturn(builderMock);
            when(builderMock.sign(any(Algorithm.HMAC256("contact-api-secret").getClass()))).thenReturn(generatedToken);

            mockStaticJWT.when(JWT::create).thenReturn(builderMock);

            String result = this.tokenService.generateConfirmationToken(userId);

            mockStaticJWT.verify(JWT::create, times(1));

            verify(builderMock, times(1)).withIssuer("contact-api");
            verify(builderMock, times(1)).withSubject(userId);
            verify(builderMock, times(1)).withExpiresAt(any(Instant.class));
            verify(builderMock, times(1)).sign(any(Algorithm.HMAC256("contact-api-secret").getClass()));

            assertThat(result).isEqualTo(generatedToken);
        }
    }


    @Test
    @DisplayName("Should throw if generate confirmation token fails")
    void generateConfirmationTokenCase2() {
        try (MockedStatic<JWT> mockStaticJWT = mockStatic(JWT.class)) {
            JWTCreator.Builder builderMock = mock(JWTCreator.Builder.class, RETURNS_DEEP_STUBS);
            when(builderMock.withIssuer("contact-api")
                    .withSubject(any(String.class))
                    .withExpiresAt(any(Instant.class))
                    .sign(any(Algorithm.class)))
                    .thenThrow(JWTCreationException.class);

            mockStaticJWT.when(JWT::create).thenReturn(builderMock);

            String userId = "fake-id";

            Exception exception = Assertions.assertThrows(RuntimeException.class, () -> this.tokenService.generateConfirmationToken(userId));

            Assertions.assertEquals("Error generating confirmation token", exception.getMessage());
        }
    }


    @Test
    @DisplayName("Should validate token successfully")
    void validateTokenCase1() {
        String generatedToken = "fake-token";
        String fakeDecodedToken = "fake-decoded-token";

        try (MockedStatic<JWT> mockStaticJWT = mockStatic(JWT.class)) {
            JWTVerifier.BaseVerification baseVerificationMock = mock(JWTVerifier.BaseVerification.class, RETURNS_DEEP_STUBS);
            when(baseVerificationMock.withIssuer("contact-api").build().verify(generatedToken).getSubject()).thenReturn(fakeDecodedToken);

            mockStaticJWT.when(() -> JWT.require(any())).thenReturn(baseVerificationMock);

            String result = this.tokenService.validateToken(generatedToken);

            assertThat(result).isEqualTo(fakeDecodedToken);

            mockStaticJWT.verify(() -> JWT.require(any(Algorithm.HMAC256("contact-api-secret").getClass())), times(1));

            InOrder inOrder = inOrder(
                    baseVerificationMock.withIssuer("contact-api").build().verify(generatedToken)
            );

            inOrder.verify(baseVerificationMock.withIssuer("contact-api").build().verify(generatedToken), times(1)).getSubject();
        }
    }

    @Test
    @DisplayName("Should return null if validate token fails")
    void validateTokenCase2() {
        String generatedToken = "fake-token";

        try (MockedStatic<JWT> mockStaticJWT = mockStatic(JWT.class)) {
            JWTVerifier.BaseVerification baseVerificationMock = mock(JWTVerifier.BaseVerification.class, RETURNS_DEEP_STUBS);
            when(baseVerificationMock.withIssuer("contact-api").build().verify(generatedToken).getSubject()).thenThrow(JWTVerificationException.class);

            mockStaticJWT.when(() -> JWT.require(any())).thenReturn(baseVerificationMock);

            String result = this.tokenService.validateToken(generatedToken);

            assertThat(result).isEqualTo(null);
        }
    }
}
