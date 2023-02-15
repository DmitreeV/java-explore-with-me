package ru.practicum.ewm_main.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
}
