package com.ftn.market.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ftn.market.dto.image.ImageResponse;
import com.ftn.market.entity.Image;
import com.ftn.market.mapper.ImageMapper;
import com.ftn.market.service.ImageService;
import com.ftn.market.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImagesController {

  private final ImageMapper imageMapper;
  private final ImageService imageService;
  private final UserService userService;

  @PostMapping(path = "/company/{companyId}", produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ImageResponse> uploadCompanyImage(@RequestParam("file") final MultipartFile file,
      @PathVariable final long companyId) {
    final Image image = imageService.uploadCompanyImage(userService.getActiveUserOrFail(), file, companyId);

    return ResponseEntity.ok(imageMapper.mapToFullResponse(image));
  }

  @PostMapping(path = "/shop/{shopId}", produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ImageResponse> uploadShopImage(@RequestParam("file") final MultipartFile file,
      @PathVariable final long shopId) {
    final Image image = imageService.uploadShopImage(userService.getActiveUserOrFail(), file, shopId);

    return ResponseEntity.ok(imageMapper.mapToFullResponse(image));
  }

  @PostMapping(path = "/product/{productId}", produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ImageResponse> uploadProductImage(@RequestParam("file") final MultipartFile file,
      @PathVariable final long productId) {
    final Image image = imageService.uploadProductImage(userService.getActiveUserOrFail(), file, productId);

    return ResponseEntity.ok(imageMapper.mapToFullResponse(image));
  }

  @PostMapping(value = "/my", produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ImageResponse> uploadProfileImage(@RequestParam("file") final MultipartFile file) {
    final Image image = imageService.uploadProfileImage(userService.getActiveUserOrFail(), file);

    return ResponseEntity.ok(imageMapper.mapToFullResponse(image));
  }

  @GetMapping(path = "/company/{companyId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ImageResponse>> findByCompanyId(@PathVariable final long companyId) {
    final List<Image> images = imageService.findByCompanyId(companyId);

    return ResponseEntity.ok(imageMapper.mapToFullListResponse(images));
  }

  @GetMapping(path = "/shop/{shopId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ImageResponse>> findByShopId(@PathVariable final long shopId) {
    final List<Image> images = imageService.findByShopId(shopId);

    return ResponseEntity.ok(imageMapper.mapToFullListResponse(images));
  }

  @GetMapping(path = "/product/{productId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ImageResponse>> findByProductId(@PathVariable final long productId) {
    final List<Image> images = imageService.findByProductId(productId);

    return ResponseEntity.ok(imageMapper.mapToFullListResponse(images));
  }
}
