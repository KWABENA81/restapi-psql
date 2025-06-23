package asksef.errors;

import jakarta.validation.constraints.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotNull
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCountryName {
    String message() default "Country already exists";

    Class<?>[] grps() default {};

    Class<? extends Throwable>[] causes() default {};

    Class<? extends Throwable>[] exceptions() default {};
    //  Class<? extends Payload>[] exceptionsCause() default {};
}
