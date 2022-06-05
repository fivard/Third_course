package lab_2.service.mapper;

import lab_2.dto.user.UserReadDto;
import lab_2.dto.user.UserWriteDto;
import lab_2.persistence.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapper.class)
public interface UserMapper extends CommonMapper<UserReadDto, UserWriteDto, User> {
}
