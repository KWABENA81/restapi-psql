package com.asksef.errors;

import ch.qos.logback.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@Slf4j
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String strObjValue;

    public String getValue() {
        return strObjValue;
    }

    public CustomResourceNotFoundException(String resource, String field, String prop, Object value) {
        super(String.format("Resource %s not found, with %s: ", resource, field));
        String property = StringUtil.isNullOrEmpty(prop) ? "Property Unknown" : prop;

        strObjValue = String.valueOf(value);;
        log.info("Resource {} not found, with {}: {} {} ", resource, field, property, strObjValue);
    }

}
