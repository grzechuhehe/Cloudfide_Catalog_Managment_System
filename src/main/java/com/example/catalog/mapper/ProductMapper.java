package com.example.catalog.mapper;

import com.example.catalog.dto.ProductRequest;
import com.example.catalog.dto.ProductResponse;
import com.example.catalog.model.Product;
import com.example.catalog.model.ProductAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ProducerMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "attributes", source = "attributes", qualifiedByName = "mapAttributes")
    ProductResponse toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producer", ignore = true) // Will be set in service
    @Mapping(target = "attributes", ignore = true) // Will be mapped separately
    Product toEntity(ProductRequest request);

    @Named("mapAttributes")
    default Map<String, String> mapAttributes(List<ProductAttribute> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return Collections.emptyMap();
        }
        return attributes.stream()
                .collect(Collectors.toMap(
                        ProductAttribute::getAttributeName,
                        ProductAttribute::getAttributeValue
                ));
    }
}
