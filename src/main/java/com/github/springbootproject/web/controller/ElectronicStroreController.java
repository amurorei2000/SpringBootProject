package com.github.springbootproject.web.controller;

import com.github.springbootproject.service.ElectronicStoreItemService;
import com.github.springbootproject.web.dto.BuyOrder;
import com.github.springbootproject.web.dto.Item;
import com.github.springbootproject.web.dto.ItemBody;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ElectronicStroreController {

    private ElectronicStoreItemService electronicStoreItemService;

    public ElectronicStroreController(ElectronicStoreItemService electronicStoreItemService) {
        this.electronicStoreItemService = electronicStoreItemService;
    }

    @GetMapping("/items")
    public List<Item> findAllItem() {
        return electronicStoreItemService.findAllItem();
    }

    @PostMapping("/items")
    public String registerItem(@RequestBody ItemBody itemBody) {
        Integer id = electronicStoreItemService.saveItem(itemBody);
        return String.format("ID: %d", id);
    }

    @GetMapping("items/{id}")
    public Item findItemByPathId(@PathVariable Integer id) {
        return electronicStoreItemService.findItemById(id);
    }

    @GetMapping("/items-query")
    public Item findItemByQueryId(@RequestParam("id") Integer id) {
        return electronicStoreItemService.findItemById(id);
    }

    @GetMapping("/items-queries")
    public List<Item> findItemByQueryIds(@RequestParam("id") List<Integer> ids) {
        return electronicStoreItemService.findItemsByIds(ids);
    }

    @DeleteMapping("items/{id}")
    public String deleteItemByPathId(@PathVariable Integer id) {
        electronicStoreItemService.deleteItemByPathId(id);
        return String.format("Object with id = %d has been deleted", id);
    }

    @PutMapping("/items/{id}")
    public Item updateItem(@PathVariable Integer id, @RequestBody ItemBody itemBody) {
        return electronicStoreItemService.updateItem(id, itemBody);
    }

    @PostMapping("items/buy")
    public String buyItem(@RequestBody BuyOrder buyOrder) {
        Integer orderItemNums = electronicStoreItemService.buyItems(buyOrder);
        return String.format("요청하신 Item 중 %d개를 구매하였습니다.", orderItemNums);
    }
}
