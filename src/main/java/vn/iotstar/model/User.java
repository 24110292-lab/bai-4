package vn.iotstar.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Users", uniqueConstraints = @UniqueConstraint(name = "uk_users_username", columnNames = "Username"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "Username", nullable = false, length = 50)
    private String username;

    @NotBlank
    @Column(name = "PasswordHash", nullable = false, length = 60)
    private String passwordHash;

    @NotBlank
    @Size(max = 100)
    @Column(name = "Fullname", nullable = false, length = 100)
    private String fullname;

    @NotBlank
    @Pattern(regexp = "^[+0-9][0-9 .()\\-]{7,19}$")
    @Column(name = "Phone", nullable = false, length = 20)
    private String phone;

    @Size(max = 50)
    @Column(name = "Images", length = 50)
    private String imageFile;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
}
