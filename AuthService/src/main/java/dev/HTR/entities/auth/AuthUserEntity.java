package dev.HTR.entities.auth;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.HTR.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Entity
@Data
@Table(name="users")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AuthUserEntity extends BaseEntity {

    static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthUserEntity(String username, String password, boolean enable, Set<Role> roles, String phoneNumber){
        this.password = passwordEncoder.encode(password);
        this.username = username;
        this.enable = enable;
        this.roles = roles;
        this.phoneNumber = phoneNumber;
    }

    public AuthUserEntity(String username, String password, boolean enable){
        this.password = passwordEncoder.encode(password);
        this.username = username;
        this.enable = enable;
    }

    private String username;

    private String password;

    private String phoneNumber;

    private boolean enable;

    @JsonBackReference
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

}
