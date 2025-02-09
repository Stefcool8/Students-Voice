package org.example.studentsvoice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_roles")
@NamedQueries({
        @NamedQuery(name = "UserRole.findAll", query = "SELECT e FROM UserRole e"),
        @NamedQuery(name = "UserRole.findByUserId", query = "SELECT e FROM UserRole e WHERE e.user.id = :userId"),
        @NamedQuery(name = "UserRole.findByRoleCode", query = "SELECT e FROM UserRole e WHERE e.id.role_code = :roleCode"),
})
@ToString
public class UserRole {
    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_code", referencedColumnName = "code", insertable = false, updatable = false)
    private Role role;
}
