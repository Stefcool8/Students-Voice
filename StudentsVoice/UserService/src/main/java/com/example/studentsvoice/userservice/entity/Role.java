package com.example.studentsvoice.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@NamedQueries({
        @NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r"),
        @NamedQuery(name = "Role.findByCode", query = "SELECT r FROM Role r WHERE r.code = :code"),
        @NamedQuery(name = "Role.findByName", query = "SELECT r FROM Role r WHERE r.name = :name")
})
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Code cannot be blank.")
    @Size(max = 50, message = "Code cannot exceed 50 characters.")
    private String code;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Name cannot be blank.")
    @Size(max = 100, message = "Name cannot exceed 100 characters.")
    private String name;
}

