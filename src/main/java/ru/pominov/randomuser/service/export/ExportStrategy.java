package ru.pominov.randomuser.service.export;

import ru.pominov.randomuser.model.User;
import java.util.List;

public interface ExportStrategy {

    void export(List<User> users);
}
