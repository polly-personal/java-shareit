package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoIn;

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

    public ResponseEntity<Object> create(long userId, ItemDtoIn itemDtoIn) {
        return post("", userId, itemDtoIn);
    }

    public ResponseEntity<Object> updateById(long userId, long itemId, ItemDtoIn updatedItemDtoIn) {
        return patch("/" + itemId, userId, updatedItemDtoIn);
    }

    public ResponseEntity<Object> deleteById(long itemId) {
        return delete("/" + itemId);
    }

    public ResponseEntity<Object> getById(long userId, long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllByOwnerId(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from=" + from + "&size=" + size, userId, parameters);
    }

    public ResponseEntity<Object> searchForUserByParameter(long userId, String text, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text=" + text + "&from=" + from + "&size=" + size, userId, parameters);
    }

    public ResponseEntity<Object> createComment(long userId, long itemId, CommentDtoIn commentDtoIn) {
        return post("/" + itemId + "/comment", userId, commentDtoIn);
    }
}
