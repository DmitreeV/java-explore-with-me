package ru.practicum.ewm_main.user.service;

import ru.practicum.ewm_main.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    List<UserDto> getAllUsers(List<Long> userIds, int from, int size);

    void deleteUser(Long userId);
}
