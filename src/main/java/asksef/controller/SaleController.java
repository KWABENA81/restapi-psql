package asksef.controller;

import asksef.assembler.SaleModelAssembler;
import asksef.entity.Sale;
import asksef.entity.service.SaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/sale")
public class SaleController {

    private static final Logger log = LoggerFactory.getLogger(SaleController.class);
    private final SaleService saleService;
    private final SaleModelAssembler saleModelAssembler;

    public SaleController(SaleService saleService, SaleModelAssembler saleModelAssembler) {
        this.saleService = saleService;
        this.saleModelAssembler = saleModelAssembler;
        log.info("SaleController");
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Sale>> all() {
        List<EntityModel<Sale>> entityModelList = this.saleService.findAll().
                stream().map(saleModelAssembler::toModel).collect(Collectors.toList());
        log.info("All sales found");
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(SaleController.class).all()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Sale> one(@PathVariable Long id) {
        Sale sale = this.saleService.findById(id);
        log.info("Found sale with id: {}", id);
        return saleModelAssembler.toModel(sale);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Sale> findBySaleNr(@RequestParam(name = "nr") String nr) {
        Sale sale = this.saleService.findBySaleNr(nr);
        log.info("Found sale with nr: {}", nr);
        return saleModelAssembler.toModel(sale);
    }

    @PostMapping(value = "/add", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<Sale>> add(@RequestBody Sale sale) {
        Sale newSale = this.saleService.save(sale);
        EntityModel<Sale> entityModel = this.saleModelAssembler.toModel(sale);
        log.info("Sale added: {}", entityModel);
//        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(SaleController.class).one(sale.getSaleId()))
                        .toUri()).body(entityModel);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Sale newSale) {
        Sale updateSale = this.saleService.update(id, newSale);

        EntityModel<Sale> entityModel = this.saleModelAssembler.toModel(updateSale);
        log.info("Updating sale with id: " + id);
        //return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(SaleController.class).one(id))
                        .toUri()).body(entityModel);
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.saleService.delete(id);

        log.info("Deleted Sale with id {}", id);
        return new ResponseEntity<>("Sale entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

}
