package asksef.assembler;

import asksef.controller.CityController;
import asksef.entity.City;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@NonNullApi
public class CityModelAssembler implements RepresentationModelAssembler<City, EntityModel<City>> {
    @Override
    public EntityModel<City> toModel(City city) {
        try {
            return EntityModel.of(city,
                    linkTo(methodOn(CityController.class).one(city.getCityId())).withSelfRel(),
//                    linkTo(methodOn(CityController.class).add(city)).withSelfRel(),
//                    linkTo(methodOn(CityController.class).update(city.getCityId(), city)).withSelfRel(),
//                    linkTo(methodOn(CityController.class).delete(city.getCityId())).withSelfRel(),
                    linkTo(methodOn(CityController.class).all()).withRel("all"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
