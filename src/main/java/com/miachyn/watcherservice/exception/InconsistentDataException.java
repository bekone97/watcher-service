package com.miachyn.watcherservice.exception;

import static com.miachyn.watcherservice.utils.ConstantUtil.Exception.INCONSISTENT_OBJECTS_PATTERN;

public class InconsistentDataException extends RuntimeException {

    public InconsistentDataException(Object firstObject, Object secondObject) {
        super(String.format(INCONSISTENT_OBJECTS_PATTERN,firstObject,secondObject));
    }
}
