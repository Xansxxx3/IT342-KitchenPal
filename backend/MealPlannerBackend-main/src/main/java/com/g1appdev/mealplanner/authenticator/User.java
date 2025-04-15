package com.g1appdev.mealplanner.authenticator;

import com.g1appdev.mealplanner.entity.Role;

public class User {
    private String fname;
    private String lname;
    private String email;
    private String password;
    private Role role;

    // Private constructor to enforce the use of the builder
    private User(Builder builder) {
        this.fname = builder.fname;
        this.lname = builder.lname;
        this.email = builder.email;
        this.password = builder.password;
        this.role = builder.role;
    }

    // Getters
    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    // Nested static Builder class
    public static class Builder {
        private String fname;
        private String lname;
        private String email;
        private String password;
        private Role role;

        public Builder fname(String fname) {
            this.fname = fname;
            return this;
        }

        public Builder lname(String lname) {
            this.lname = lname;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
