package com.bansiwala.bhojnalay.dto;

public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private Long studentId;

    public AuthResponse() {}

    public AuthResponse(String token, String username, String role, Long studentId) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.studentId = studentId;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
}
