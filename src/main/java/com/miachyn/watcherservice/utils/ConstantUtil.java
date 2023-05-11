package com.miachyn.watcherservice.utils;

import com.miachyn.watcherservice.exception.CryptoApiClientException;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class ConstantUtil {
    public static class Exception{
        public final static String NO_FOUNDED_PATTERN = "%s wasn't found by %s=%s";
        public static final String INCONSISTENT_OBJECTS_PATTERN = "Object : %s is inconsistent for %s";
        public static final String VALIDATION_ERROR = "Validation error";

    }
}
