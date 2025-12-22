package com.example.auth.bootstrap;

import com.example.auth.domain.user.Role;
import com.example.auth.domain.user.RoleRepository;
import com.example.auth.domain.user.UserAccount;
import com.example.auth.domain.user.UserAccountRepository;
import com.example.auth.domain.tenant.Tenant;
import com.example.auth.domain.tenant.TenantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevDataInitializer implements CommandLineRunner {

   private final UserAccountRepository userRepo;
   private final RoleRepository roleRepo;
   private final TenantRepository tenantRepo;
   private final PasswordEncoder encoder;

   public DevDataInitializer(
         UserAccountRepository userRepo,
         RoleRepository roleRepo,
         TenantRepository tenantRepo,
         PasswordEncoder encoder
   ) {
      this.userRepo = userRepo;
      this.roleRepo = roleRepo;
      this.tenantRepo = tenantRepo;
      this.encoder = encoder;
   }

   @Override
   public void run(String... args) {

      // 1) ensure tenant
      Tenant tenant = tenantRepo.findById(1L)
               .orElseGet(() -> tenantRepo.save(new Tenant("DEF", "Default Tenant"))
        );

      // 2) ensure role
      Role adminRole = roleRepo.findByCode("ADMIN")
               .orElseGet(() ->
                     roleRepo.save(new Role("ADMIN", "Administrator"))
               );

      // 3) ensure admin user
      if (userRepo.findByEmail("admin@example.com").isEmpty()) {

         UserAccount admin = new UserAccount(
                  tenant,
                  "admin@example.com",
                  encoder.encode("admin123")
         );

         admin.addRole(adminRole);

         userRepo.save(admin);
      }
   }
}
