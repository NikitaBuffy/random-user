CREATE TABLE IF NOT EXISTS user_location (
    user_location_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    city varchar(50),
    street_name VARCHAR(100),
    street_number INT,
    state VARCHAR(50),
    country VARCHAR(30),
    postcode VARCHAR(50),
    latitude FLOAT,
    longitude FLOAT,
    tz_offset varchar(20),
    tz_desc VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS user_login (
    user_login_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    uuid UUID,
    username VARCHAR(100),
    password VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR(20),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email varchar(255),
    phone varchar(100),
    cell varchar(100),
    gender varchar(10),
    age INT,
    birth_date TIMESTAMP,
    nationality VARCHAR(20),
    registered TIMESTAMP,
    registration_age INT,
    large_picture VARCHAR(255),
    medium_picture VARCHAR(255),
    thumbnail_picture VARCHAR(255),
    user_location_id BIGINT REFERENCES user_location(user_location_id),
    user_login_id BIGINT REFERENCES user_login(user_login_id)
);
