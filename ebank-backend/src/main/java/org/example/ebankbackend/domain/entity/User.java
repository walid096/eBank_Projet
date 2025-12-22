package org.example.ebankbackend.domain.entity;

import jakarta.persistence.*;
import org.example.ebankbackend.domain.enums.Role;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_login", columnNames = "login")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // login used for authentication
    @Column(name = "login", nullable = false)
    private String login;

    // must be encrypted in DB (RG_1)
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    // Only for CLIENT role (AGENT_GUICHET => null)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(name = "fk_users_client"))
    private Client client;

    public User() {}

    public Long getId() { return id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
}
