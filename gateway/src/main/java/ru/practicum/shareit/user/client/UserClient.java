package ru.practicum.shareit.user.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> getUserById(long userId) {
        log.info("getUserById:'{}'", userId);
        return get("/" + userId);
    }

    public ResponseEntity<Object> getUsersList() {
        log.info("getUsersList :");
        return get("");
    }

    public ResponseEntity<Object> save(UserDto userDto) {
        log.info("User save: '{}'", userDto);
        return post("", userDto);
    }

    public ResponseEntity<Object> updateUser(long userId, UserDto userDto) {
        log.info("User update:'{}','{}'", userId, userDto);
        return patch("/" + userId, userDto);
    }

    public void deleteUser(long userId) {
        log.info("delete user");
        delete("/" + userId);
    }
}