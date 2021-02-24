package ua.kpi.fict.carpark.config.util;

import org.springframework.stereotype.Component;
import ua.kpi.fict.carpark.entity.Route;

@Component
public class RouteParser {

    public Route parse(String line) {
        int number = Integer.parseInt(line);
        return createRoute(number);
    }

    private Route createRoute(int number) {
        return Route.builder().number(number).build();
    }
}
