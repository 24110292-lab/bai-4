package vn.iotstar.util;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

public final class FormValidation {
    private FormValidation() {
    }

    public static Map<String, String> validate(Validator validator, Object form) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (ConstraintViolation<Object> violation : validator.validate(form)) {
            errors.putIfAbsent(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errors;
    }
}
