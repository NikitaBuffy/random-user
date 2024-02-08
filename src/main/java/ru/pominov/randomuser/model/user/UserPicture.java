package ru.pominov.randomuser.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_pictures")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pictures_id")
    private Long id;

    @Column(name = "large_url")
    private String large;

    @Column(name = "medium_url")
    private String medium;

    @Column(name = "thumbnail_url")
    private String thumbnail;
}
