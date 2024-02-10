package ru.pominov.randomuser.util;

import com.google.common.hash.Hashing;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;
import ru.pominov.randomuser.model.User;
import ru.pominov.randomuser.model.UserLocation;
import ru.pominov.randomuser.model.UserLogin;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class JsonDeserializer {

    /**
     * @param jsonString JSON, полученный в теле ответа
     * @return Список сущностей пользователей
     */
    public List<User> deserialize(String jsonString) {
        List<User> users = new ArrayList<>();
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonObject().getAsJsonArray("results");

        for (JsonElement jsonElement : jsonArray) {
            users.add(deserializeUser(jsonElement.getAsJsonObject()));
        }

        return users;
    }

    /**
     * Метод десериализует JSON с данными пользователя и преобразует в объект User.
     * Так как API RandomUserMe позволяет управлять выборкой данных путем указания специальных параметров в запросе,
     * перед сеттером происходит проверка на наличие поля в JSON.
     * Так как изначальный JSON имеет структуру с вложенными объектами, написал свой десериализатор на основе созданных
     * объектов User, UserLocation и UserLogin.
     *
     * @param userObject JSON с пользователем
     * @return десериализованный из JSON объект User
     */
    private User deserializeUser(JsonObject userObject) {
        User user = new User();

        if (userObject.has("name")) {
            /* API RandomUserMe позволяет убирать/добавлять только весь объект 'name' целиком,
               поэтому проверка на наличие внутренних полей не требуется */
            user.setTitle(userObject.getAsJsonObject("name").get("title").getAsString());
            user.setFirstName(userObject.getAsJsonObject("name").get("first").getAsString());
            user.setLastName(userObject.getAsJsonObject("name").get("last").getAsString());
        }
        if (userObject.has("email")) {
            user.setEmail(userObject.get("email").getAsString());
        }
        if (userObject.has("phone")) {
            user.setPhone(userObject.get("phone").getAsString());
        }
        if (userObject.has("cell")) {
            user.setCell(userObject.get("cell").getAsString());
        }
        if (userObject.has("nat")) {
            user.setNationality(userObject.get("nat").getAsString());
        }
        if (userObject.has("gender")) {
            user.setGender(userObject.get("gender").getAsString());
        }
        if (userObject.has("dob")) {
            /* API RandomUserMe позволяет убирать/добавлять только весь объект 'dob' целиком,
               поэтому проверка на наличие внутренних полей не требуется */
            user.setAge(userObject.getAsJsonObject("dob").get("age").getAsInt());
            user.setDateOfBirth(LocalDateTime.parse(userObject.getAsJsonObject("dob").get("date").getAsString(), DateTimeFormatter.ISO_DATE_TIME));
        }
        if (userObject.has("registered")) {
            /* API RandomUserMe позволяет убирать/добавлять только весь объект 'registered' целиком,
               поэтому проверка на наличие внутренних полей не требуется */
            user.setRegistered(LocalDateTime.parse(userObject.getAsJsonObject("registered").get("date").getAsString(), DateTimeFormatter.ISO_DATE_TIME));
            user.setRegistrationAge(userObject.getAsJsonObject("registered").get("age").getAsInt());
        }

        if (userObject.has("picture")) {
            /* API RandomUserMe позволяет убирать/добавлять только весь объект 'picture' целиком,
               поэтому проверка на наличие внутренних полей не требуется */
            user.setLargePicture(userObject.getAsJsonObject("picture").get("large").getAsString());
            user.setMediumPicture(userObject.getAsJsonObject("picture").get("medium").getAsString());
            user.setThumbnailPicture(userObject.getAsJsonObject("picture").get("thumbnail").getAsString());
        }

        if (userObject.has("location")) {
            UserLocation userLocation = new UserLocation();
            /* API RandomUserMe позволяет убирать/добавлять только весь объект 'location' целиком,
               поэтому проверка на наличие внутренних полей не требуется */
            userLocation.setCity(userObject.getAsJsonObject("location").get("city").getAsString());
            userLocation.setStreetName(userObject.getAsJsonObject("location").getAsJsonObject("street").get("name").getAsString());
            userLocation.setStreetNumber(userObject.getAsJsonObject("location").getAsJsonObject("street").get("number").getAsInt());
            userLocation.setState(userObject.getAsJsonObject("location").get("state").getAsString());
            userLocation.setCountry(userObject.getAsJsonObject("location").get("country").getAsString());
            userLocation.setPostcode(userObject.getAsJsonObject("location").get("postcode").getAsString());
            userLocation.setLat(userObject.getAsJsonObject("location").getAsJsonObject("coordinates").get("latitude").getAsFloat());
            userLocation.setLon(userObject.getAsJsonObject("location").getAsJsonObject("coordinates").get("longitude").getAsFloat());
            userLocation.setTimezoneOffset(userObject.getAsJsonObject("location").getAsJsonObject("timezone").get("offset").getAsString());
            userLocation.setTimezoneDescription(userObject.getAsJsonObject("location").getAsJsonObject("timezone").get("description").getAsString());
            user.setUserLocation(userLocation);
        }

        if (userObject.has("login")) {
            UserLogin userLogin = new UserLogin();
            /* API RandomUserMe позволяет убирать/добавлять только весь объект 'login' целиком,
               поэтому проверка на наличие внутренних полей не требуется */
            userLogin.setUuid(UUID.fromString(userObject.getAsJsonObject("login").get("uuid").getAsString()));
            userLogin.setUsername(userObject.getAsJsonObject("login").get("username").getAsString());
            // Шифрование пароля алгоритмом SHA-256 (формальность для хранения в БД, ведь изначально пароль в JSON отправляется, что не надежно)
            userLogin.setPassword(Hashing.sha256()
                    .hashString(userObject.getAsJsonObject("login").get("password").getAsString(), StandardCharsets.UTF_8)
                    .toString()
            );
            user.setUserLogin(userLogin);
        }

        return user;
    }
}
