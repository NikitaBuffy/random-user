package ru.pominov.randomuser.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_location")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_location_id")
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "street_number")
    private Integer streetNumber;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "latitude")
    private float lat;

    @Column(name = "longitude")
    private float lon;

    @Column(name = "tz_offset")
    private String timezoneOffset;

    @Column(name = "tz_desc")
    private String timezoneDescription;
}
