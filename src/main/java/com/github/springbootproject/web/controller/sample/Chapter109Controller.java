package com.github.springbootproject.web.controller.sample;

import com.github.springbootproject.service.ElectronicStoreItemService;
import com.github.springbootproject.web.dto.items.Item;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/sample")
@RequiredArgsConstructor
@Slf4j
public class Chapter109Controller {

    private final ElectronicStoreItemService electronicStoreItemService;

    @Operation(summary = "가성비 싼 것부터 검색")
    @GetMapping("/items-prices")
    public List<Item> findItemByPrices(HttpServletRequest httpServletRequest) {
        Integer maxPrice = Integer.valueOf(httpServletRequest.getParameter("max"));

        log.info("GET /sample/items-prices 요청이 들어왔습니다.");
        List<Item> items = electronicStoreItemService.findItemsOrderByPrices(maxPrice);

        log.info("GET /items-prices 응답: " + items);
        return items;
    }

    @Operation(summary = "단일 Item id로 삭제")
    @DeleteMapping("items/{id}")
    public void deleteItemByPathId(@PathVariable Integer id, HttpServletResponse httpServletResponse) throws IOException {
        log.info("DELETE /sample/items" + id + " 요청이 들어왔습니다.");
        electronicStoreItemService.deleteItemByPathId(id);

        String responseMessage = "Object with id " + id + " has been deleted";
        log.info("DELETE /sample/items" + id + " 응답: " + responseMessage);
        httpServletResponse.getOutputStream().println(responseMessage);
    }
}
