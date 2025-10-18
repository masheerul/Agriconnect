package com.example.agriconnect.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.agriconnect.dto.ProductDto;
import com.example.agriconnect.entity.ProductEntity;
import com.example.agriconnect.exception.ProductNotFoundException;
import com.example.agriconnect.exception.VendorNotFoundException;
import com.example.agriconnect.service.ProductService;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProductController {

    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
  
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        try {
            List<ProductDto> products = productService.getAllProducts();
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        try {
            ProductDto product = productService.getProductById(productId);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @PostMapping(value = "/{vendorId}/addproducts", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDto> saveProducts(
            @PathVariable Long vendorId,
            @RequestParam("productName") String productName,
            @RequestParam("productQuantity") int productQuantity,
            @RequestParam("category") String category,
            @RequestParam("cost") double cost,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam("description") String description,
            @RequestParam("unit") String unit) {
        
        try {
            ProductDto dto = new ProductDto();
            dto.setProductName(productName);
            dto.setProductQuantity(productQuantity);
            dto.setCategory(category);
            dto.setCost(cost);
            dto.setDescription(description);
            dto.setUnit(unit);
            
           
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    String imageData = Base64.getEncoder().encodeToString(imageFile.getBytes());
                    String fileExtension = getFileExtension(imageFile.getOriginalFilename());
                    dto.setImage("data:image/" + fileExtension + ";base64," + imageData);
                } catch (IOException e) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            
            ProductDto productDto = productService.addProduct(vendorId, dto);
            return new ResponseEntity<>(productDto, HttpStatus.CREATED);
            
        } catch (VendorNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   
// 
  
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long productId, 
            @RequestBody ProductDto dto) {
        
        try {
            ProductDto updatedProduct = productService.updateProduct(productId, dto);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeProductFromVendor(@PathVariable Long productId) {
        try {
            productService.removeProduct(productId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "jpeg"; 
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam(required = false) String searchTerm) {
        try {
            List<ProductDto> products = productService.searchProducts(searchTerm);
            return new ResponseEntity<>(products, HttpStatus.OK); 
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}