package com.example.catalog.service;

import com.example.catalog.dto.ProducerCreateRequest;
import com.example.catalog.dto.ProducerDto;
import com.example.catalog.exception.ResourceNotFoundException;
import com.example.catalog.mapper.ProducerMapper;
import com.example.catalog.model.Producer;
import com.example.catalog.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ProducerRepository producerRepository;
    private final ProducerMapper producerMapper;

    @Transactional(readOnly = true)
    public List<ProducerDto> getAllProducers() {
        return producerRepository.findAll().stream()
                .map(producerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProducerDto createProducer(ProducerCreateRequest request) {
        Producer producer = producerMapper.toEntity(request);
        Producer savedProducer = producerRepository.save(producer);
        return producerMapper.toDto(savedProducer);
    }

    @Transactional(readOnly = true)
    public Producer getProducerEntity(Long id) {
        return producerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producer not found with id: " + id));
    }
}
