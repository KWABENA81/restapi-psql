package asksef.errors;

import asksef.entity.Country;
import asksef.entity.service.CountryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class CountryNameValidator implements ConstraintValidator<UniqueCountryName, Country> {

    private static final Logger log = LoggerFactory.getLogger(CountryNameValidator.class);

    private final CountryService countryService;

    public CountryNameValidator(CountryService countryService) {
        this.countryService = countryService;
    }

    @Override
    public boolean isValid(Country country, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(country)) {
            return true;
        } else {
            return Objects.nonNull(countryService.findByName(country.getCountry()));
        }
    }
}
