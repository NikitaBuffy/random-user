package ru.pominov.randomuser.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "cell")
    private String cell;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "birth_date")
    private LocalDateTime dateOfBirth;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "registered")
    private LocalDateTime registered;

    @Column(name = "registration_age")
    private Integer registrationAge;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_pictures_id")
    private UserPicture userPicture;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_location_id")
    private UserLocation userLocation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_login_id")
    private UserLogin userLogin;
}