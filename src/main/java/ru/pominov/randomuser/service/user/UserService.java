package ru.pominov.randomuser.service.user;

public interface UserService {

    void saveToDatabase(String userJsonData);

    void getFromDatabase();
}
