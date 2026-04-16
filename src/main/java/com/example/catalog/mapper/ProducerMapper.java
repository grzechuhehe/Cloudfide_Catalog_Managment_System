package com.example.catalog.mapper;

import com.example.catalog.dto.ProducerCreateRequest;
import com.example.catalog.dto.ProducerDto;
import com.example.catalog.model.Producer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProducerMapper {

    ProducerDto toDto(Producer producer);

    @Mapping(target = "id", ignore = true)
    Producer toEntity(ProducerCreateRequest request);
}
