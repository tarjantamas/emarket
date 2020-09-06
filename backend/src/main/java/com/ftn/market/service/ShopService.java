package com.ftn.market.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.market.constants.ErrorCodes;
import com.ftn.market.dto.shop.RegisterShop;
import com.ftn.market.dto.shop.UpdateShop;
import com.ftn.market.entity.Company;
import com.ftn.market.entity.Product;
import com.ftn.market.entity.Shop;
import com.ftn.market.entity.User;
import com.ftn.market.exception.BadRequestException;
import com.ftn.market.exception.NotFoundException;
import com.ftn.market.mapper.ShopMapper;
import com.ftn.market.repository.ShopRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShopService {

  private final ShopMapper shopMapper;
  private final CompanyService companyService;
  private final ProductService productService;
  private final ShopRepository shopRepository;

  public ShopService(final ShopMapper shopMapper, final CompanyService companyService,
      @Lazy final ProductService productService, final ShopRepository shopRepository) {
    this.shopMapper = shopMapper;
    this.companyService = companyService;
    this.productService = productService;
    this.shopRepository = shopRepository;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Shop create(final User user, final RegisterShop registerShop) {
    final Company company = companyService.findById(registerShop.getCompanyId(), ErrorCodes.COMPANY_NOT_FOUND_1);
    if (!company.getUser().equals(user)) {
      throw new BadRequestException(ErrorCodes.SHOP_CREATE_REQUESTER_NOT_COMPANY_OWNER);
    }

    Shop shop = shopMapper.mapToDBO(registerShop);
    shop.setCompany(company);

    shop = shopRepository.save(shop);

    log.info("{} successfully created {}", user, shop);
    return shop;
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public Shop findById(final long id, final String errorCode) {
    final Optional<Shop> oShop = shopRepository.findById(id);

    if (!oShop.isPresent()) {
      log.info("Shop with id '{}' doesn't exist.", id);
      throw new NotFoundException(errorCode);
    }
    return oShop.get();
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<Shop> findByCompanyId(final long companyId) {
    final Company company = companyService.findById(companyId, ErrorCodes.COMPANY_NOT_FOUND_4);

    return shopRepository.findByCompany(company);
  }

  @Transactional
  public Shop updateById(final User user, final long id, final UpdateShop updateShop) {
    Shop existingShop = findById(id, ErrorCodes.SHOP_NOT_FOUND_5);
    if (!existingShop.getCompany().getUser().equals(user)) {
      throw new BadRequestException(ErrorCodes.SHOP_UPDATE_REQUESTER_NOT_COMPANY_OWNER);
    }

    shopMapper.mapToDBO(updateShop, existingShop);

    if (updateShop.getProductIds() != null) {
      final List<Product> products = productService.findByIds(updateShop.getProductIds());
      if (products.size() != updateShop.getProductIds().size()) {
        throw new NotFoundException(ErrorCodes.PRODUCT_NOT_FOUND_2);
      }
      existingShop.setProducts(products);
    }

    existingShop = shopRepository.save(existingShop);
    log.info("{} successfully updated {}", user, existingShop);

    return existingShop;
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<Shop> findAll() {
    return shopRepository.findAll();
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<Shop> findByIds(final String shopIds) {
    if (StringUtils.isEmpty(shopIds)) {
      return Collections.emptyList();
    }

    final Set<Long> ids =
        Stream.of(shopIds.split(",")).map(String::trim).map(Long::parseLong).collect(Collectors.toSet());

    final List<Shop> shops = shopRepository.findAllById(ids);

    if (ids.size() != shops.size()) {
      throw new NotFoundException(ErrorCodes.SHOP_NOT_FOUND_8);
    }

    return shops;
  }

  @Transactional
  public void removeById(final User user, final long id) {
    final Shop existingShop = findById(id, ErrorCodes.SHOP_NOT_FOUND_9);

    if (!existingShop.getCompany().getUser().equals(user)) {
      throw new BadRequestException(ErrorCodes.SHOP_DELETE_REQUESTER_NOT_COMPANY_OWNER);
    }

    shopRepository.delete(existingShop);
    log.info("{} successfully removed {}", user, existingShop);
  }
}
