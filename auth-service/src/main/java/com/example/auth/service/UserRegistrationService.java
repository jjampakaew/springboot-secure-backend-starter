package com.example.auth.service;

import com.example.auth.domain.tenant.Tenant;
import com.example.auth.domain.tenant.TenantRepository;
import com.example.auth.domain.user.Role;
import com.example.auth.domain.user.RoleRepository;
import com.example.auth.domain.user.UserAccount;
import com.example.auth.domain.user.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationService {

    private final TenantRepository tenantRepository;
    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(
            TenantRepository tenantRepository,
            UserAccountRepository userAccountRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.tenantRepository = tenantRepository;
        this.userAccountRepository = userAccountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserAccount registerUser(
            Long tenantId,
            String email,
            String rawPassword
    ) {
        if (userAccountRepository.existsByTenantIdAndEmail(tenantId, email)) {
            throw new IllegalStateException("Email already registered");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        String hashedPassword = passwordEncoder.encode(rawPassword);

        UserAccount user = new UserAccount(tenant, email, hashedPassword);

        Role userRole = roleRepository.findByCode("USER")
                .orElseThrow(() -> new IllegalStateException("Default role not found"));

        user.addRole(userRole);

        return userAccountRepository.save(user);
    }
}