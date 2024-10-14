package com.project.CarRental.services.admin;

import com.project.CarRental.dto.CarDto;
import com.project.CarRental.entity.Car;
import com.project.CarRental.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Calendar;
import java.util.Date;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateCar_existingCar_updatesSuccessfully() throws IOException {
        // Arrange: Setup test data
        Long carId = 1L;
        CarDto carDto = new CarDto();
        carDto.setName("Updated Car");
        carDto.setBrand("Updated Brand");
        carDto.setColor("Red");
        carDto.setPrice((long) 20000.0);

        // Set the year using a Date object
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2023);
        carDto.setYear(calendar.getTime());  // Pass a Date object

        carDto.setType("SUV");

        Car existingCar = new Car();
        existingCar.setId(carId);
        existingCar.setName("Old Car");
        existingCar.setBrand("Old Brand");

        // Mock repository behavior
        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));

        // Act: Call the service method
        boolean result = adminService.updateCar(carId, carDto);

        // Assert: Verify the result and interactions
        assertTrue(result);
        assertEquals("Updated Car", existingCar.getName());
        assertEquals("Updated Brand", existingCar.getBrand());
        assertEquals("Red", existingCar.getColor());
        assertEquals(20000L, existingCar.getPrice());
        assertEquals("SUV", existingCar.getType());

        // Verify that the save method was called once
        verify(carRepository, times(1)).save(existingCar);
    }
}
