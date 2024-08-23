package com.study.contactapi.repositories;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.study.contactapi.domain.user.AccountConfirmationToken;


public interface AccountConfirmationTokenRepository extends JpaRepository<AccountConfirmationToken, String> {
    AccountConfirmationToken findByToken(String token);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE account_confirmation_token SET is_active = false WHERE token = ?1 AND is_active = true", nativeQuery = true)
    void disableByToken(String token);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE account_confirmation_token SET is_active = false WHERE login_id = ?1 AND is_active = true", nativeQuery = true)
    void disableByLoginId(String loginId);
}
