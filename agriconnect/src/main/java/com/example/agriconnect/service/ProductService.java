

package com.example.agriconnect.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.agriconnect.dto.ProductDto;
import com.example.agriconnect.dto.VendorDto;
import com.example.agriconnect.entity.ProductEntity;
import com.example.agriconnect.entity.VendorEntity;
import com.example.agriconnect.exception.ProductNotFoundException;
import com.example.agriconnect.exception.VendorNotFoundException;
import com.example.agriconnect.repository.ProductRepository;
import com.example.agriconnect.repository.VendorRepository;

@Service
public class ProductService {
		
	   private final ProductRepository productRepository;
	    private final VendorRepository vendorRepository;
	   
	    public ProductService(ProductRepository productRepository, VendorRepository vendorRepository) {
	        this.productRepository = productRepository;
	        this.vendorRepository = vendorRepository;
	    }
		
		@Autowired
		private ModelMapper mapper;
		
		public ProductDto convertToDto(ProductEntity entity) {
			return mapper.map(entity, ProductDto.class);
		}
		public ProductEntity convertToEntity(ProductDto dto) {
			return mapper.map(dto, ProductEntity.class);
		}
		
		
		public ProductDto addProduct(Long vendorId, ProductDto dto) {

		    VendorEntity vendorEntity = vendorRepository.findById(vendorId)
		            .orElseThrow(() -> new VendorNotFoundException("Vendor not found"));
		    ProductEntity productEntity = convertToEntity(dto);
		    productEntity.setVendorEntity(vendorEntity);

		    if (vendorEntity.getProductEntities() == null) {
		        vendorEntity.setProductEntities(new ArrayList<>());
		    }

if (!vendorEntity.isVerified()) {
    throw new RuntimeException("Vendor account is not verified");
}

		    vendorEntity.getProductEntities().add(productEntity);

		    ProductEntity savedProduct = productRepository.save(productEntity);
		    return convertToDto(savedProduct);
		}


		public ProductDto removeProduct(Long productId) {
			ProductEntity product=productRepository.findById(productId)
					.orElseThrow(()-> new ProductNotFoundException("Product not found with id: "+productId));
			VendorEntity vendorEntity=product.getVendorEntity();
			if(vendorEntity != null) {
				vendorEntity.getProductEntities().remove(product);
				
			}
			productRepository.delete(product);
			return convertToDto(product);
		}
		
		public ProductDto updateProduct(Long productId, ProductDto dto) {
			ProductEntity entity=productRepository.findById(productId)
					.orElseThrow(()->new ProductNotFoundException("Product not found with id: "+productId));
			entity.setProductName(dto.getProductName());
		    entity.setProductQuantity(dto.getProductQuantity());
		    entity.setCategory(dto.getCategory());
		    entity.setCost(dto.getCost());
		    entity.setImage(dto.getImage());
		    entity.setDescription(dto.getDescription());
		    
		    ProductEntity updated=productRepository.save(entity);
		    return convertToDto(updated); 
					
		}
		
		public List<ProductDto> getAllProducts() {
		    return productRepository.findAll()
		        .stream()
		        .map(product -> mapper.map(product, ProductDto.class))  
		        .collect(Collectors.toList());
		}

		public List<ProductDto> searchProducts(String searchTerm) {
		    List<ProductEntity> products;

		    if (searchTerm != null && !searchTerm.isEmpty()) {
		        products = productRepository
		                .findByProductNameContainingIgnoreCaseOrVendorEntity_LocationEntity_DistrictContainingIgnoreCase(searchTerm, searchTerm);
		    } else {
		        products = productRepository.findAll();
		    }

		    return products.stream()
		            .map(product -> mapper.map(product, ProductDto.class))
		            .collect(Collectors.toList());
		}


//			
		  public ProductDto getProductById(Long productId) {
			    ProductEntity productEntity = productRepository.findById(productId)
			            .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

			    return mapper.map(productEntity, ProductDto.class);			}
}
