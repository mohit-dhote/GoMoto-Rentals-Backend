package com.project.CarRental.services.customer;

import com.project.CarRental.dto.BookACarDto;
import com.project.CarRental.dto.CarDto;
import com.project.CarRental.dto.CarDtoListDto;
import com.project.CarRental.dto.SearchCarDto;
import com.project.CarRental.entity.BookACar;
import com.project.CarRental.entity.Car;
import com.project.CarRental.entity.User;
import com.project.CarRental.enums.BookCarStatus;
import com.project.CarRental.repository.BookACarRepository;
import com.project.CarRental.repository.CarRepository;
import com.project.CarRental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CarRepository carRepository;

    private final UserRepository userRepository;

    private final BookACarRepository bookACarRepository;

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
    }

    @Override
    public boolean bookACar(Long carId, BookACarDto bookACarDto) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        Optional<User> optionalUser = userRepository.findById(bookACarDto.getUserId());

        if (optionalCar.isPresent() && optionalUser.isPresent()) {
            CarDto existingCar = getCarById(carId);
            BookACar newBookACar = new BookACar();

            newBookACar.setBookCarStatus(BookCarStatus.PENDING);
            newBookACar.setFromDate(bookACarDto.getFromDate());
            newBookACar.setToDate(bookACarDto.getToDate());

            long diffInMilliSeconds = bookACarDto.getToDate().getTime() - bookACarDto.getFromDate().getTime();
            long days = TimeUnit.MILLISECONDS.toDays(diffInMilliSeconds);
            newBookACar.setDays(days);

            newBookACar.setPrice(existingCar.getPrice() * days);

            newBookACar.setUser(optionalUser.get());
            newBookACar.setCar(optionalCar.get());

            bookACarRepository.save(newBookACar);

            return true;
        }
        return false;
    }



    @Override
    public CarDto getCarById(Long carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        return optionalCar.map(Car::getCarDto).orElse(null);
    }

    @Override
    public List<BookACarDto> getBookingsByUserId(Long userId) {
        return bookACarRepository.findAllByUserId(userId).stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
    }

    @Override
    public CarDtoListDto searchCar(SearchCarDto searchCarDto) {
        Car car = new Car();
        car.setBrand(searchCarDto.getBrand());
        car.setType(searchCarDto.getType());
        car.setTransmission(searchCarDto.getTransmission());
        car.setColor(searchCarDto.getColor());

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                .withMatcher("brand", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("type", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("transmission", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("color", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());


        Example<Car> carExample = Example.of(car, exampleMatcher);
        List<Car> carList = carRepository.findAll(carExample);

        CarDtoListDto carDtoListDto = new CarDtoListDto();
        carDtoListDto.setCarDtoList(carList.stream().map(Car::getCarDto).collect(Collectors.toList()));
        return carDtoListDto;
    }


}
