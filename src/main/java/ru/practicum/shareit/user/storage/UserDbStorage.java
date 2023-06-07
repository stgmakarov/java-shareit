package ru.practicum.shareit.user.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utilites.ShareitLogger;

import java.util.List;
import java.util.regex.Pattern;

@Component
@Primary
public class UserDbStorage implements UserStorage {
    @Autowired
    private UserRepository userRepository;

    private final Pattern emailPattern =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public User create(String name, String email) {
        checkEmail(email);
        return userRepository.save(new User(name, email));
    }

    @Override
    public User get(String email) {
        User user = userRepository.findDistinctByEmailIgnoreCase(email);
        if (user == null)
            ShareitLogger.returnErrorMsg(HttpStatus.NOT_FOUND, String.format("EMail %s не найден", email));
        return user;
    }

    @Override
    public User get(long id) {
        return getById(id);
    }

    private User getById(long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) ShareitLogger.returnErrorMsg(HttpStatus.NOT_FOUND, String.format("ID %d не найден", id));
        return user;
    }

    @Override
    public User update(long userId, User user) {
        User existUser = getById(userId);

        if (user.getName().isEmpty() && user.getEmail().isEmpty()) {
            ShareitLogger.returnErrorMsg(HttpStatus.BAD_REQUEST, "Не указаны поля для изменения");
        }

        //смена почты
        if (!user.getEmail().isEmpty()) {
            String email = user.getEmail().toLowerCase();
            if (!existUser.getEmail().equals(email)) {
                checkEmail(email);
                existUser.setEmail(user.getEmail());
            }
        }

        if (!user.getName().isEmpty()) {
            existUser.setName(user.getName());
        }

        return userRepository.save(existUser);
    }

    @Override
    public void delete(long userId) {
        //Проверка существования
        getById(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public User create(User user) {
        return create(user.getName(), user.getEmail());
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    private void checkEmail(String email) {
        if (email.isBlank()) {
            ShareitLogger.returnErrorMsg(HttpStatus.BAD_REQUEST, "Email должен быть указан.");
        } else if (!checkEmailFormat(email)) {
            ShareitLogger.returnErrorMsg(HttpStatus.BAD_REQUEST, "Email в неправильном формате.");
        }
    }

    private boolean checkEmailFormat(String email) {
        return emailPattern.matcher(email).matches();
    }
}
