package com.ftn.market.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.market.dto.company.CompanyResponse;
import com.ftn.market.dto.company.RegisterCompany;
import com.ftn.market.entity.Company;
import com.ftn.market.mapper.CompanyMapper;
import com.ftn.market.service.CompanyService;
import com.ftn.market.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompaniesController {

  private final CompanyMapper companyMapper;
  private final CompanyService companyService;
  private final UserService userService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<CompanyResponse> create(@RequestBody @Valid final RegisterCompany registerCompany) {
    final Company company = companyService.create(userService.getActiveUserOrFail(), registerCompany);

    return new ResponseEntity<>(companyMapper.mapToFullResponse(company), HttpStatus.CREATED);
  }

  @GetMapping(value = "/my", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CompanyResponse>> findMyCompanies() {
    final List<Company> companies = companyService.findMyCompanies(userService.getActiveUserOrFail());

    return ResponseEntity.ok(companyMapper.mapToFullListResponse(companies));
  }
}
