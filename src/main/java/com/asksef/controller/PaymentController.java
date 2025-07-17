package com.asksef.controller;

import com.asksef.assembler.InvoiceModelAssemblerSupport;
import com.asksef.assembler.PaymentModelAssemblerSupport;
import com.asksef.assembler.StaffModelAssemblerSupport;
import com.asksef.entity.core.Invoice;
import com.asksef.entity.core.Payment;
import com.asksef.entity.core.Staff;
import com.asksef.entity.model.InvoiceModel;
import com.asksef.entity.model.PaymentModel;
import com.asksef.entity.model.StaffModel;
import com.asksef.entity.repository.PaymentRepository;
import com.asksef.entity.service_impl.PaymentService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/api/payment")
public class PaymentController {

    private final PaymentModelAssemblerSupport paymentModelAssemblerSupport;
    private final PaymentService paymentService;
    private final PagedResourcesAssembler<Payment> pagedResourcesAssembler;
    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentModelAssemblerSupport paymentModelAssemblerSupport,
                             PaymentService paymentService,
                             PagedResourcesAssembler<Payment> pagedResourcesAssembler,
                             PaymentRepository paymentRepository) {
        this.paymentModelAssemblerSupport = paymentModelAssemblerSupport;
        this.paymentService = paymentService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<PaymentModel>> all() {
        List<Payment> entityList = paymentService.findAll().stream().toList();
        return new ResponseEntity<>(this.paymentModelAssemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    public ResponseEntity<PaymentModel> one(@PathVariable("id") Long id) {
        return this.paymentRepository.findById(id)
                .map(this.paymentModelAssemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/nr", produces = "application/hal+json")
    public ResponseEntity<PaymentModel> paymentByNr(@RequestParam(value = "pn") String nr) {
        Payment payment = this.paymentService.findByPayNr(nr);
        log.info("Payment found: {}", payment);
        return new ResponseEntity<>(paymentModelAssemblerSupport.toModel(payment), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.paymentService.delete(id);
        log.info("Payment deleted: {}", id);
        return new ResponseEntity<>("Payment entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/add", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PaymentModel> add(@RequestBody PaymentModel paymentModel) {
        Payment savedPayment = this.paymentService.save(paymentModel);
        @NonNull PaymentModel entityModel = this.paymentModelAssemblerSupport.toModel(savedPayment);
        log.info("Added payment: {}", entityModel);

        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Payment newPayment) {
        Payment updatePayment = this.paymentService.update(id, newPayment);

        @NonNull PaymentModel entityModel = paymentModelAssemblerSupport.toModel(updatePayment);
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
        staffModel.add(linkTo(methodOn(PaymentController.class).findStaffOnPayment(id)).withRel("Staff on Payment"));
        return new ResponseEntity<>(staffModel, HttpStatus.OK);
    }

    @GetMapping(value = "{id}/invoice", produces = "application/hal+json")
    public ResponseEntity<InvoiceModel> findPaymentInvoice(@PathVariable("id") Long id) {
        Invoice invoice = this.paymentService.findPaymentInvoice(id);
        @NonNull InvoiceModel invoiceModel = new InvoiceModelAssemblerSupport().toModel(invoice);
        invoiceModel.add(linkTo(methodOn(PaymentController.class).findPaymentInvoice(id)).withRel("Payment Invoice"));
        return new ResponseEntity<>(invoiceModel, HttpStatus.OK);
    }

    @GetMapping(value = "/pageable")
    public ResponseEntity<PagedModel<PaymentModel>> pageable(Pageable pageable) {
        Page<Payment> entityPage = paymentService.findAll(pageable);

        PagedModel<PaymentModel> pagedModel = pagedResourcesAssembler.toModel(entityPage, paymentModelAssemblerSupport);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }
}
