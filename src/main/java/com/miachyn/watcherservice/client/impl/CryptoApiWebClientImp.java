package com.miachyn.watcherservice.client.impl;

import com.miachyn.watcherservice.client.CryptoApiClient;
import com.miachyn.watcherservice.dto.CurrencyDtoClientResponse;
import com.miachyn.watcherservice.entity.Currency;
import com.miachyn.watcherservice.exception.CryptoApiClientException;
import com.miachyn.watcherservice.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Slf4j
public class CryptoApiWebClientImp implements CryptoApiClient {

    private final WebClient webClient;


    public CryptoApiWebClientImp(@Value("${client.url}") String clientUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(clientUrl)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public CurrencyDtoClientResponse getCurrencyDtoById(Long id) {
        log.info("Get currency by id : {}", id);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("id", id)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response-> handleError(response.statusCode()))
                .bodyToMono(CurrencyDtoClientResponse[].class)
                .retry(3)
                .map(array -> array[0])
                .block();
    }

    private Mono<? extends Throwable> handleError(HttpStatusCode statusCode) {
            log.error("An error occurred with status code : {}",statusCode);
            return Mono.error(new ResourceNotFoundException("An error occurred during getting the from api with status code "));

    }
}

