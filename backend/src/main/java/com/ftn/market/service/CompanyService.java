package com.ftn.market.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.market.constants.ErrorCodes;
import com.ftn.market.dto.company.RegisterCompany;
import com.ftn.market.dto.company.UpdateCompany;
import com.ftn.market.entity.Company;
import com.ftn.market.entity.User;
import com.ftn.market.exception.BadRequestException;
import com.ftn.market.exception.NotFoundException;
import com.ftn.market.mapper.CompanyMapper;
import com.ftn.market.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyMapper companyMapper;
  private final CompanyRepository companyRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Company create(final User user, final RegisterCompany registerCompany) {
    Company company = companyMapper.mapToDBO(registerCompany);
    company.setUser(user);

    company = companyRepository.save(company);

    log.info("{} successfully created {}", user, company);
    return company;
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<Company> findMyCompanies(final User user) {
    return companyRepository.findByUser(user);
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public Company findById(final Long id, final String errorCode) {
    final Optional<Company> oCompany = companyRepository.findById(id);

    if (!oCompany.isPresent()) {
      log.info("Company with id '{}' doesn't exist.", id);
      throw new NotFoundException(errorCode);
    }
    return oCompany.get();
  }

  @Transactional
  public Company updateById(final User user, final long id, final UpdateCompany updateCompany) {
    Company existingCompany = findById(id, ErrorCodes.COMPANY_NOT_FOUND_8);
    if (!existingCompany.getUser().equals(user)) {
      throw new BadRequestException(ErrorCodes.COMPANY_UPDATE_REQUESTER_NOT_OWNER);
    }

    companyMapper.mapToDBO(updateCompany, existingCompany);

    existingCompany = companyRepository.save(existingCompany);
    log.info("{} successfully updated {}", user, existingCompany);

    return existingCompany;
  }
}
