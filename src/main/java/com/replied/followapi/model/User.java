package com.replied.followapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

        @Id
        @GeneratedValue
        private UUID id;

        private String username;
        private String birthdate;

        public User() {}

        public User(String username, String birthdate) {
                this.username = username;
                this.birthdate = birthdate;
        }

        public UUID getId() {
                return id;
        }

        public void setId(UUID id) {
                this.id = id;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getBirthdate() {
                return birthdate;
        }

        public void setBirthdate(String birthdate) {
                this.birthdate = birthdate;
        }
}
