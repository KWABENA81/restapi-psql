package asksef.controller;

import asksef.assembler.InvoiceModelAssemblerSupport;
import asksef.assembler.PaymentModelAssembler;
import asksef.assembler_support.StaffModelAssemblerSupport;
import asksef.entity.Invoice;
import asksef.entity.Payment;
import asksef.entity.Staff;
import asksef.entity.entity_model.InvoiceModel;
import asksef.entity.entity_model.StaffModel;
import asksef.entity.service.PaymentService;
import lombok.NonNull;
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
@RequestMapping(value = "/api/payment")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentModelAssembler paymentModelAssembler;
    private final PaymentService paymentService;

    public PaymentController(PaymentModelAssembler paymentModelAssembler, PaymentService paymentService) {
        this.paymentModelAssembler = paymentModelAssembler;
        this.paymentService = paymentService;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Payment>> all() {
        List<EntityModel<Payment>> entityModelList = paymentService.findAll()
                .stream().map(paymentModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(PaymentController.class).all()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Payment> one(@PathVariable("id") Long id) {
        Payment payment = this.paymentService.findById(id);
        return paymentModelAssembler.toModel(payment);
    }

    @GetMapping(value = "/nr", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Payment> paymentByNr(@RequestParam(value = "pn") String nr) {
        Payment payment = this.paymentService.findByPayNr(nr);
        log.info("Payment found: {}", payment);
        return paymentModelAssembler.toModel(payment);
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.paymentService.delete(id);

        log.info("Payment deleted: {}", id);
        return new ResponseEntity<>("Payment entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/add", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> add(@RequestBody Payment payment) {
        Payment savedPayment = this.paymentService.save(payment);
        EntityModel<Payment> entityModel = paymentModelAssembler.toModel(savedPayment);
        log.info("Added payment: {}", entityModel);

//        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(PaymentController.class).one(savedPayment.getPaymentId()))
                        .toUri()).body(entityModel);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Payment newPayment) {
        Payment updatePayment = this.paymentService.update(id, newPayment);

        EntityModel<Payment> entityModel = paymentModelAssembler.toModel(updatePayment);
        log.info("Updated payment: {}", entityModel);
//        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(PaymentController.class).one(id))
                        .toUri()).body(entityModel);
    }

    @GetMapping(value = "{id}/staff", produces = "application/hal+json")
    public ResponseEntity<StaffModel> findStaffOnPayment(@PathVariable("id") Long id) {
        Staff staff = this.paymentService.findStaffOnPayment(id);
        @NonNull StaffModel staffModel = new StaffModelAssemblerSupport().toModel(staff);
        staffModel.add(linkTo(methodOn(InvoiceController.class).findCustomerOnInvoice(id)).withRel("Customer On Invoice"));
        return new ResponseEntity<>(staffModel, HttpStatus.OK);
    }

    @GetMapping(value = "{id}/invoice", produces = "application/hal+json")
    public ResponseEntity<InvoiceModel> findInvoiceOnPayment(@PathVariable("id") Long id) {
        Invoice invoice = this.paymentService.findInvoiceOnPayment(id);
        @NonNull InvoiceModel invoiceModel = new InvoiceModelAssemblerSupport().toModel(invoice);
        invoiceModel.add(linkTo(methodOn(InvoiceController.class).findCustomerOnInvoice(id)).withRel("Customer On Invoice"));
        return new ResponseEntity<>(invoiceModel, HttpStatus.OK);
    }
}
