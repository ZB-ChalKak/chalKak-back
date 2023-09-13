package com.btb.chalKak.common.util;

import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ValidationUtils {

    public static String getErrorMessageFromBindingResult(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        return errors.get(0).getDefaultMessage();
    }
}
