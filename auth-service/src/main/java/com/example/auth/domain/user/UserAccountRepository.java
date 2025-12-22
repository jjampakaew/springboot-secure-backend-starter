package com.example.auth.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

   Optional<UserAccount> findByTenantIdAndEmail(Long tenantId, String email);

   Optional<UserAccount> findByEmail(String email);

   boolean existsByTenantIdAndEmail(Long tenantId, String email);

   @Query("""
      select u from UserAccount u
      left join fetch u.roles
      where u.email = :email
   """)
   Optional<UserAccount> findWithRolesByEmail(@Param("email") String email);
}
