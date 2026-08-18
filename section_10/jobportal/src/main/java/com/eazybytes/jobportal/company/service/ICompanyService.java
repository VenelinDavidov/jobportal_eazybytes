package com.eazybytes.jobportal.company.service;

import com.eazybytes.jobportal.dto.CompanyDto;
import com.eazybytes.jobportal.entity.Company;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface ICompanyService {

    List<CompanyDto> getAllCompanies();

    boolean createCompany(CompanyDto companyDto);

    List<CompanyDto> getALLCompaniesForAdmin();


    boolean updateCompanyDetails( Long id, CompanyDto companyDto);

    void deleteCompanyById(Long aLong);
}
