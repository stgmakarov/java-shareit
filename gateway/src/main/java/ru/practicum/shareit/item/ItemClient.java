package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentInDtoGw;
import ru.practicum.shareit.item.dto.ItemInDtoGw;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(ItemInDtoGw itemInDtoGw, long userId) {
        return post("", userId, itemInDtoGw);
    }

    public ResponseEntity<Object> updateItem(long itemId, ItemInDtoGw itemInDtoGw, long userId) {
        return patch("/" + itemId, userId, itemInDtoGw);
    }

    public ResponseEntity<Object> getById(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllUserItems(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findByText(String text) {
        if (text.trim().isEmpty())
            return generateEmptyJsonResponse();
        Map<String, Object> parameters = Map.of("text", text);
        return get("/search", parameters);
    }

    public ResponseEntity<Object> getItemComment(long itemId) {
        return get("/" + itemId + "/comment");
    }

    public ResponseEntity<Object> addComment(long itemId, long userId, String text) {
        return post("/" + itemId + "/comment", userId, new CommentInDtoGw(text));
    }
}