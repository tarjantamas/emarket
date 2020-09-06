package com.ftn.market.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.market.constants.ErrorCodes;
import com.ftn.market.dto.product.ProductResponse;
import com.ftn.market.dto.product.RegisterProduct;
import com.ftn.market.entity.Product;
import com.ftn.market.exception.BadRequestException;
import com.ftn.market.helper.PaginationHelper;
import com.ftn.market.mapper.ProductMapper;
import com.ftn.market.service.ProductService;
import com.ftn.market.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductsController {

  private final ProductMapper productMapper;
  private final ProductService productService;
  private final UserService userService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<ProductResponse> create(@RequestBody @Valid final RegisterProduct registerProduct) {
    final Product product = productService.create(userService.getActiveUserOrFail(), registerProduct);

    return new ResponseEntity<>(productMapper.mapToFullResponse(product), HttpStatus.CREATED);
  }

  @GetMapping(value = "/shop/{shopId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ProductResponse>> findByShopId(@PathVariable final long shopId) {
    return ResponseEntity.ok(productMapper.mapToFullListResponse(productService.findByShopId(shopId)));
  }

  @GetMapping(value = "/company/{companyId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ProductResponse>> findByCompanyId(@PathVariable final long companyId) {
    return ResponseEntity.ok(productMapper.mapToFullListResponse(productService.findByCompanyId(companyId)));
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ProductResponse>> search(@RequestParam(value = "lat", required = false) final Double lat,
      @RequestParam(value = "lng", required = false) final Double lng,
      @RequestParam(value = "rad", required = false) final Double radius,
      @RequestParam(name = "term", required = false, defaultValue = "") final String term, final Pageable pageable,
      final HttpServletResponse response) {
    if ((lat != null || lng != null || radius != null) && (lat == null || lng == null || radius == null)) {
      throw new BadRequestException(ErrorCodes.PRODUCT_SEARCH_PARAMS_INVALID);
    }

    final Page<Product> products = productService.search(lat, lng, radius, term, pageable);

    PaginationHelper.addPaginationHeaders(response, products);

    return ResponseEntity.ok(productMapper.mapToFullListResponse(products.getContent()));
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE, params = { "shop_id" })
  public ResponseEntity<List<ProductResponse>> searchForShop(@RequestParam(value = "shop_id") final long shopId,
      @RequestParam(name = "term", required = false, defaultValue = "") final String term, final Pageable pageable,
      final HttpServletResponse response) {
    final Page<Product> products = productService.searchForShop(shopId, term, pageable);

    PaginationHelper.addPaginationHeaders(response, products);

    return ResponseEntity.ok(productMapper.mapToFullListResponse(products.getContent()));
  }
}
