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
import com.ftn.market.dto.product.ProductResponse;
import com.ftn.market.dto.product.UpdateProduct;
import com.ftn.market.entity.Product;
import com.ftn.market.mapper.ProductMapper;
import com.ftn.market.service.ProductService;
import com.ftn.market.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

  private final ProductMapper productMapper;

  private final UserService userService;
  private final ProductService productService;

  @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<ProductResponse> findById(@PathVariable final long id) {
    final Product product = productService.findById(id, ErrorCodes.PRODUCT_NOT_FOUND_1);

    return ResponseEntity.ok(productMapper.mapToFullResponse(product));
  }

  @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> removeById(@PathVariable final long id) {
    productService.removeById(userService.getActiveUserOrFail(), id);

    return ResponseEntity.noContent().build();
  }

  @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<ProductResponse> updateById(@PathVariable final long id,
      @RequestBody final UpdateProduct updateProduct) {
    final Product product = productService.updateById(userService.getActiveUserOrFail(), id, updateProduct);

    return ResponseEntity.ok(productMapper.mapToFullResponse(product));
  }
}
