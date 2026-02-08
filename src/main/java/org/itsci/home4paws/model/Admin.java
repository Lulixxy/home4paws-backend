package org.itsci.home4paws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "admin")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Admin {
    @Id
    @Column(name = "username", length = 12)
    private String username;

    @Column(name = "password", length = 16)
    private String password;

    public Admin() {
    }

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
