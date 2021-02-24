package ua.kpi.fict.carpark.util.factories;

import org.springframework.data.domain.Page;

import ua.kpi.fict.carpark.dto.response.DriverDto;
import ua.kpi.fict.carpark.entity.Bus;
import ua.kpi.fict.carpark.entity.Driver;
import ua.kpi.fict.carpark.entity.User;

public class DriverFactory {

    public static Driver driverInstance() {
        return Driver.builder().user(new User()).build();
    }

    public static Driver driverInstance(User user) {
        return Driver.builder().user(user).build();
    }

    public static Page<Driver> driverPageInstance() {
        return Page.empty();
    }

    public static Page<DriverDto> driverDtosPageInstance() {
        return Page.empty();
    }

    public static Driver driverWithBusInstance() {
        Driver driver = driverInstance();
        Bus bus = new Bus();
        driver.setBus(bus);
        bus.setDriver(driver);
        return driver;
    }

    public static DriverDto driverDtoInstance() {
        return DriverDto.builder()
            .user(UserFactory.userDtoInstance())
            .build();
    }
}
