package ua.kpi.fict.carpark.config.util;

import org.springframework.stereotype.Component;
import ua.kpi.fict.carpark.entity.Bus;

@Component
public class BusParser {

    public Bus parse(String line) {
        String[] fields = line.split(",");
        String model = fields[0];
        int capacity = Integer.parseInt(fields[1]);
        return createBus(model, capacity);
    }

    private Bus createBus(String model, int capacity) {
        return Bus.builder().model(model).capacity(capacity).build();
    }
}
