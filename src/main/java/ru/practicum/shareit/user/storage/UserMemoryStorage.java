package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utilites.ShareitLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Stanislav Makarov
 */
@Component
@Slf4j
public class UserMemoryStorage implements UserStorage {
    private long lastUserId;
    private final Map<Long, User> userIdMap = new HashMap<>();
    private final Map<String, Long> userEmailMap = new HashMap<>();
    private final Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private synchronized long lastUserIdIncrement() {
        return ++lastUserId;
    }

    @Override
    public User create(String name, String email) {
        checkEmail(email);
        long userId = lastUserIdIncrement();
        User user = new User(userId, name, email);
        userIdMap.put(userId, user);
        userEmailMap.put(user.getEmail().toLowerCase(), userId);
        return user;
    }

    private void checkEmail(String email) {
        if (email.isBlank()) {
            ShareitLogger.returnErrorMsg(HttpStatus.BAD_REQUEST, "Email должен быть указан.");
        } else if (!checkEmailFormat(email)) {
            ShareitLogger.returnErrorMsg(HttpStatus.BAD_REQUEST, "Email в неправильном формате.");
        } else if (checkEmailIsBusy(email)) {
            ShareitLogger.returnErrorMsg(HttpStatus.CONFLICT, String.format("Email %s уже зарегистрирован.", email));
        }
    }

    @Override
    public User get(String email) {
        if (!userEmailMap.containsKey(email.toLowerCase())) {
            ShareitLogger.returnErrorMsg(HttpStatus.NOT_FOUND, String.format("EMail %s не найден", email));
        }
        return userIdMap.get(userEmailMap.get(email.toLowerCase()));
    }

    @Override
    public User get(long id) {
        if (!userIdMap.containsKey(id)) {
            ShareitLogger.returnErrorMsg(HttpStatus.NOT_FOUND, String.format("ID %d не найден", id));
        }
        return userIdMap.get(id);
    }

    private boolean checkEmailIsBusy(String email) {
        return userEmailMap.containsKey(email.toLowerCase());
    }

    private boolean checkEmailFormat(String email) {
        return emailPattern.matcher(email).matches();
    }

    @Override
    public User update(long userId, User user) {
        if (!userIdMap.containsKey(userId)) {
            ShareitLogger.returnErrorMsg(HttpStatus.NOT_FOUND, String.format("ID %d не найден", userId));
        }
        if (user.getName().isEmpty() && user.getEmail().isEmpty()) {
            ShareitLogger.returnErrorMsg(HttpStatus.BAD_REQUEST, "Не указаны поля для изменения");
        }
        User existUser = userIdMap.get(userId);
        //смена почты
        if (!user.getEmail().isEmpty()) {
            String email = user.getEmail().toLowerCase();
            if (!existUser.getEmail().equals(email)) {
                checkEmail(email);
                userEmailMap.remove(existUser.getEmail().toLowerCase());
                existUser.setEmail(user.getEmail());
            }
        }

        if (!user.getName().isEmpty()) {
            existUser.setName(user.getName());
        }

        userIdMap.put(existUser.getId(), existUser);
        return existUser;
    }

    @Override
    public void delete(long userId) {
        if (!userIdMap.containsKey(userId)) {
            ShareitLogger.returnErrorMsg(HttpStatus.NOT_FOUND, String.format("ID %d не найден", userId));
        }
        userEmailMap.remove(userIdMap.get(userId).getEmail().toLowerCase());
        userIdMap.remove(userId);
    }

    @Override
    public User create(User user) {
        return create(user.getName(), user.getEmail());
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userIdMap.values());
    }

}
