package com.example.studentsvoice.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
        @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
        @NamedQuery(name = "User.findByEnabled", query = "SELECT u FROM User u WHERE u.enabled = :enabled")
})
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username cannot be blank.")
    @Size(max = 50, message = "Username cannot exceed 50 characters.")
    private String username;

    @Column(nullable = false)
    @Email(message = "Email must be valid.")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Name cannot be blank.")
    @Size(max = 100, message = "Name cannot exceed 100 characters.")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    @Column(nullable = false)
    @NotNull(message = "Enabled cannot be null.")
    private boolean enabled;
}
