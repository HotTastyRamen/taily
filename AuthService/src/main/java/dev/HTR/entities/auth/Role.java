package dev.HTR.entities.auth;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.HTR.entities.BaseEntity;
import dev.HTR.entities.auth.enumes.Roles;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {

    @JsonBackReference
    @ToString.Exclude
    @ManyToMany(mappedBy = "roles")
    private Collection<AuthUserEntity> users;

    @JsonBackReference
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    private Collection<Privilege> privileges;

    private Roles name;

    public Role() {
        super();
    }

    public Role(Roles name) {
        super();
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name.name();
    }
}
