package com.nimbusdesk.identity.persistence.mapper;

import com.nimbusdesk.identity.persistence.dto.AuthResponse;
import com.nimbusdesk.identity.persistence.dto.RegisterRequest;
import com.nimbusdesk.identity.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    UserEntity toEntity(RegisterRequest request);

    @Mapping(target = "token", source = "token")
    @Mapping(target = "email", source = "entity.email")
    @Mapping(target = "role", source = "entity.role")
    AuthResponse toAuthResponse(UserEntity entity, String token);
}
