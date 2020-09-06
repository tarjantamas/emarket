package com.ftn.market.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.market.constants.ErrorCodes;
import com.ftn.market.dto.company.CompanyResponse;
import com.ftn.market.dto.company.UpdateCompany;
import com.ftn.market.entity.Company;
import com.ftn.market.mapper.CompanyMapper;
import com.ftn.market.service.CompanyService;
import com.ftn.market.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/company")
public class CompanyController {

  private final CompanyMapper companyMapper;
  private final CompanyService companyService;
  private final UserService userService;

  @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<CompanyResponse> findById(@PathVariable final long id) {
    final Company company = companyService.findById(id, ErrorCodes.COMPANY_NOT_FOUND_2);

    return ResponseEntity.ok(companyMapper.mapToFullResponse(company));
  }

  @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<CompanyResponse> updateById(@PathVariable final long id,
      @RequestBody final UpdateCompany updateCompany) {
    final Company company = companyService.updateById(userService.getActiveUserOrFail(), id, updateCompany);

    return ResponseEntity.ok(companyMapper.mapToFullResponse(company));
  }
}
