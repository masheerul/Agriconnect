package com.example.agriconnect.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.agriconnect.dto.CartDto;
import com.example.agriconnect.service.CartService;

@RestController
@RequestMapping("api/cart")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CartController {
		
		
		@Autowired
		private CartService service;
		
		 @PostMapping("/addProduct")
		  public ResponseEntity<CartDto> cartAddProductToCart(
		      @RequestParam(name = "userId") Long userId, 
		      @RequestParam(name = "productId") Long productId,
		      @RequestParam(name = "quantity") int quantity) {
		 
		    if (userId == null) {
		      return new  ResponseEntity<>(HttpStatus.BAD_REQUEST);
		    }

		    CartDto updatedCart = service.addProductToCart(userId, productId, quantity);
		    return ResponseEntity.ok(updatedCart);  
		  }
		
		@DeleteMapping("/remove")
	    public ResponseEntity<CartDto> removeProductFromCart(
	            @RequestParam Long userId,
	            @RequestParam Long productId) {

	        CartDto updatedCart = service.removeProductFromCart(userId, productId);
	        return ResponseEntity.ok(updatedCart);
	    }
		
		
		 @DeleteMapping("/clean/{userId}")
		    public ResponseEntity<CartDto> cleanOutOfStockItems(@PathVariable Long userId) {
		        CartDto cleanedCart = service.cleanOutOfStockItems(userId);
		        return new ResponseEntity<>(cleanedCart,HttpStatus.NO_CONTENT);
		}
		 @GetMapping("/{userId}")
		    public ResponseEntity<CartDto> getCartStatus(@PathVariable Long userId) {
		        try {
		            CartDto cartDto = service.getCartStatus(userId);
		            return ResponseEntity.ok(cartDto);
		        } catch (RuntimeException e) {
		            
		            CartDto emptyCart = new CartDto();
		            emptyCart.setCartItems(new ArrayList<>());
		            emptyCart.setTotalAmount(0.0);
		            emptyCart.setTotalItems(0);
		            return ResponseEntity.ok(emptyCart);
		        }
		    }
			
			@DeleteMapping("/clear/{userId}")
			public ResponseEntity<String> clearCart(@PathVariable Long userId) {
			   service.clearCart(userId);
			    return ResponseEntity.ok("Cart has been successfully cleared.");
			}
			
			
}
