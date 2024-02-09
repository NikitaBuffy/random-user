package ru.pominov.randomuser.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "user_login")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_login_id")
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
}
