package ca.pethappy.server.api.errors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class ValidationErrors {
    private final String formName;
    private final Map<String, String> errors;

    public ValidationErrors(String formName, BindingResult bindingResult) {
        this.formName = formName;
        this.errors = new HashMap<>();

        if (bindingResult != null) {
            for (FieldError fe : bindingResult.getFieldErrors()) {
                this.errors.put(fe.getField(), fe.getDefaultMessage());
            }
        }
    }

    public String getFormName() {
        return formName;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
