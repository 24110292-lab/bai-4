package vn.iotstar.form;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import vn.iotstar.util.FormValidation;

class FormValidationTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void createValidator() {
        validatorFactory = jakarta.validation.Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    void registrationRejectsInvalidFields() {
        RegisterForm form = new RegisterForm();
        form.setUsername("x");
        form.setPassword("short");
        form.setConfirmPassword("different");
        form.setFullname("");
        form.setPhone("not-a-phone");

        Map<String, String> errors = FormValidation.validate(validator, form);
        assertTrue(errors.containsKey("username"));
        assertTrue(errors.containsKey("password"));
        assertTrue(errors.containsKey("fullname"));
        assertTrue(errors.containsKey("phone"));
    }

    @Test
    void profileRequiresValidNameAndPhone() {
        ProfileForm form = new ProfileForm();
        form.setFullname("   ");
        form.setPhone("123");

        Map<String, String> errors = FormValidation.validate(validator, form);
        assertTrue(errors.containsKey("fullname"));
        assertTrue(errors.containsKey("phone"));
    }

    @Test
    void validLoginHasNoValidationErrors() {
        LoginForm form = new LoginForm();
        form.setUsername("user_123");
        form.setPassword("ValidP4ssword!");

        assertTrue(FormValidation.validate(validator, form).isEmpty());
    }
}
