package ru.pominov.randomuser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@Slf4j
public class RandomUserMeClient {

    private final RestTemplate rest;
    private final String serverUrl;


    public RandomUserMeClient(@Autowired RestTemplate restTemplate, @Value("${randomuserme.url}") String serverUrl) {
        this.rest = restTemplate;
        this.serverUrl = serverUrl;
    }

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
