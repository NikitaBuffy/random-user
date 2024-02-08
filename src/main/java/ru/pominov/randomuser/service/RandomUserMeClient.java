package ru.pominov.randomuser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
        // Построение URL с параметрами
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl);
        params.forEach(builder::queryParam);

        // Отправка GET запроса
        ResponseEntity<String> response = rest.getForEntity(builder.toUriString(), String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
