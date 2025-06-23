package asksef.controller;

import asksef.assembler.InvoiceModelAssembler;
import asksef.entity.Invoice;
import asksef.entity.service_impl.InvoiceService;
import jakarta.servlet.ServletResponse;
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
@RequestMapping(value = "/v0/invoice")
public class InvoiceController {

    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);
    private final InvoiceService invoiceService;
    private final InvoiceModelAssembler invoiceModelAssembler;

    public InvoiceController(InvoiceService invoiceService, InvoiceModelAssembler invoiceModelAssembler) {
        this.invoiceService = invoiceService;
        this.invoiceModelAssembler = invoiceModelAssembler;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Invoice>> all() {
        List<EntityModel<Invoice>> entityModelList = this.invoiceService.findAll().
                stream().map(invoiceModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(InvoiceController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Invoice> one(@PathVariable("id") Long id) {
        Invoice invoice = this.invoiceService.findById(id);
        return invoiceModelAssembler.toModel(invoice);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.invoiceService.delete(id);
        return new ResponseEntity<>("Invoice entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> add(@RequestBody Invoice invoice) {
        Invoice savedInvoice = this.invoiceService.save(invoice);
        EntityModel<Invoice> entityModel = invoiceModelAssembler.toModel(savedInvoice);
        log.info("Invoice addInvoice");
        return ResponseEntity
                .created(linkTo(methodOn(InvoiceController.class).one(savedInvoice.getInvoiceId()))
                        .toUri()).body(entityModel);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Invoice newInvoice, ServletResponse servletResponse) {
        Invoice updateInvoice = this.invoiceService.update(id, newInvoice);

        EntityModel<Invoice> entityModel = invoiceModelAssembler.toModel(updateInvoice);
        log.info("Invoice updated");
//        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(InvoiceController.class).one(id)).withSelfRel()
                        .toUri()).body(entityModel);
    }

}
