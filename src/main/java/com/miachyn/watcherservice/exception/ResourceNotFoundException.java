package com.miachyn.watcherservice.exception;

import static com.miachyn.watcherservice.utils.ConstantUtil.Exception.NO_FOUNDED_PATTERN;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
    super(message);
    }

    public ResourceNotFoundException(Class<?> resourceType, String fieldName, Object fieldValue) {
        super(String.format(NO_FOUNDED_PATTERN, resourceType.getSimpleName(), fieldName, fieldValue));
    }

}
