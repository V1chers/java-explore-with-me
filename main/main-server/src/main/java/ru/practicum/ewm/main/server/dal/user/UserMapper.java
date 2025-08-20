package ru.practicum.ewm.main.server.dal.user;

import ru.practicum.ewm.main.dto.user.CreateUserDto;
import ru.practicum.ewm.main.dto.user.UserDto;

import java.util.List;

public class UserMapper {
    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public static List<UserDto> toDto(List<User> userList) {
        return userList.stream().map(UserMapper::toDto).toList();
    }

    public static User fromDto(CreateUserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        return user;
    }
}
