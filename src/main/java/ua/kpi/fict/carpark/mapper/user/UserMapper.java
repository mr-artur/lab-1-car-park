package ua.kpi.fict.carpark.mapper.user;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ua.kpi.fict.carpark.dto.request.CreateUserDto;
import ua.kpi.fict.carpark.dto.security.UserDetailsDto;
import ua.kpi.fict.carpark.dto.response.UserDto;
import ua.kpi.fict.carpark.entity.User;

@Named("UserMapper")
@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(CreateUserDto createUserDto);

    default UserDetailsDto toUserDetailsDto(User user) {
        UserDetailsDto dto = toUserDetailsDtoWithoutAuthorities(user);
        dto.setAuthorities(Set.of(user.getRole()));
        return dto;
    }

    @Mapping(target = "authorities", ignore = true)
    UserDetailsDto toUserDetailsDtoWithoutAuthorities(User user);

    @Named("toUserDto")
    UserDto toUserDto(User user);

    @Named("toUserDtoWithNativeData")
    @Mapping(source = "firstNameNative", target = "firstName")
    @Mapping(source = "lastNameNative", target = "lastName")
    UserDto toUserDtoWithNativeData(User user);
}
