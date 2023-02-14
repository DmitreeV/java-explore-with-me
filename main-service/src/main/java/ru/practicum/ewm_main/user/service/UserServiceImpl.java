package ru.practicum.ewm_main.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_main.error.exception.ConflictException;
import ru.practicum.ewm_main.user.dto.UserDto;
import ru.practicum.ewm_main.user.model.User;
import ru.practicum.ewm_main.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm_main.user.mapper.UserMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = toUser(userDto);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Пользователь уже существует.");
        }
        log.info("Пользователь сохранен.");
        return toUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers(List<Long> userIds, int from, int size) {
        if (userIds.isEmpty()) {
            return new ArrayList<>();
        }
        log.info("Получен список всех пользователей.");
        return toUsersDto(userRepository.findAllByIdIn(userIds, PageRequest.of(from / size, size)));
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Пользователь удален.");
        userRepository.deleteById(userId);
    }
}
