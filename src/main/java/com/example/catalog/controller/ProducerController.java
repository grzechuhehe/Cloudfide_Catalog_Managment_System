package com.example.catalog.controller;

import com.example.catalog.dto.ProducerCreateRequest;
import com.example.catalog.dto.ProducerDto;
import com.example.catalog.service.ProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/producers")
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService producerService;

    @GetMapping
    public List<ProducerDto> getAllProducers() {
        return producerService.getAllProducers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProducerDto createProducer(@Valid @RequestBody ProducerCreateRequest request) {
        return producerService.createProducer(request);
    }
}
