package asksef.assembler;

import asksef.controller.CountryController;
import asksef.entity.Country;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@NonNullApi
public class CountryModelAssembler implements RepresentationModelAssembler<Country, EntityModel<Country>> {
    @Override
    public EntityModel<Country> toModel(Country country) {
        try {
            return EntityModel.of(country,
                    linkTo(methodOn(CountryController.class).one(country.getCountryId())).withSelfRel(),
                    //linkTo(methodOn(CountryController.class).add(country)).withSelfRel(),
                    //linkTo(methodOn(CountryController.class).update(country.getCountryId(), country)).withSelfRel(),
                    //linkTo(methodOn(CountryController.class).delete(country.getCountryId())).withSelfRel(),
                    linkTo(methodOn(CountryController.class).findLikeName(country.getCountry())).withSelfRel(),
                    linkTo(methodOn(CountryController.class).all()).withRel("all")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}