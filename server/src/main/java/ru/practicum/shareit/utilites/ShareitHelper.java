package ru.practicum.shareit.utilites;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Stanislav Makarov
 */
@Slf4j
@UtilityClass
public class ShareitHelper {
    public static void returnErrorMsg(HttpStatus httpStatus, String msg) {
        log.info(msg);
        throw new ResponseStatusException(httpStatus, msg);
    }

    public static Pageable getPage(Long size, Long from) {
        Pageable pageable = Pageable.unpaged();
        if (size != null && from != null) {
            if (size > 0 && from >= 0)
                pageable = PageRequest.of((int) (from / size), size.intValue());
            else
                returnErrorMsg(HttpStatus.BAD_REQUEST, "Некорректные параметры");
        }
        return pageable;
    }
}
