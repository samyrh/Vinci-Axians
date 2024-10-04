package vinci.ma.inventory.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntered;
    private String ref;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image; // Binary data for image of the material
    @Transient
    private String imageBase64;


    @ManyToMany
    @JoinTable(
            name = "material_supplier",
            joinColumns = @JoinColumn(name = "material_id"),
            inverseJoinColumns = @JoinColumn(name = "supplier_id")
    )
    private List<Supplier> suppliers;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(mappedBy = "materials")
    private List<User> users;

    @ManyToMany(mappedBy = "materials")
    private List<Admin> admins;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;


    public void setImageBase64(String base64Image) {
    }
}