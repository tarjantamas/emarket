package com.tim32.emarket.customcomponents.company;

import com.tim32.emarket.apiclients.dto.Company;

public class CompanyListItem {

    private final Company company;

    public CompanyListItem(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return company.getName();
    }

    public Company getCompany() {
        return company;
    }
}
