package com.project.CarRental.dto;

import com.project.CarRental.enums.BookCarStatus;
import lombok.Data;

import java.util.Date;

@Data
public class BookACarDto {

    private Long id;

    private Date fromDate;

    private Date toDate;

    private Long days;

    private Long price;

    private BookCarStatus bookCarStatus;

    private Long carId;

    private Long userId;

    private String username;

    private String email;

    private String brand;

    private String name;

    private String color;

    private String type;

    private String transmission;
}
