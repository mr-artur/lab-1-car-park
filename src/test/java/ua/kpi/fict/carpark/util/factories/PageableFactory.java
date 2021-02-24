package ua.kpi.fict.carpark.util.factories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageableFactory {

    public static Pageable pageableInstance() {
        return PageRequest.of(1, 1);
    }
}
