package lab_2.service.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;


@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommonMapper<R, W, E> {

    R entityToDto(E entity);

    E dtoToEntity(W dto);
}
