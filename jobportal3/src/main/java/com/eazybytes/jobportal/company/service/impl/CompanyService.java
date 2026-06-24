package com.eazybytes.jobportal.company.service.impl;

import com.eazybytes.jobportal.company.web.dto.CompanyDTO;
import com.eazybytes.jobportal.company.web.dto.mapper.Mapper;
import com.eazybytes.jobportal.entity.Company;
import com.eazybytes.jobportal.company.repository.CompanyRepository;
import com.eazybytes.jobportal.company.service.ICompanyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService implements ICompanyService {


    private final CompanyRepository companyRepository;

    public CompanyService( CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List <CompanyDTO> getAllCompanies() {

        List<Company> companyList =companyRepository.findAll();

       return companyList
               .stream()
               .map(Mapper::transFromToDto)
               .collect(Collectors.toList());
    }
//
//    private CompanyDTO transformToDto(Company company) {
//        return new CompanyDTO (company.getId(), company.getName(), company.getLogo(),
//                company.getIndustry(), company.getSize(), company.getRating(),
//                company.getLocations(), company.getFounded(), company.getDescription(),
//                company.getEmployees(), company.getWebsite(), company.getCreatedAt());
//    }
}
