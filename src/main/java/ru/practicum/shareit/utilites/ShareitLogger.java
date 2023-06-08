package ru.practicum.shareit.utilites;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Stanislav Makarov
 */
@Slf4j
@UtilityClass
public class ShareitLogger {
    public static void returnErrorMsg(HttpStatus httpStatus, String msg) {
        log.info(msg);
        throw new ResponseStatusException(httpStatus, msg);
    }
}
