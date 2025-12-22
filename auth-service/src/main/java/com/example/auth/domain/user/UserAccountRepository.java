package com.example.auth.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByTenantIdAndEmail(Long tenantId, String email);

    boolean existsByTenantIdAndEmail(Long tenantId, String email);
}
