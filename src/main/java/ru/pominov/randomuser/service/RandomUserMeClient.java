package ru.pominov.randomuser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * Класс для отправки запроса на получение пользователей в сервис RandomUserMeClient
 */
@Service
@Slf4j
public class RandomUserMeClient {

    private final RestTemplate rest;
    private final String serverUrl;


    public RandomUserMeClient(@Autowired RestTemplate restTemplate, @Value("${randomuserme.url}") String serverUrl) {
        this.rest = restTemplate;
        this.serverUrl = serverUrl;
    }

    /**
     * Метод принимает список параметров (могут отсутствовать) и строит URL для отправки запроса.
     *
     * @param params Набор параметров для отправки запроса на получение пользователей
     *               (прописывается в консоли пользователем, есть подсказка)
     * @return Ответ запроса или пробрасывается исключение HttpStatusCodeException, если ответ сервера 4xx или 5xx
     */
    public ResponseEntity<String> getRandomUsers(Map<String, String> params) {
        try {
            // Построение URL с параметрами
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl);
            params.forEach(builder::queryParam);

            // Отправка GET запроса
            ResponseEntity<String> response = rest.getForEntity(builder.toUriString(), String.class);

            return response;
        } catch (HttpStatusCodeException e) {
            log.error("Failed to fetch random users from API. Status code: {}", e.getStatusCode());
            log.error("Error message: {}", e.getMessage());
            throw e;
        }
    }
}
