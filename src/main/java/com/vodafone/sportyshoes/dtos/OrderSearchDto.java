package com.vodafone.sportyshoes.dtos;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class OrderSearchDto {
    @DateTimeFormat(pattern= "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern= "yyyy-MM-dd")
    private Date endDate;
    private Long categoryId;
}
