package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentInDto;
import ru.practicum.shareit.item.dto.ItemInDto;

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

    public ResponseEntity<Object> createItem(ItemInDto itemInDto, long userId) {
        return post("", userId, itemInDto);
    }

    public ResponseEntity<Object> updateItem(long itemId, ItemInDto itemInDto, long userId) {
        return patch("/" + itemId, userId, itemInDto);
    }

    public ResponseEntity<Object> getByIdDto2(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllUserItems(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findByText(String text) {
        Map<String, Object> parameters = Map.of("text", text);
        return get("/search", parameters);
    }

    public ResponseEntity<Object> getItemComment(long itemId) {
        return get("/" + itemId + "/comment");
    }

    public ResponseEntity<Object> addComment(long itemId, long userId, String text) {
        return post("/" + itemId + "/comment", userId, new CommentInDto(text));
    }
}