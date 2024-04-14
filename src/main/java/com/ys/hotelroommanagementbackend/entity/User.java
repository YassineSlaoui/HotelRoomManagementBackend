package com.ys.hotelroommanagementbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    /**
     * A special not null constraint is being set the DataSourceConfig class at application startup
     * <br>
     * It doesn't allow both the username and email to be null at the same time.
     * @see com.ys.hotelroommanagementbackend.config.DataSourceConfig
     *
     * @auther Yassine Slaoui
     */
    @Basic
    @Column(name = "username", unique = true, columnDefinition = "CITEXT")
    private String username;

    @Basic
    @Column(name = "email", unique = true, columnDefinition = "CITEXT")
    private String email;

    @Basic
    @Column(name = "password", nullable = false)
    private String password;

    @Builder.Default
    @Setter(AccessLevel.NONE)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Guest guest;

    public void assignRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void revokeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, email, password);
    }

}
