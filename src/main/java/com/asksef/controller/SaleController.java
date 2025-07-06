package com.asksef.controller;

import com.asksef.assembler.InvoiceModelAssemblerSupport;
import com.asksef.assembler.SaleModelAssemblerSupport;
import com.asksef.assembler.StaffModelAssemblerSupport;
import com.asksef.entity.core.Invoice;
import com.asksef.entity.core.Sale;
import com.asksef.entity.core.Staff;
import com.asksef.entity.model.InvoiceModel;
import com.asksef.entity.model.SaleModel;
import com.asksef.entity.model.StaffModel;
import com.asksef.entity.repository.SaleRepository;
import com.asksef.entity.service_impl.SaleService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/sale")
public class SaleController {

    private static final Logger log = LoggerFactory.getLogger(SaleController.class);
    private final SaleService saleService;
    private final SaleRepository saleRepository;
    private final SaleModelAssemblerSupport saleModelAssemblerSupport;

    public SaleController(SaleService saleService, SaleRepository saleRepository, SaleModelAssemblerSupport saleModelAssemblerSupport) {
        this.saleService = saleService;
        this.saleRepository = saleRepository;
        this.saleModelAssemblerSupport = saleModelAssemblerSupport;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<SaleModel>> all() {
        List<Sale> entityList = this.saleService.findAll().stream().toList();
        return new ResponseEntity<>(this.saleModelAssemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    public ResponseEntity<SaleModel> one(@PathVariable Long id) {
        return this.saleRepository.findById(id)
                .map(saleModelAssemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<SaleModel> findBySaleNr(@RequestParam(name = "nr") String nr) {
        Sale sale = this.saleService.findBySaleNr(nr);
        log.info("Found sale with nr: {}", nr);
        return new ResponseEntity<>(saleModelAssemblerSupport.toModel(sale), HttpStatus.OK);
    }

    @PostMapping(value = "/add", produces = "application/hal+json")
    public ResponseEntity<SaleModel> add(@RequestBody SaleModel saleModel) {
        Sale savedSale = this.saleService.save(saleModel);
        SaleModel entityModel = this.saleModelAssemblerSupport.toModel(savedSale);
//
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Sale newSale) {
        Sale updateSale = this.saleService.update(id, newSale);
        SaleModel entityModel = this.saleModelAssemblerSupport.toModel(updateSale);
        //
        return ResponseEntity
                .created(linkTo(methodOn(SaleController.class).one(id))
                        .toUri()).body(entityModel);
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.saleService.delete(id);
        log.info("Deleted Sale with id {}", id);
        return new ResponseEntity<>("Sale entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}/invoice", produces = "application/hal+json")
    public ResponseEntity<InvoiceModel> findInvoiceOfSale(@PathVariable("id") Long id) {
        @NonNull Invoice invoice = this.saleService.findInvoiceOfSale(id);
        //  build  model
        @NonNull InvoiceModel invoiceModel = new InvoiceModelAssemblerSupport().toModel(invoice);
        invoiceModel.add(linkTo(methodOn(SaleController.class).findInvoiceOfSale(id)).withRel("Sale Invoice"));
        invoiceModel.add(linkTo(methodOn(SaleController.class).one(id)).withRel("Sale"));
        return new ResponseEntity<>(invoiceModel, HttpStatus.OK);
    }
    @GetMapping(value = "/{id}/staff", produces = "application/hal+json")
    public ResponseEntity<StaffModel> findStaffOfSale(@PathVariable("id") Long id) {
        Staff staff = this.saleService.findStaffOfSale(id);
        //  build  model
        @NonNull StaffModel staffModel = new StaffModelAssemblerSupport().toModel(staff);
        staffModel.add(linkTo(methodOn(SaleController.class).findStaffOfSale(id)).withRel("Sale Staff"));
        staffModel.add(linkTo(methodOn(SaleController.class).one(id)).withRel("Sale"));
        return new ResponseEntity<>(staffModel, HttpStatus.OK);
    }
}
