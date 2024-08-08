package com.chilly.main_svc.mapper;

import com.chilly.main_svc.dto.UserDto;
import com.chilly.main_svc.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoModelMapper extends BaseDtoMapper<User, UserDto> {

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected Class<UserDto> getDtoClass() {
        return UserDto.class;
    }
}
