package com.miachyn.watcherservice.handler;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.miachyn.watcherservice.utils.ConstantUtil.Exception.VALIDATION_ERROR;

@Data
@NoArgsConstructor
public class ValidationErrorResponse extends ApiErrorResponse{
    private List<ValidationMessage> validationMessages;

    public ValidationErrorResponse(List<ValidationMessage> validationMessages) {
        super(VALIDATION_ERROR);
        this.validationMessages = validationMessages;
    }
}
