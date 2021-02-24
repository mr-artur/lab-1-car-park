package ua.kpi.fict.carpark.config.util;

import java.util.Random;
import org.springframework.stereotype.Component;
import ua.kpi.fict.carpark.entity.User;

@Component
public class UsernameGenerator {

    private final Random random = new Random();

    public String generate(User user) {
        return getNamePart(user) + random.nextInt(100000);
    }

    private String getNamePart(User user) {
        return String.format("%s.%s", truncate(user.getFirstName()), truncate(user.getLastName())).toLowerCase();
    }

    private String truncate(String name) {
        return name.substring(0, 3);
    }
}
