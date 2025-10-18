package com.example.agriconnect.service;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.agriconnect.dto.PaymentDto;
import com.example.agriconnect.entity.OrderEntity;
import com.example.agriconnect.entity.PaymentEntity;
import com.example.agriconnect.enums.OrderStatus;
import com.example.agriconnect.enums.PaymentMethods;
import com.example.agriconnect.enums.PaymentStatus;
import com.example.agriconnect.repository.OrderRepository;
import com.example.agriconnect.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@Service
public class PaymentService {

	private static final String KEY = "rzp_test_Su4WV4zdBIGTmZ";
    private static final String SECRET = "EmH6eToe5CvCfAfgfADREv3C";

		
		@Autowired
		private PaymentRepository paymentRepository;
		@Autowired
		private OrderRepository orderRepository;
		
		@Autowired
		private ModelMapper mapper;
		
		public PaymentDto creatPayment(Long orderId,Double amount, PaymentMethods methods,boolean success) throws RazorpayException {
			OrderEntity entity=orderRepository.findById(orderId)
					.orElseThrow(()-> new RuntimeException("Order not fount"));
			
			RazorpayClient  client=new RazorpayClient(KEY, SECRET);
			
			JSONObject orderRequest =new JSONObject();
			orderRequest .put("amount",(int)(amount*100));
			orderRequest .put("currency","INR");
			orderRequest.put("receipt", "txn_" + System.currentTimeMillis());
			 Order razorpayOrder = client.orders.create(orderRequest);
			
			 PaymentEntity paymentEntity=new PaymentEntity();
			 paymentEntity.setTransactionId(razorpayOrder.get("id"));
			 paymentEntity.setTotalAmount(amount);
			 paymentEntity.setPaymentMethods(methods);
			 paymentEntity.setStatus(PaymentStatus.PENDING);
			 paymentEntity.setOrderEntity(entity);
			 
			 paymentRepository.save(paymentEntity);
			 
			 if(success) {
				 paymentEntity.setStatus(PaymentStatus.SUCCESS);
				 entity.setStatus(OrderStatus.CONFIRMED);
			 }
			 else {
				 paymentEntity.setStatus(PaymentStatus.FAILED);
				 entity.setStatus(OrderStatus.FAILED);
			 }
			 
			 paymentRepository.save(paymentEntity);
			    orderRepository.save(entity);
			 return mapper.map(paymentEntity, PaymentDto.class);
		}
		
		
		public PaymentDto updatePaymentStatus(String transactionId, boolean success) {
		 		    PaymentEntity payment = paymentRepository.findByTransactionId(transactionId)
		            .orElseThrow(() -> new RuntimeException("Payment not found"));

		    payment.setStatus(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
		    OrderEntity order = payment.getOrderEntity();
		    order.setStatus(success ? OrderStatus.CONFIRMED : OrderStatus.FAILED);

		    paymentRepository.save(payment);
		    orderRepository.save(order);

		    return mapper.map(payment, PaymentDto.class);
		}

		
}
