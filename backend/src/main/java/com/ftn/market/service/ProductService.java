package com.ftn.market.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.market.constants.ErrorCodes;
import com.ftn.market.dto.product.RegisterProduct;
import com.ftn.market.dto.product.UpdateProduct;
import com.ftn.market.entity.Company;
import com.ftn.market.entity.Product;
import com.ftn.market.entity.Setting;
import com.ftn.market.entity.Shop;
import com.ftn.market.entity.Subscription;
import com.ftn.market.entity.User;
import com.ftn.market.exception.BadRequestException;
import com.ftn.market.exception.NotFoundException;
import com.ftn.market.helper.GeometryHelper;
import com.ftn.market.mapper.ProductMapper;
import com.ftn.market.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductMapper productMapper;

  private final EmailService emailService;
  private final SettingService settingService;
  private final CompanyService companyService;
  private final SubscriptionService subscriptionService;
  private final ShopService shopService;

  private final ProductRepository productRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Product create(final User user, final RegisterProduct registerProduct) {
    final Company company = companyService.findById(registerProduct.getCompanyId(), ErrorCodes.COMPANY_NOT_FOUND_3);
    if (!company.getUser().equals(user)) {
      throw new BadRequestException(ErrorCodes.PRODUCT_CREATE_REQUESTER_NOT_COMPANY_OWNER);
    }

    Product product = productMapper.mapToDBO(registerProduct);
    product.setCompany(company);

    product = productRepository.save(product);

    log.info("{} successfully created {}", user, product);

    notifyUsersThatProductIsCreated(product);
    return product;
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public Product findById(final long id, final String errorCode) {
    final Optional<Product> oProduct = productRepository.findById(id);

    if (!oProduct.isPresent()) {
      log.info("Product with id '{}' doesn't exist.", id);
      throw new NotFoundException(errorCode);
    }
    return oProduct.get();
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<Product> findByShopId(final long shopId) {
    return shopService.findById(shopId, ErrorCodes.SHOP_NOT_FOUND_2).getProducts();
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<Product> findByCompanyId(final long companyId) {
    final Company company = companyService.findById(companyId, ErrorCodes.COMPANY_NOT_FOUND_7);

    return productRepository.findByCompany(company);
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<Product> findByIds(final List<Long> productIds) {
    return productRepository.findAllById(productIds);
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public Page<Product> search(final Double lat, final Double lng, final Double radius, final String term,
      final Pageable pageable) {
    if (lat != null && lng != null && radius != null) {

      final List<Shop> shopsInRange = shopService.findAll()
        .stream()
        .filter(shop -> GeometryHelper.distance(lat, lng, shop.getLatitude(), shop.getLongitude()) < radius)
        .collect(Collectors.toList());

      if (CollectionUtils.isEmpty(shopsInRange)) {
        return new PageImpl<>(Collections.emptyList());
      }

      final Set<Long> productIds = new HashSet<>();
      shopsInRange.forEach(
          shop -> productIds.addAll(shop.getProducts().stream().map(Product::getId).collect(Collectors.toSet())));

      if (CollectionUtils.isEmpty(productIds)) {
        return new PageImpl<>(Collections.emptyList());
      }

      return productRepository.findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndIdIn(term, term,
          productIds, pageable);
    } else {
      return productRepository.findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(term, term, pageable);
    }
  }

  @Transactional
  public void removeById(final User user, final long id) {
    final Product product = findById(id, ErrorCodes.PRODUCT_NOT_FOUND_5);

    if (!product.getCompany().getUser().equals(user)) {
      throw new BadRequestException(ErrorCodes.PRODUCT_DELETE_REQUESTER_NOT_COMPANY_OWNER);
    }

    productRepository.delete(product);
    log.info("{} successfully removed {}", user, product);

    notifyUsersThatProductIsRemoved(product);
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public Page<Product> searchForShop(final long shopId, final String term, final Pageable pageable) {
    final Shop shop = shopService.findById(shopId, ErrorCodes.SHOP_NOT_FOUND_10);

    return productRepository.findByShopAndSearchTerm(shop, term, pageable);
  }

  @Transactional
  public Product updateById(final User user, final long id, final UpdateProduct updateProduct) {
    Product existingProduct = findById(id, ErrorCodes.PRODUCT_NOT_FOUND_6);
    if (!existingProduct.getCompany().getUser().equals(user)) {
      throw new BadRequestException(ErrorCodes.PRODUCT_UPDATE_REQUESTER_NOT_COMPANY_OWNER);
    }

    productMapper.mapToDBO(updateProduct, existingProduct);

    existingProduct = productRepository.save(existingProduct);
    log.info("{} successfully updated {}", user, existingProduct);

    notifyUsersThatProductIsUpdated(existingProduct);

    return existingProduct;
  }

  private void notifyUsersThatProductIsCreated(final Product product) {
    notifyUsers(product, ErrorCodes.SETTING_NOT_FOUND_3, "Product has been added",
        String.format("Product '%s' [%s] with price '%.2f %s' has been added to company '%s'. Go check it out!",
            product.getName(), product.getDescription(), product.getPrice(), product.getUnit(),
            product.getCompany().getName()));
  }

  private void notifyUsersThatProductIsUpdated(final Product product) {
    notifyUsers(product, ErrorCodes.SETTING_NOT_FOUND_4, "Product has been updated",
        String.format("Product '%s' has new price: '%.2f %s' in company '%s'. Go check it out!", product.getName(),
            product.getPrice(), product.getUnit(), product.getCompany().getName()));
  }

  private void notifyUsersThatProductIsRemoved(final Product product) {
    notifyUsers(product, ErrorCodes.SETTING_NOT_FOUND_5, "Product has been removed", String
      .format("Product '%s' has been removed from company '%s'.", product.getName(), product.getCompany().getName()));
  }

  private void notifyUsers(final Product product, final String errorCode, final String subject, final String text) {
    final List<Subscription> subscriptionsForCompany =
        subscriptionService.findSubscriptionsForCompany(product.getCompany());

    for (final Subscription subscription : subscriptionsForCompany) {
      final User user = subscription.getUser();
      final Setting setting = settingService.findForUser(user, errorCode);
      if (BooleanUtils.isTrue(setting.getEmailsEnabled())) {
        emailService.sendMail(user.getEmail(), subject, text);
      }
    }
  }
}
