package com.eazybytes.jobportal.company.web.dto.mapper;

import com.eazybytes.jobportal.company.web.dto.CompanyDTO;
import com.eazybytes.jobportal.entity.Company;
import lombok.Builder;
import lombok.experimental.UtilityClass;
@Builder
@UtilityClass
public class Mapper {


    public static CompanyDTO transFromToDto(Company company) {

        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .logo(company.getLogo())
                .industry(company.getIndustry())
                .size(company.getSize())
                .rating(company.getRating())
                .locations(company.getLocations())
                .founded(company.getFounded())
                .description(company.getDescription())
                .employees(company.getEmployees())
                .website(company.getWebsite())
                .createdAt(company.getCreatedAt())
                .build();
    }

}
