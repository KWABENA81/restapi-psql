package com.asksef.entity.service_impl;

import com.asksef.entity.core.Invoice;
import com.asksef.entity.core.Item;
import com.asksef.entity.core.Order;
import com.asksef.entity.core.Staff;
import com.asksef.entity.model.OrderModel;
import com.asksef.entity.repository.OrderRepository;
import com.asksef.entity.service_interface.SaleServiceInterface;
import com.asksef.errors.CustomResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;


@Service
public class OrderService implements SaleServiceInterface {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Collection<Order> findAll() {
        log.info("Finding all sales...");
        return this.orderRepository.findAll();
    }

    @Override
    public Collection<Order> findAll(int pageNumber, int pageSize) {
        return this.orderRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public Order findById(Long id) {
        Order order = this.orderRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Order", "id", null, id)
        );
        log.info("Found Order with ID: {}", id);
        return order;
    }

    @Transactional
    @Override
    public Order save(Order order) {
        return this.orderRepository.save(order);
    }

    @Transactional
    public Order save(@Valid OrderModel orderModel) {
        Order order = Order.builder()
                .orderId(orderModel.getOrderId())
                .orderDate(orderModel.getOrderDate())
                .orderNr(orderModel.getOrderNr())
                .staff(orderModel.getStaff())
                .item(orderModel.getItem())
                .lastUpdate(orderModel.getLastUpdate())
                .build();
        return save(order);
    }

    @Override
    public Order update(Order order) {
        Long id = order.getOrderId();
        Optional<Order> optional = this.orderRepository.findById(id);
        if (optional.isPresent()) {
            Order updateOrder = optional.get();
            updateOrder.setOrderDate(order.getOrderDate());
            updateOrder.setOrderNr(order.getOrderNr());

            updateOrder.setStaff(order.getStaff());
            updateOrder.setItem(order.getItem());

            log.info("Updating Order {}", updateOrder.getOrderNr());
            return orderRepository.save(updateOrder);
        } else {
            throw new CustomResourceNotFoundException("Order", "id", null, id);
        }

    }

    public Order update(Long id, Order newOrder) {
        if (Objects.equals(newOrder.getOrderId(), id)) {
            return update(newOrder);
        } else
            throw new CustomResourceNotFoundException("Country", "id", null, id);
    }

    @Override
    public void delete(Order order) {
        delete(order.getOrderId());
    }

    public void delete(Long id) {
        Order order = this.orderRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Order", "id", null, id)
        );
        log.info("Deleting country id: {}", id);
        this.orderRepository.delete(order);
    }

    public void delete(String saleNr) {
        Order order = findBySaleNr(saleNr);
        if (order != null) {
            log.info("Deleting Order# {}", order);
            delete(order.getOrderId());
        }
    }

    @Override
    public Long count() {
        log.info("Counting Sales ...");
        return this.orderRepository.count();
    }

    public Order findBySaleNr(String nr) {
        return this.orderRepository.findBySaleNr(nr);
    }

    public Staff findStaffOfSale(Long id) {
        return orderRepository.findStaffOfSale(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Order", "id", null, id)
        );
    }

    public @NonNull Invoice findInvoiceOfSale(Long id) {
        return orderRepository.findInvoiceOfSale(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Order", "id", null, id)
        );
    }

    public @NonNull Item findItemOfSale(Long id) {
        return orderRepository.findItemOfSale(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Order", "id", null, id)
        );
    }
}
