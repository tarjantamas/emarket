package com.ftn.market.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.market.constants.ErrorCodes;
import com.ftn.market.entity.Company;
import com.ftn.market.entity.Subscription;
import com.ftn.market.entity.User;
import com.ftn.market.exception.BadRequestException;
import com.ftn.market.repository.SubscriptionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

  private final CompanyService companyService;
  private final SubscriptionRepository subscriptionRepository;

  @Transactional
  public void subscribeToCompany(final User user, final long companyId) {
    final Company company = companyService.findById(companyId, ErrorCodes.COMPANY_NOT_FOUND_5);

    final Optional<Subscription> oSubscription = subscriptionRepository.findByUserAndCompany(user, company);
    if (oSubscription.isPresent()) {
      throw new BadRequestException(ErrorCodes.SUBSCRIPTION_COMPANY_ALREADY_EXISTS);
    }

    final Subscription subscription = new Subscription();
    subscription.setUser(user);
    subscription.setCompany(company);

    subscriptionRepository.save(subscription);
    log.info("{} successfully subscribed to {}", user, company);
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<Subscription> findCompanySubscriptions(final User user) {
    return subscriptionRepository.findByUserAndCompanyIsNotNull(user);
  }

  @Transactional
  public void unsubscribeFromCompany(final User user, final long companyId) {
    final Company company = companyService.findById(companyId, ErrorCodes.COMPANY_NOT_FOUND_6);

    final Optional<Subscription> oSubscription = subscriptionRepository.findByUserAndCompany(user, company);
    if (!oSubscription.isPresent()) {
      throw new BadRequestException(ErrorCodes.COMPANY_NOT_SUBSCRIBED);
    }

    subscriptionRepository.delete(oSubscription.get());
    log.info("{} successfully removed {} from subscriptions", user, company);
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<Subscription> findSubscriptionsForCompany(final Company company) {
    return subscriptionRepository.findByCompanyAndUserIsNotNull(company);
  }
}
