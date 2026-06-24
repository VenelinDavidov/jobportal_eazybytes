package com.eazybytes.jobportal.company.service;

import com.eazybytes.jobportal.company.web.dto.CompanyDTO;


import java.util.List;

public interface ICompanyService {

    List <CompanyDTO> getAllCompanies();
}
