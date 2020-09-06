package com.ftn.market.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.market.constants.ErrorCodes;
import com.ftn.market.dto.shop.ShopResponse;
import com.ftn.market.dto.shop.UpdateShop;
import com.ftn.market.entity.Shop;
import com.ftn.market.mapper.ShopMapper;
import com.ftn.market.service.ShopService;
import com.ftn.market.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop")
public class ShopController {

  private final ShopMapper shopMapper;
  private final UserService userService;
  private final ShopService shopService;

  @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<ShopResponse> findById(@PathVariable final long id) {
    final Shop shop = shopService.findById(id, ErrorCodes.SHOP_NOT_FOUND_1);

    return ResponseEntity.ok(shopMapper.mapToFullResponse(shop));
  }

  @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<ShopResponse> updateById(@PathVariable final long id,
      @RequestBody final UpdateShop updateShop) {
    final Shop shop = shopService.updateById(userService.getActiveUserOrFail(), id, updateShop);

    return ResponseEntity.ok(shopMapper.mapToFullResponse(shop));
  }

  @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> removeById(@PathVariable final long id) {
    shopService.removeById(userService.getActiveUserOrFail(), id);

    return ResponseEntity.noContent().build();
  }
}
