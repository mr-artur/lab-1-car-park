package ua.kpi.fict.carpark.config;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ua.kpi.fict.carpark.config.util.BusParser;
import ua.kpi.fict.carpark.config.util.FileReader;
import ua.kpi.fict.carpark.config.util.RouteParser;
import ua.kpi.fict.carpark.config.util.UserParser;
import ua.kpi.fict.carpark.entity.Bus;
import ua.kpi.fict.carpark.entity.Driver;
import ua.kpi.fict.carpark.entity.Route;
import ua.kpi.fict.carpark.entity.enums.Role;
import ua.kpi.fict.carpark.entity.User;
import ua.kpi.fict.carpark.repository.BusRepository;
import ua.kpi.fict.carpark.repository.DriverRepository;
import ua.kpi.fict.carpark.repository.RouteRepository;
import ua.kpi.fict.carpark.repository.UserRepository;

@RequiredArgsConstructor
@Component
public class DataWriter {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;

    private final UserParser userParser;
    private final RouteParser routeParser;
    private final BusParser busParser;
    private final FileReader fileReader;

    @EventListener(ApplicationReadyEvent.class)
    public void appReady() {
        addSuperAdminsIfNecessary();
        addAdminsIfNecessary();
        addDriversIfNecessary();
        addRoutesIfNecessary();
        addBusesIfNecessary();
        addDeactivatedUsersIfNecessary();
    }

    private void addSuperAdminsIfNecessary() {
        if (userRepository.findByRole(Role.ROLE_SUPERADMIN).isEmpty()) {
            addSuperAdmins();
        }
    }

    private void addAdminsIfNecessary() {
        if (userRepository.findByRole(Role.ROLE_ADMIN).isEmpty()) {
            addAdmins();
        }
    }

    private void addDriversIfNecessary() {
        if (userRepository.findByRole(Role.ROLE_DRIVER).isEmpty()) {
            addDrivers();
        }
    }

    private void addRoutesIfNecessary() {
        if (routeRepository.findAll().isEmpty()) {
            addRoutes();
        }
    }

    private void addBusesIfNecessary() {
        if (busRepository.findAll().isEmpty()) {
            addBuses();
        }
    }

    private void addDeactivatedUsersIfNecessary() {
        if (userRepository.findByEnabledIsFalse().isEmpty()) {
            addDeactivatedUsers();
        }
    }

    private void addSuperAdmins() {
        addUsersFromFile("db-data/superAdmins.csv");
    }

    private List<User> addUsersFromFile(String fileName) {
        List<User> users = fileReader.readWithoutHeader(fileName).stream()
            .map(userParser::parseToActivated)
            .collect(Collectors.toList());
        userRepository.saveAll(users);
        return users;
    }

    private void addAdmins() {
        addUsersFromFile("db-data/admins.csv");
    }

    private void addDrivers() {
        List<User> users = addUsersFromFile("db-data/drivers.csv");
        addDriversForUsers(users);
    }

    private void addDriversForUsers(List<User> users) {
        driverRepository.saveAll(users.stream().map(this::createDriver).collect(Collectors.toList()));
    }

    private Driver createDriver(User user) {
        return Driver.builder().user(user).build();
    }

    private void addRoutes() {
        List<Route> routes = fileReader.readWithoutHeader("db-data/routes.csv").stream()
            .map(routeParser::parse)
            .collect(Collectors.toList());
        routeRepository.saveAll(routes);
    }

    private void addBuses() {
        List<Bus> buses = fileReader.readWithoutHeader("db-data/buses.csv").stream()
            .map(busParser::parse)
            .collect(Collectors.toList());
        busRepository.saveAll(buses);
    }

    private void addDeactivatedUsers() {
        List<User> users = fileReader.readWithoutHeader("db-data/deactivated.csv").stream()
            .map(userParser::parseToDeactivatedWithRandomUsername)
            .collect(Collectors.toList());
        userRepository.saveAll(users);
    }
}
