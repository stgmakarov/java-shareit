package ru.practicum.shareit.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.repository.JpaRepository;

@UtilityClass
public class ShareitTestUtils {
    public static <T> T createEntity(T entity, JpaRepository<T, Long> repository) {
        return repository.save(entity);
    }
}
