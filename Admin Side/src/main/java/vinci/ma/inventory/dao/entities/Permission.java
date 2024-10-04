package vinci.ma.inventory.dao.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vinci.ma.inventory.dao.enums.PermissionType;

import java.util.List;

@Entity
@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    @Column(length = 255)  // Ensures the column is of type VARCHAR(255)
    private PermissionType name;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;

}