package ua.kpi.fict.carpark.mapper.user;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ua.kpi.fict.carpark.dto.request.CreateUserDto;
import ua.kpi.fict.carpark.dto.security.UserDetailsDto;
import ua.kpi.fict.carpark.dto.response.UserDto;
import ua.kpi.fict.carpark.entity.User;
import ua.kpi.fict.carpark.mapper.LocalizedMapper;

@RequiredArgsConstructor
@Component
public class LocalizedUserMapper extends LocalizedMapper<User, UserDto> {

    private final UserMapper mapper;

    public User toUser(CreateUserDto createUserDto) {
        return mapper.toUser(createUserDto);
    }

    public UserDetailsDto toUserDetailsDto(User user) {
        return mapper.toUserDetailsDto(user);
    }

    @Override
    protected UserDto toDtoWithDefaultData(User user) {
        return mapper.toUserDto(user);
    }

    @Override
    protected UserDto toDtoWithNativeData(User user) {
        return mapper.toUserDtoWithNativeData(user);
    }
}
