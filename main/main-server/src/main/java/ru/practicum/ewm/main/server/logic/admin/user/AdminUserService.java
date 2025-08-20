package ru.practicum.ewm.main.server.logic.admin.user;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.models.ConflictException;
import ru.practicum.ewm.main.dto.user.CreateUserDto;
import ru.practicum.ewm.main.dto.user.UserDto;
import ru.practicum.ewm.main.server.dal.user.User;
import ru.practicum.ewm.main.server.dal.user.UserMapper;
import ru.practicum.ewm.main.server.dal.user.UserRepository;
import ru.practicum.ewm.main.server.logic.validation.ServiceUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserService {
    private final UserRepository userRepository;

    public List<UserDto> getUsers(@Nullable List<Integer> idList,
                                  Integer from,
                                  Integer size) {
        List<User> userList;

        if (idList == null) {
            userList = userRepository.findAll(PageRequest.of(from, size)).getContent();
        } else {
            userList = userRepository.findByIdIn(idList, PageRequest.of(from, size)).getContent();
        }

        return UserMapper.toDto(userList);
    }

    @Transactional
    public UserDto createUser(CreateUserDto userDto) {
        isEmailExist(userDto.getEmail());

        User user = UserMapper.fromDto(userDto);
        user = userRepository.save(user);

        return UserMapper.toDto(user);
    }

    @Transactional
    public void deleteUser(Integer userId) {
        ServiceUtils.isExist(userRepository, userId, "Пользователь с таким id не найден");

        userRepository.deleteById(userId);
    }

    public void isEmailExist(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new ConflictException("Пользователь с данным имейлом уже существует");
        }
    }
}
