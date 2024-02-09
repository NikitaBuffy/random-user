package ru.pominov.randomuser.service.user;

import java.util.Map;

public interface UserService {

    void saveToDatabase(Map<String, String> params);

    void getFromDatabase(String exportMethod, int numberOfUsers);
}
