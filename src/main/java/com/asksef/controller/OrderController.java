package com.asksef.controller;

import com.asksef.assembler.InvoiceModelAssemblerSupport;
import com.asksef.assembler.ItemModelAssemblerSupport;
import com.asksef.assembler.OrderModelAssemblerSupport;
import com.asksef.assembler.StaffModelAssemblerSupport;
import com.asksef.entity.core.Invoice;
import com.asksef.entity.core.Item;
import com.asksef.entity.core.Order;
import com.asksef.entity.core.Staff;
import com.asksef.entity.model.InvoiceModel;
import com.asksef.entity.model.ItemModel;
import com.asksef.entity.model.OrderModel;
import com.asksef.entity.model.StaffModel;
import com.asksef.entity.repository.OrderRepository;
import com.asksef.entity.service_impl.OrderService;
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
@RequestMapping(value = "/api/order")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService saleService;
    private final OrderRepository orderRepository;
    private final OrderModelAssemblerSupport orderModelAssemblerSupport;

    public OrderController(OrderService saleService, OrderRepository orderRepository, OrderModelAssemblerSupport orderModelAssemblerSupport) {
        this.saleService = saleService;
        this.orderRepository = orderRepository;
        this.orderModelAssemblerSupport = orderModelAssemblerSupport;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<OrderModel>> all() {
        List<Order> entityList = this.saleService.findAll().stream().toList();
        return new ResponseEntity<>(this.orderModelAssemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    public ResponseEntity<OrderModel> one(@PathVariable Long id) {
        return this.orderRepository.findById(id)
                .map(orderModelAssemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<OrderModel> findBySaleNr(@RequestParam(name = "nr") String nr) {
        Order order = this.saleService.findBySaleNr(nr);
        log.info("Found order with nr: {}", nr);
        return new ResponseEntity<>(orderModelAssemblerSupport.toModel(order), HttpStatus.OK);
    }

    @PostMapping(value = "/add", produces = "application/hal+json")
    public ResponseEntity<OrderModel> add(@RequestBody OrderModel orderModel) {
        Order savedOrder = this.saleService.save(orderModel);
        OrderModel entityModel = this.orderModelAssemblerSupport.toModel(savedOrder);
//
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Order newOrder) {
        Order updateOrder = this.saleService.update(id, newOrder);
        OrderModel entityModel = this.orderModelAssemblerSupport.toModel(updateOrder);
        //
        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).one(id))
                        .toUri()).body(entityModel);
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.saleService.delete(id);
        log.info("Deleted Order with id {}", id);
        return new ResponseEntity<>("Order entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}/invoices", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<InvoiceModel>> findOrderInvoices(@PathVariable("id") Long id) {
        @NonNull List<Invoice> invoices = this.saleService.findOrderInvoices(id);
        //  build  model
        CollectionModel<InvoiceModel> invoiceModels = new InvoiceModelAssemblerSupport().toCollectionModel(invoices);
        //        invoiceModels.add(linkTo(methodOn(OrderController.class).findOrderInvoices(id)).withRel("Order Invoice"));
//        invoiceModel.add(linkTo(methodOn(OrderController.class).one(id)).withRel("Order"));
        return new ResponseEntity<>(invoiceModels, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/item", produces = "application/hal+json")
    public ResponseEntity<ItemModel> findOrderItem(@PathVariable("id") Long id) {
        @NonNull Item item = this.saleService.findOrderItem(id);
        //  build  model
        @NonNull ItemModel itemModel = new ItemModelAssemblerSupport().toModel(item);
        itemModel.add(linkTo(methodOn(OrderController.class).findOrderItem(id)).withRel("Order Invoice"));
        itemModel.add(linkTo(methodOn(OrderController.class).one(id)).withRel("Order"));
        return new ResponseEntity<>(itemModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/staff", produces = "application/hal+json")
    public ResponseEntity<StaffModel> findOrderStaff(@PathVariable("id") Long id) {
        Staff staff = this.saleService.findOrderStaff(id);
        //  build  model
        @NonNull StaffModel staffModel = new StaffModelAssemblerSupport().toModel(staff);
        staffModel.add(linkTo(methodOn(OrderController.class).findOrderStaff(id)).withRel("Order Staff"));
        staffModel.add(linkTo(methodOn(OrderController.class).one(id)).withRel("Order"));
        return new ResponseEntity<>(staffModel, HttpStatus.OK);
    }
}
