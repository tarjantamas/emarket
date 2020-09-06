package com.ftn.market.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.market.entity.Image;
import com.ftn.market.mapper.ImageMapper;
import com.ftn.market.service.ImageService;
import com.ftn.market.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageController {

  private final ImageMapper imageMapper;

  private final ImageService imageService;
  private final UserService userService;

  @GetMapping(value = "/{id}", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Resource> download(@PathVariable("id") final Long id) {
    return imageService.download(id);
  }

  @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> removeById(@PathVariable final long id) {
    imageService.removeById(userService.getActiveUserOrFail(), id);

    return ResponseEntity.noContent().build();
  }

  @GetMapping(path = "/company/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Resource> findFirstForCompany(@PathVariable final long id) {
    final List<Image> images = imageService.findByCompanyId(id);

    if (CollectionUtils.isEmpty(images)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return imageService.download(images.get(0).getId());
  }

  @GetMapping(path = "/shop/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Resource> findFirstForShop(@PathVariable final long id) {
    final List<Image> images = imageService.findByShopId(id);

    if (CollectionUtils.isEmpty(images)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return imageService.download(images.get(0).getId());
  }

  @GetMapping(path = "/product/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Resource> findFirstForProduct(@PathVariable final long id) {
    final List<Image> images = imageService.findByProductId(id);

    if (CollectionUtils.isEmpty(images)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return imageService.download(images.get(0).getId());
  }

  @GetMapping(path = "/my", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Resource> findFirstForUser() {
    final List<Image> images = imageService.findByUser(userService.getActiveUserOrFail());

    if (CollectionUtils.isEmpty(images)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return imageService.download(images.get(0).getId());
  }
}
