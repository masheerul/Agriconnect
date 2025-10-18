package com.example.agriconnect.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.agriconnect.dto.DeliveryDto;
import com.example.agriconnect.dto.LocationDto;
import com.example.agriconnect.dto.OrderDto;
import com.example.agriconnect.dto.OrderTrackingDto;
import com.example.agriconnect.dto.OrderTrackingDto;
import com.example.agriconnect.dto.UserDto;
import com.example.agriconnect.entity.CartEntity;
import com.example.agriconnect.entity.CartItemEntity;
import com.example.agriconnect.entity.LocationEntity;
import com.example.agriconnect.entity.OrderEntity;
import com.example.agriconnect.entity.OrderItemEntity;
import com.example.agriconnect.entity.ProductEntity;
import com.example.agriconnect.entity.UserEntity;
import com.example.agriconnect.enums.OrderStatus;
import com.example.agriconnect.exception.ProductNotFoundException;
import com.example.agriconnect.exception.UserNotFoundException;
import com.example.agriconnect.repository.CartItemRepository;
import com.example.agriconnect.repository.CartRepository;
import com.example.agriconnect.repository.LocationRepository;
import com.example.agriconnect.repository.OrderItemRepository;
import com.example.agriconnect.repository.OrderRepository;
import com.example.agriconnect.repository.ProductRepository;
import com.example.agriconnect.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
		
		
			@Autowired
			private UserRepository userRepository;
			@Autowired
			private OrderRepository orderRepository;
			@Autowired
			private CartRepository cartRepository;
			@Autowired
			private ProductRepository productRepository;
			@Autowired
			private	OrderItemRepository orderItemRepository;  
			@Autowired
			private CartItemRepository cartItemRepository;
			@Autowired
			private ModelMapper mapper;
			@Autowired
			private LocationRepository locationRepository;
			
			private void recalculateCartTotal(CartEntity cart) {
			    int totalQuantity = 0;
			    double totalAmount = 0.0;

			    for (CartItemEntity item : cart.getCartItems()) {
			        totalQuantity += item.getQuantity();
			        totalAmount += item.getQuantity() * item.getProduct().getCost();
			    }

			    cart.setTotalItems(totalQuantity);   
			    cart.setTotalAmount(totalAmount);
			    cartRepository.save(cart);
			}


			@Transactional
			public OrderDto placeOrder(Long userId, Long productId, int quantity, boolean fromCart, DeliveryDto dto) {

			    
			    UserEntity user = userRepository.findById(userId)
			            .orElseThrow(() -> new UserNotFoundException("User not found"));

			    OrderEntity orderEntity = new OrderEntity();
			    orderEntity.setUser(user);
			    orderEntity.setStatus(OrderStatus.PENDING);
			    orderEntity.setCreatedAt(LocalDateTime.now());
			    if (dto != null) {
			        orderEntity.setDeliveryName(dto.getDeliveryName());
			        orderEntity.setDeliveryMobileNumber(dto.getDeliveryMobileNumber());

			        LocationEntity location = mapper.map(dto, LocationEntity.class);
			        Optional<LocationEntity> existingLocation = locationRepository.findByStateAndDistrictAndSubLocationAndZipCode(
			                location.getState(),
			                location.getDistrict(),
			                location.getSubLocation(),
			                location.getZipCode()
			        );
			        LocationEntity deliveryLocation = existingLocation.orElseGet(() -> locationRepository.save(location));
			        orderEntity.setDeliveryLocation(deliveryLocation);
			    }

			    orderEntity = orderRepository.save(orderEntity);
			    double totalAmount = 0.0;
			    List<OrderItemEntity> orderItems = new ArrayList<>();

			    if (fromCart) {
			        CartEntity cartEntity = cartRepository.findByUserEntity(user)
			                .orElseThrow(() -> new RuntimeException("Cart not found"));

			        Iterator<CartItemEntity> iterator = cartEntity.getCartItems().iterator();
			        while (iterator.hasNext()) {
			            CartItemEntity cartItem = iterator.next();
			            ProductEntity product = cartItem.getProduct();

			            if (product.getProductQuantity() < cartItem.getQuantity()) {
			                throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
			            }

			            product.setProductQuantity(product.getProductQuantity() - cartItem.getQuantity());
			            productRepository.save(product);

			            OrderItemEntity orderItemEntity = new OrderItemEntity();
			            orderItemEntity.setOrderEntity(orderEntity);
			            orderItemEntity.setProduct(product);
			            orderItemEntity.setQuantity(cartItem.getQuantity());
			            orderItemEntity.setPrice(product.getCost());
			            orderItemEntity.setTotal(cartItem.getQuantity() * product.getCost());

			            totalAmount += orderItemEntity.getTotal();

			            orderItemEntity = orderItemRepository.save(orderItemEntity);
			            orderItems.add(orderItemEntity);

			            iterator.remove();
			            cartItem.setCart(null);
			        }

			        recalculateCartTotal(cartEntity);
			        cartRepository.save(cartEntity);

			    } else {
			        ProductEntity productEntity = productRepository.findById(productId)
			                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

			        if (productEntity.getProductQuantity() < quantity) {
			            throw new RuntimeException("Insufficient stock for product: " + productEntity.getProductName());
			        }

			        productEntity.setProductQuantity(productEntity.getProductQuantity() - quantity);
			        productRepository.save(productEntity);

			        OrderItemEntity orderItemEntity = new OrderItemEntity();
			        orderItemEntity.setOrderEntity(orderEntity);
			        orderItemEntity.setProduct(productEntity);
			        orderItemEntity.setQuantity(quantity);
			        orderItemEntity.setPrice(productEntity.getCost());
			        orderItemEntity.setTotal(quantity * productEntity.getCost());

			        totalAmount = orderItemEntity.getTotal();

			        orderItemEntity = orderItemRepository.save(orderItemEntity);
			        orderItems.add(orderItemEntity);
			    }

			   
			    orderEntity.setOrderItems(orderItems);
			    orderEntity.setTotalAmount(totalAmount);
			    orderRepository.save(orderEntity);

			   
			    OrderDto orderDto = mapper.map(orderEntity, OrderDto.class);

			   
			    orderDto.setDeliveryDto(dto);

			    return orderDto;
			}

			
			public OrderTrackingDto getOrderStatus(Long orderId) {
	        OrderEntity order = orderRepository.findById(orderId)
	                .orElseThrow(() -> new RuntimeException("Order not found"));

	        OrderTrackingDto dto = new OrderTrackingDto();
	        dto.setOrderId(order.getId());
	        dto.setOrderStatus(order.getStatus().name());

	        if (order.getPaymentEntity() != null) {
	            dto.setPaymentStatus(order.getPaymentEntity().getStatus().name());
	            dto.setPaymentMethod(order.getPaymentEntity().getPaymentMethods().name());
	            dto.setTransactionId(order.getPaymentEntity().getTransactionId());
	            dto.setTotalAmount(order.getPaymentEntity().getTotalAmount());
	        }

	        return dto;
	    }
			
			
			public List<OrderDto> getOrdersByUser(Long userId) {
			    List<OrderEntity> orderEntities = orderRepository.findByUser_Id(userId);

			    return orderEntities.stream().map(order -> {
			        OrderDto dto = mapper.map(order, OrderDto.class);
			        if (order.getPaymentEntity()!= null) {
			            dto.setTotalAmount(order.getPaymentEntity().getTotalAmount());
			        } else {
			            double sum = order.getOrderItems().stream()
			                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
			                    .sum();
			            dto.setTotalAmount(sum);
			        }

			        return dto;
			    }).toList();
			}

			
			public OrderDto returnOrder(Long orderId,Long userId, String reason) {
				OrderEntity orderEntity=orderRepository.findById(orderId)
						.orElseThrow(()-> new RuntimeException("Order not found"));
				 if (!orderEntity.getUser().getId().equals(userId)) {
				        throw new IllegalStateException("You are not authorized to return this order");
				    }
				if(orderEntity.getStatus() !=OrderStatus.DELIVERED) {
					throw new IllegalStateException("Only delivered order can be returned ");
				}
				orderEntity.setStatus(OrderStatus.RETURN_REQUESTED);
				orderEntity.setReason(reason);
				orderRepository.save(orderEntity);
				
				return mapper.map(orderEntity, OrderDto.class);
			}

}
