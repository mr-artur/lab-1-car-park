package ua.kpi.fict.carpark.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ua.kpi.fict.carpark.entity.enums.Role;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static ua.kpi.fict.carpark.TextConstants.REGEX_NAME;
import static ua.kpi.fict.carpark.TextConstants.REGEX_NATIVE_NAME;
import static ua.kpi.fict.carpark.TextConstants.REGEX_PASSWORD;
import static ua.kpi.fict.carpark.TextConstants.REGEX_USERNAME;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CreateUserDto {

    @NotNull
    @Pattern(regexp = REGEX_USERNAME)
    private String username;

    @NotNull
    @Pattern(regexp = REGEX_PASSWORD)
    private String password;

    @NotNull
    private Role role;

    @NotNull
    @Pattern(regexp = REGEX_NAME)
    private String firstName;

    @NotNull
    @Pattern(regexp = REGEX_NATIVE_NAME)
    private String firstNameNative;

    @NotNull
    @Pattern(regexp = REGEX_NAME)
    private String lastName;

    @NotNull
    @Pattern(regexp = REGEX_NATIVE_NAME)
    private String lastNameNative;
}
