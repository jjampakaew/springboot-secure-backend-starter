package com.example.auth.domain.user;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false, unique = true, length = 50)
   private String code;

   @Column(length = 255)
   private String description;

   protected Role() {}

   public Role(String code, String description) {
      this.code = code;
      this.description = description;
   }

   // getters
   public String getCode() {
      return code;
   }
}
