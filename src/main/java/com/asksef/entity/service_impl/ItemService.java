package com.asksef.entity.service_impl;

import com.asksef.entity.core.Item;
import com.asksef.entity.model.ItemModel;
import com.asksef.entity.repository.ItemRepository;
import com.asksef.entity.service_interface.ItemServiceInterface;
import com.asksef.errors.CustomResourceExistsException;
import com.asksef.errors.CustomResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;


@Service
public class ItemService implements ItemServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(ItemService.class);
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Collection<Item> findAll() {
        log.info("Find all items");
        return this.itemRepository.findAll();
    }

    @Override
    public Collection<Item> findAll(int pageNumber, int pageSize) {
        return this.itemRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public Item findById(Long id) {
        Item item = this.itemRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Item", "id", null, id)
        );
        log.info("findById succeeded");
        return item;
    }

    @Transactional
    public Item save(@Valid ItemModel itemModel) {
        Item item = Item.builder()
                .id(itemModel.getItemId())
                .itemCode(itemModel.getItemCode())
                .itemCost(itemModel.getItemCost())
                .itemDesc(itemModel.getItemDesc())
                .itemName(itemModel.getItemName())
                .saleInfo(itemModel.getSaleInfo())
                .build();
        return save(item);
    }

    @Transactional
    @Override
    public Item save(Item item) {
        Optional<Item> optional = this.itemRepository.findById(item.getItemId());
        if (optional.isEmpty()) {
            return this.itemRepository.save(item);
        } else {
            throw new CustomResourceExistsException("Item", "id", null, item.getItemId());
        }
    }

    @Override
    public Item update(Item item) {
        Long id = item.getItemId();
        Optional<Item> optional = itemRepository.findById(id);
        if (optional.isPresent()) {
            Item updateItem = optional.get();
            updateItem.setItemName(item.getItemName());
            updateItem.setItemCode(item.getItemCode());
            updateItem.setItemDesc(item.getItemDesc());
            updateItem.setItemCost(item.getItemCost());
            updateItem.setSaleInfo(item.getSaleInfo());

            updateItem.setInventoryList(item.getInventoryList());
            log.info("Item updated");
            return this.itemRepository.save(updateItem);
        } else {
            throw new CustomResourceNotFoundException("Item", "id", null, id);
        }
    }

    public Item update(Item item, Long id) {
        if (Objects.equals(id, item.getItemId())) {
            return this.update(item);
        } else
            throw new CustomResourceNotFoundException("Item", "id", null, id);
    }

    public void delete(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Item", "id", null, id)
        );
        delete(item);
    }

    @Override
    public void delete(Item item) {
        itemRepository.delete(item);
    }

    @Override
    public Long count() {
        return this.itemRepository.count();
    }

    public Item findByCode(String code) {
        return itemRepository.findByCode(code);
    }

    public Collection<Item> findByDescLike(String desc) {
        return itemRepository.findByDescLike(desc);
    }

    public Collection<Item> findByNameLike(String name) {
        return itemRepository.findByNameLike(name);
    }
}
