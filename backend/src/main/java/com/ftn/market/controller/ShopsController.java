package com.ftn.market.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.market.dto.shop.RegisterShop;
import com.ftn.market.dto.shop.ShopResponse;
import com.ftn.market.entity.Shop;
import com.ftn.market.mapper.ShopMapper;
import com.ftn.market.service.ShopService;
import com.ftn.market.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shops")
public class ShopsController {

  private final ShopMapper shopMapper;
  private final ShopService shopService;
  private final UserService userService;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ShopResponse> create(@RequestBody @Valid final RegisterShop registerShop) {
    final Shop shop = shopService.create(userService.getActiveUserOrFail(), registerShop);

    return new ResponseEntity<>(shopMapper.mapToFullResponse(shop), HttpStatus.CREATED);
  }

  @GetMapping(value = "/company/{companyId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ShopResponse>> findByCompanyId(@PathVariable final long companyId) {
    return ResponseEntity.ok(shopMapper.mapToFullListResponse(shopService.findByCompanyId(companyId)));
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ShopResponse>> findAll() {
    return ResponseEntity.ok(shopMapper.mapToFullListResponse(shopService.findAll()));
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE, params = { "ids" })
  public ResponseEntity<List<ShopResponse>> findByIds(
      @RequestParam(value = "ids", defaultValue = "") final String shopIds) {
    return ResponseEntity.ok(shopMapper.mapToFullListResponse(shopService.findByIds(shopIds)));
  }
}
