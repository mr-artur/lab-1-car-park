package ua.kpi.fict.carpark.config.util;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ua.kpi.fict.carpark.entity.User;
import ua.kpi.fict.carpark.entity.enums.Role;

@RequiredArgsConstructor
@Component
public class UserParser {

    private final UsernameGenerator usernameGenerator;
    private final PasswordEncoder passwordEncoder;

    public User parseToActivated(String line) {
        return activate(parseToUser(line));
    }

    public User parseToDeactivatedWithRandomUsername(String line) {
        User user = parseToUser(line);
        user.setUsername(usernameGenerator.generate(user));
        return user;
    }

    private User parseToUser(String line) {
        String[] fields = line.split(",");
        String firstName = fields[0];
        String firstNameNative = fields[1];
        String lastName = fields[2];
        String lastNameNative = fields[3];
        String username = fields[4];
        String password = fields[5];
        Role role = Role.valueOf(fields[6]);
        return createUser(firstName, firstNameNative, lastName, lastNameNative, username, password, role);
    }

    private User createUser(String firstName, String firstNameNative, String lastName, String lastNameNative,
                            String username, String password, Role role) {
        return User.builder()
            .firstName(firstName)
            .firstNameNative(firstNameNative)
            .lastName(lastName)
            .lastNameNative(lastNameNative)
            .username(username)
            .password(passwordEncoder.encode(password))
            .role(role)
            .build();
    }

    private User activate(User user) {
        return user.toBuilder()
            .accountNonExpired(true)
            .accountNonLocked(true)
            .credentialsNonExpired(true)
            .enabled(true)
            .build();
    }
}
