package dev.HTR.entities.auth;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.HTR.entities.BaseEntity;
import dev.HTR.entities.auth.enumes.Privileges;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "privileges")
public class Privilege extends BaseEntity {

    private Privileges name;

    @JsonBackReference
    @ToString.Exclude
    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;


}
