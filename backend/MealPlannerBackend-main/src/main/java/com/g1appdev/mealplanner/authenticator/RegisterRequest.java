package com.g1appdev.mealplanner.authenticator;

public class RegisterRequest {

    private String fname;
    private String lname;
    private String email;
    private String password;

    // No-argument constructor
    public RegisterRequest() {
        super();
    }

    // Parameterized constructor
    public RegisterRequest(String fname, String lname, String email, String password) {
        super();
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
