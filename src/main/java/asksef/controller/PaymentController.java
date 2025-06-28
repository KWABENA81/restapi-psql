package asksef.controller;

import asksef.assembler.PaymentModelAssembler;
import asksef.entity.Payment;
import asksef.entity.service.PaymentService;
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

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Payment>> all() {
        List<EntityModel<Payment>> entityModelList = paymentService.findAll()
                .stream().map(paymentModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(PaymentController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Payment> one(@PathVariable("id") Long id) {
        Payment payment = this.paymentService.findById(id);
        return paymentModelAssembler.toModel(payment);
    }

    @GetMapping("/nr")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Payment> paymentByNr(@RequestParam(value = "pn") String nr) {
        Payment payment = this.paymentService.findByPayNr(nr);
        log.info("Payment found: {}", payment);
        return paymentModelAssembler.toModel(payment);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.paymentService.delete(id);

        log.info("Payment deleted: {}", id);
        return new ResponseEntity<>("Payment entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/add")
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

    @PutMapping("/update/{id}")
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

}
