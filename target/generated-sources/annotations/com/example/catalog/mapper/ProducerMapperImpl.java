package com.example.catalog.mapper;

import com.example.catalog.dto.ProducerCreateRequest;
import com.example.catalog.dto.ProducerDto;
import com.example.catalog.model.Producer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-16T20:56:40+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class ProducerMapperImpl implements ProducerMapper {

    @Override
    public ProducerDto toDto(Producer producer) {
        if ( producer == null ) {
            return null;
        }

        ProducerDto.ProducerDtoBuilder producerDto = ProducerDto.builder();

        producerDto.id( producer.getId() );
        producerDto.name( producer.getName() );

        return producerDto.build();
    }

    @Override
    public Producer toEntity(ProducerCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Producer.ProducerBuilder producer = Producer.builder();

        producer.name( request.getName() );

        return producer.build();
    }
}
