package com.github.springbootproject.web.controller;

import com.github.springbootproject.service.ElectronicStoreItemService;
import com.github.springbootproject.web.dto.items.BuyOrder;
import com.github.springbootproject.web.dto.items.Item;
import com.github.springbootproject.web.dto.items.ItemBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ElectronicStoreController {

    private final ElectronicStoreItemService electronicStoreItemService;

    @Operation(summary = "모든 Item을 검색하는 API")
    @GetMapping("/items")
    public List<Item> findAllItem() {
        log.info("GET /items 요청이 들어왔습니다.");
        List<Item> items = electronicStoreItemService.findAllItem();
        log.info("GET /items 응답: " + items);
        return items;
    }

    @Operation(summary = "모든 Item 등록")
    @PostMapping("/items")
    public String registerItem(@RequestBody ItemBody itemBody) {
        Integer id = electronicStoreItemService.saveItem(itemBody);
        return String.format("ID: %d", id);
    }

    @Operation(summary = "모든 Item id로 검색")
    @GetMapping("items/{id}")
    public Item findItemByPathId(
            @Parameter(name = "id", description = "item ID", example = "1", required = true)
            @PathVariable Integer id) {
        return electronicStoreItemService.findItemById(id);
    }

    @Operation(summary = "모든 Item id로 검색(쿼리문)")
    @GetMapping("/items-query")
    public Item findItemByQueryId(
            @Parameter(name = "id", description = "item ID", example = "1", required = true)
            @RequestParam("id") Integer id) {
        return electronicStoreItemService.findItemById(id);
    }

    @Operation(summary = "모든 Item ids로 검색(쿼리문)")
    @GetMapping("/items-queries")
    public List<Item> findItemByQueryIds(
            @Parameter(name = "ids", description = "item IDs", example = "[1, 2, 3]", required = true)
            @RequestParam("id") List<Integer> ids) {
        log.info("/items-queries 요청");
        List<Item> items = electronicStoreItemService.findItemsByIds(ids);
        log.info("/items-queries 응답:" + items);
        return items;
    }

    @Operation(summary = "모든 Item id로 삭제")
    @DeleteMapping("items/{id}")
    public String deleteItemByPathId(
            @Parameter(name = "id", description = "item ID", example = "1", required = true)
            @PathVariable Integer id) {
        electronicStoreItemService.deleteItemByPathId(id);
        return String.format("Object with id = %d has been deleted", id);
    }

    @Operation(summary = "모든 Item id로 수정")
    @PutMapping("/items/{id}")
    public Item updateItem(
            @Parameter(name = "id", description = "item ID", example = "1", required = true)
            @PathVariable Integer id,
            @RequestBody ItemBody itemBody) {
        return electronicStoreItemService.updateItem(id, itemBody);
    }

    @Operation(summary = "모든 Item 구매")
    @PostMapping("items/buy")
    public String buyItem(@RequestBody BuyOrder buyOrder) {
        Integer orderItemNums = electronicStoreItemService.buyItems(buyOrder);
        return String.format("요청하신 Item 중 %d개를 구매하였습니다.", orderItemNums);
    }

    @Operation(summary = "여러 Item types 검색 (쿼리문)")
    @GetMapping("/items-types")
    public List<Item> findItemByTypes(
            @Parameter(name="types", description = "item type", example = "[스마트폰, 노트북]", required = true)
            @RequestParam("type") List<String> types) {

        log.info("/items-types 요청 types: " + types);
        List<Item> items = electronicStoreItemService.findItemsByTypes(types);
        log.info("/items-types 응답: " + items);

        return items;
    }

    @Operation(summary = "단일 Item types 검색 (쿼리문)")
    @GetMapping("/items-prices")
    public List<Item> findItemByPrices(
            @Parameter(name="max", description = "max value", example = "100000", required = true)
            @RequestParam("max") Integer maxValue) {

        return electronicStoreItemService.findItemsOrderByPrices(maxValue);
    }

    @Operation(summary = "Pagination 지원")
    @GetMapping("/items-page")
    public Page<Item> findItemsPagination(Pageable pageable) {
        return electronicStoreItemService.findAllWithPageable(pageable);
    }

    @Operation(summary = "Pagination 지원2")
    @GetMapping("/items-types-page")
    public Page<Item> findItemsPagination(@RequestParam("type") List<String> types, Pageable pageable) {
        return electronicStoreItemService.findAllWithPageable(types, pageable);
    }
}
