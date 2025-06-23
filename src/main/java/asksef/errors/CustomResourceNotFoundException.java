package asksef.errors;

import ch.qos.logback.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(CustomResourceNotFoundException.class);

    private final Long lgValue;

    public Long getValue() {
        return lgValue;
    }

    public CustomResourceNotFoundException(String resource, String field, String prop, Long value) {
        super(String.format("Resource %s not found, with %s: ", resource, field));
        this.lgValue = StringUtil.isNullOrEmpty(String.valueOf(value)) ? 0L : value;
        String property = StringUtil.isNullOrEmpty(prop) ? "Property Unknown" : prop;
        log.info("Resource {} not found, with {}: {} {} ", resource, field, property, this.lgValue);
    }

}
