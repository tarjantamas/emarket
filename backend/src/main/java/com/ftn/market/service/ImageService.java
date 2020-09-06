package com.ftn.market.service;

import static java.util.UUID.randomUUID;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ftn.market.constants.ErrorCodes;
import com.ftn.market.entity.Company;
import com.ftn.market.entity.Image;
import com.ftn.market.entity.Product;
import com.ftn.market.entity.Shop;
import com.ftn.market.entity.User;
import com.ftn.market.exception.BadRequestException;
import com.ftn.market.exception.NotFoundException;
import com.ftn.market.repository.ImageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

  private final CompanyService companyService;
  private final ProductService productService;
  private final ShopService shopService;
  private final ImageRepository imageRepository;

  private Path imagesStoragePath;

  @PostConstruct
  public void init() {
    imagesStoragePath = Paths.get("images").toAbsolutePath().normalize();
    try {
      Files.createDirectories(imagesStoragePath);
    } catch (final IOException e) {
      throw new BadRequestException("Could not create image storage directory.");
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Image uploadCompanyImage(final User user, final MultipartFile file, final long companyId) {
    final Company company = companyService.findById(companyId, ErrorCodes.COMPANY_NOT_FOUND_9);
    verifyUserIsCompanyOwner(user, company, ErrorCodes.IMAGE_UPLOAD_NOT_COMPANY_OWNER_1);

    return saveImage(user, file, company, null, null, false);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Image uploadShopImage(final User user, final MultipartFile file, final long shopId) {
    final Shop shop = shopService.findById(shopId, ErrorCodes.SHOP_NOT_FOUND_6);
    verifyUserIsCompanyOwner(user, shop.getCompany(), ErrorCodes.IMAGE_UPLOAD_NOT_COMPANY_OWNER_2);

    return saveImage(user, file, null, shop, null, false);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Image uploadProductImage(final User user, final MultipartFile file, final long productId) {
    final Product product = productService.findById(productId, ErrorCodes.PRODUCT_NOT_FOUND_3);
    verifyUserIsCompanyOwner(user, product.getCompany(), ErrorCodes.IMAGE_UPLOAD_NOT_COMPANY_OWNER_3);

    return saveImage(user, file, null, null, product, false);
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public ResponseEntity<Resource> download(final Long id) {
    final Image image = findById(id, ErrorCodes.IMAGE_NOT_FOUND_1);

    return loadFileAsResource(image);
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<Image> findByCompanyId(final long companyId) {
    final Company company = companyService.findById(companyId, ErrorCodes.COMPANY_NOT_FOUND_10);

    return imageRepository.findByCompany(company);
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<Image> findByShopId(final long shopId) {
    final Shop shop = shopService.findById(shopId, ErrorCodes.SHOP_NOT_FOUND_7);

    return imageRepository.findByShop(shop);
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<Image> findByProductId(final long productId) {
    final Product product = productService.findById(productId, ErrorCodes.PRODUCT_NOT_FOUND_4);

    return imageRepository.findByProduct(product);
  }

  private ResponseEntity<Resource> loadFileAsResource(final Image image) {
    try {
      final Path filePath = imagesStoragePath.resolve(image.getFileNameWithExtension()).normalize();
      final Resource resource = new UrlResource(filePath.toUri());
      if (!resource.exists()) {
        throw new BadRequestException("Image with given filename is not stored on the server.");
      }

      return ResponseEntity.ok()
        .contentType(MediaType.TEXT_PLAIN)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            String.format("attachment; filename=\"%s\"", image.getFileNameWithExtension()))
        .body(resource);
    } catch (final MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  private Image findById(final Long id, final String errorCode) {
    final Optional<Image> oImage = imageRepository.findById(id);

    if (!oImage.isPresent()) {
      log.info("Image with id '{}' doesn't exist.", id);
      throw new NotFoundException(errorCode);
    }
    return oImage.get();
  }

  private Image saveImage(final User user, final MultipartFile file, final Company company, final Shop shop,
      final Product product, final boolean profileImage) {
    Image image = buildImage(company, shop, product, file, profileImage ? user : null);
    store(file, image);
    image = imageRepository.save(image);

    log.info("{} successfully uploaded {}", user, image);
    return image;
  }

  private Image buildImage(final Company company, final Shop shop, final Product product, final MultipartFile file,
      final User user) {
    final Image image = new Image();
    image.setCompany(company);
    image.setShop(shop);
    image.setProduct(product);
    image.setUser(user);
    image.setUuid(randomUUID().toString());
    image.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
    return image;
  }

  private void verifyUserIsCompanyOwner(final User user, final Company company, final String errorCode) {
    if (!company.getUser().equals(user)) {
      throw new BadRequestException(errorCode);
    }
  }

  private void store(final MultipartFile file, final Image image) {
    final Path targetLocation = imagesStoragePath.resolve(image.getFileNameWithExtension());

    try {
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Transactional
  public void removeById(final User user, final long id) {
    final Image image = findById(id, ErrorCodes.IMAGE_NOT_FOUND_2);

    if (image.isCompanyImage()) {
      if (!image.getCompany().getUser().equals(user)) {
        throw new BadRequestException(ErrorCodes.IMAGE_DELETE_REQUESTER_NOT_COMPANY_OWNER);
      }
    } else if (image.isShopImage()) {
      if (!image.getShop().getCompany().getUser().equals(user)) {
        throw new BadRequestException(ErrorCodes.IMAGE_DELETE_REQUESTER_NOT_COMPANY_OWNER);
      }
    } else if (image.isProductImage()) {
      if (!image.getProduct().getCompany().getUser().equals(user)) {
        throw new BadRequestException(ErrorCodes.IMAGE_DELETE_REQUESTER_NOT_COMPANY_OWNER);
      }
    } else {
      if (!image.getUser().equals(user)) {
        throw new BadRequestException(ErrorCodes.IMAGE_DELETE_REQUESTER_NOT_IMAGE_OWNER);
      }
    }

    imageRepository.delete(image);
    log.info("{} successfully removed {}", user, image);
  }

  @Transactional
  public Image uploadProfileImage(final User user, final MultipartFile file) {
    return saveImage(user, file, null, null, null, true);
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<Image> findByUser(final User user) {
    return imageRepository.findByUser(user);
  }
}
