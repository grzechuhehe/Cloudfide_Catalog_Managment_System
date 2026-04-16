package com.example.catalog.service;

import com.example.catalog.dto.ProductRequest;
import com.example.catalog.dto.ProductResponse;
import com.example.catalog.exception.ResourceNotFoundException;
import com.example.catalog.mapper.ProductMapper;
import com.example.catalog.model.Producer;
import com.example.catalog.model.Product;
import com.example.catalog.model.ProductAttribute;
import com.example.catalog.repository.ProductRepository;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProducerService producerService;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts(Long producerId, String search) {
        Specification<Product> spec = Specification.where(null);

        if (producerId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("producer").get("id"), producerId));
        }

        if (search != null && !search.trim().isEmpty()) {
            String likePattern = "%" + search.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), likePattern),
                    cb.like(cb.lower(root.get("description")), likePattern)
            ));
        }

        // To avoid N+1 we should ideally use entity graphs or custom query, but with Specifications
        // it requires custom fetching logic. For simplicity and standard requirements:
        if (producerId == null && (search == null || search.trim().isEmpty())) {
            return productRepository.findAllWithDetails().stream()
                    .map(productMapper::toDto)
                    .collect(Collectors.toList());
        }

        return productRepository.findAll(spec).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = getProductEntity(id);
        return productMapper.toDto(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Producer producer = producerService.getProducerEntity(request.getProducerId());

        Product product = productMapper.toEntity(request);
        product.setProducer(producer);
        
        applyAttributes(product, request.getAttributes());

        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = getProductEntity(id);

        if (!product.getProducer().getId().equals(request.getProducerId())) {
            Producer producer = producerService.getProducerEntity(request.getProducerId());
            product.setProducer(producer);
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        // Update attributes (clear and recreate for simplicity)
        product.getAttributes().clear();
        applyAttributes(product, request.getAttributes());

        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private Product getProductEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    private void applyAttributes(Product product, Map<String, String> attributes) {
        if (attributes != null && !attributes.isEmpty()) {
            attributes.forEach((key, value) -> {
                ProductAttribute attr = ProductAttribute.builder()
                        .attributeName(key)
                        .attributeValue(value)
                        .build();
                product.addAttribute(attr);
            });
        }
    }
}
