package com.example.agriconnect.service;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.agriconnect.dto.CartDto;
import com.example.agriconnect.entity.CartEntity;
import com.example.agriconnect.entity.CartItemEntity;
import com.example.agriconnect.entity.ProductEntity;
import com.example.agriconnect.entity.UserEntity;
import com.example.agriconnect.entity.VendorEntity;
import com.example.agriconnect.enums.Role;
import com.example.agriconnect.exception.ProductNotFoundException;
import com.example.agriconnect.exception.UserNotFoundException;
import com.example.agriconnect.repository.CartItemRepository;
import com.example.agriconnect.repository.CartRepository;
import com.example.agriconnect.repository.ProductRepository;
import com.example.agriconnect.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartService {
		
		
		@Autowired
		private UserRepository userRepository;
		@Autowired
		private ProductRepository productRepository;
		@Autowired
		private CartRepository cartRepository;
		@Autowired
		private ModelMapper mapper;
		@Autowired
		private CartItemRepository cartItemRepository;
		
		public CartDto addProductToCart(Long userId, Long productId, int quantity) {
		
		    UserEntity user = userRepository.findById(userId)
		            .orElseThrow(() -> new UserNotFoundException("User not found"));

		    ProductEntity product = productRepository.findById(productId)
		            .orElseThrow(() -> new RuntimeException("Product not found"));
		    
		   VendorEntity vendor = product.getVendorEntity();
//		   if (vendor == null || vendor.getUser() == null || 
//				    vendor.getUser().getRoles().contains(Role.VENDOR)) {
//				    throw new RuntimeException("This product is not uploaded by a valid vendor.");
//				}
		   if (vendor == null) {
			    throw new RuntimeException("Product vendor information is missing.");
			}

			if (vendor.getUser() == null) {
			    throw new RuntimeException("Vendor user account not found.");
			}

			if (!vendor.getUser().getRoles().contains(Role.VENDOR)) {
			    throw new RuntimeException("This product is not uploaded by a valid vendor.");
			}

		    
		    int availableQuantity = product.getProductQuantity();
		 if (quantity > availableQuantity) {
		     throw new RuntimeException("Requested quantity is more than available stock. Available: " + availableQuantity + " kg");
		 }

		    CartEntity cartEntity = cartRepository.findByUserEntity(user)
		            .orElseGet(() -> {
		                CartEntity newCart = new CartEntity();
		                newCart.setUserEntity(user);
		                newCart.setTotalAmount(0.0);
		                newCart.setTotalItems(0);
		                newCart.setCartItems(new ArrayList<>());
		                return cartRepository.save(newCart);
		            });

		    CartItemEntity existingItem = cartEntity.getCartItems().stream()
		            .filter(item -> item.getProduct().getId().equals(productId))
		            .findFirst()
		            .orElse(null);

		    if (existingItem != null) {
		        int newQuantity = existingItem.getQuantity() + quantity;
		        if (newQuantity > availableQuantity) {
		            throw new RuntimeException("Cannot add more than available stock. Available: " + availableQuantity + " kg");
		        }
		        existingItem.setQuantity(newQuantity);
		        existingItem.setPrice(product.getCost());
		        cartItemRepository.save(existingItem);
		    } else {
		        CartItemEntity newItem = new CartItemEntity();
		        newItem.setCart(cartEntity);
		        newItem.setProduct(product);
		        newItem.setQuantity(quantity);
		        newItem.setPrice(product.getCost()); 
		        cartEntity.getCartItems().add(newItem);
		        cartItemRepository.save(newItem);
		    }	
		    int totalItems = cartEntity.getCartItems().stream()
		            .mapToInt(CartItemEntity::getQuantity)
		            .sum();
		    cartEntity.setTotalItems(totalItems);

		    double totalAmount = cartEntity.getCartItems().stream()
		            .mapToDouble(item -> item.getPrice() * item.getQuantity()) 
		            .sum();
		    cartEntity.setTotalAmount(totalAmount);


		    cartRepository.save(cartEntity);
		    return mapper.map(cartEntity, CartDto.class);
		}

		public CartDto removeProductFromCart(Long userId, Long productId) {
			
			UserEntity userEntity=userRepository.findById(userId)
					.orElseThrow(()->new UserNotFoundException("User not found"));
			
			CartEntity cartEntity=cartRepository.findByUserEntity(userEntity)
					.orElseThrow(()->new RuntimeException("Cart Not found"));
			
			CartItemEntity itemToRemove=cartEntity.getCartItems().stream()
					.filter(item -> item.getProduct().getId().equals(productId))
					.findFirst()
					.orElseThrow(()-> new ProductNotFoundException("Product not found in cart"));
			cartEntity.getCartItems().remove(itemToRemove);
			
			
			int totalItems = cartEntity.getCartItems().stream()
			        .mapToInt(CartItemEntity::getQuantity)
			        .sum();
			cartEntity.setTotalItems(totalItems);
			double totalAmount=cartEntity.getCartItems().stream()
					.mapToDouble(CartItemEntity::getPrice)
					.sum();
			cartEntity.setTotalAmount(totalAmount);
			
			cartRepository.save(cartEntity);
			
			return mapper.map(cartEntity, CartDto.class);
					
		}
		
		
		public CartDto getCartStatus(Long userId) {
	        UserEntity userEntity = userRepository.findById(userId)
	            .orElseThrow(() -> new UserNotFoundException("User not found"));
	        
	        CartEntity cartEntity = cartRepository.findByUserEntity(userEntity)
	            .orElseGet(() -> {
	                CartEntity newCart = new CartEntity();
	                newCart.setUserEntity(userEntity);
	                newCart.setCartItems(new ArrayList<>());
	                newCart.setTotalAmount(0.0);
	                newCart.setTotalItems(0);
	                return cartRepository.save(newCart);
	            });
	        if (cartEntity.getCartItems() != null) {
	            cartEntity.getCartItems().size(); 
	        }
	        
	        CartDto cartDto = mapper.map(cartEntity, CartDto.class);
	        
	        double totalAmount = cartEntity.getCartItems().stream()
	            .mapToDouble(item -> item.getPrice() * item.getQuantity())
	            .sum();
	        
	        int totalItems = cartEntity.getCartItems().stream()
	            .mapToInt(CartItemEntity::getQuantity)
	            .sum();
	        
	        cartDto.setTotalAmount(totalAmount);
	        cartDto.setTotalItems(totalItems);
	        	
	        return cartDto;
	    }
//		

		public CartDto cleanOutOfStockItems(Long userId) {
			UserEntity userEntity=userRepository.findById(userId)
					.orElseThrow(()-> new UserNotFoundException("user  not found"));
		
			CartEntity cartEntity=cartRepository.findByUserEntity(userEntity)
					.orElseThrow(()->new RuntimeException("cart not found"));
			
			cartEntity.getCartItems().removeIf(item -> item.getProduct().getStockQuantity() <=0);
			
			 int totalItems = cartEntity.getCartItems().stream()
			            .mapToInt(CartItemEntity::getQuantity)
			            .sum();
			 cartEntity.setTotalItems(totalItems);
			 
			 double totalAmount=cartEntity.getCartItems().stream()
					 .mapToDouble(CartItemEntity::getPrice)
					 .sum();
			 cartEntity.setTotalAmount(totalAmount);
			 cartRepository.save(cartEntity);
			 return mapper.map(cartEntity, CartDto.class);
			 
		}
		
		 public void clearCart(Long userId) {
		        UserEntity user = userRepository.findById(userId)
		            .orElseThrow(() -> new UserNotFoundException("User not found"));

		        CartEntity cart = cartRepository.findByUserEntity(user)
		            .orElseThrow(() -> new RuntimeException("Cart not found"));

		        cart.getCartItems().clear();
		        cart.setTotalAmount(0.0);
		        cart.setTotalItems(0);

		        cartRepository.save(cart);
		    }
}
