package asksef.assembler_support;

import asksef.controller.CityController;
import asksef.entity.City;
import asksef.entity.model.CityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CityModelAssemblerSupport extends RepresentationModelAssemblerSupport<City, CityModel> {

    public CityModelAssemblerSupport() {
        super(CityController.class, CityModel.class);
    }

    //    @org.springframework.lang.NonNull
//   @Override
//    public CityModel toModel(@NonNull City entity) {CityModel cityModel = instantiateModel(entity);
//        cityModel.setCountry(entity.getCo);
//        return null;
//    }

    @NonNull
    @Override
    public CityModel toModel(@NonNull City entity) {
        CityModel cityModel = instantiateModel(entity);
        cityModel.setCityId(entity.getCityId());
        cityModel.setCity(entity.getCity());
        cityModel.setCountry(entity.getCountry());
        return cityModel;
    }

    @NonNull
    @Override
    public CollectionModel<CityModel> toCollectionModel(@NonNull Iterable<? extends City> entities) {
        CollectionModel<CityModel> cityModels = super.toCollectionModel(entities);
        cityModels.add(linkTo(methodOn(CityController.class).all()).withSelfRel());
        return cityModels;
    }
}
