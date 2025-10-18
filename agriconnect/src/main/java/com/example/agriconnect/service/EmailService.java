package com.example.agriconnect.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {
	
	@Value("${zeptomail.api.url}")
    private String zeptoMailUrl;

    @Value("${zeptomail.api.key}")
    private String zeptoMailApiKey;
    
    public void sendotpEMail(String toEmail,String otp) {
    	RestTemplate template=new RestTemplate();
    	Map<String, Object>requestBody=new HashMap<>();
    	Map<String, String> from = Map.of("address", "noreply@yourdomain.com", "name", "YourApp");
    	Map<String, String>toEmailAddress=Map.of("address",toEmail,"name","User");
    	Map<String, Object> toObject = Map.of("email_address", toEmailAddress);
    	requestBody.put("from",from);
    	requestBody.put("to", List.of(toObject));
    	requestBody.put("subject","Your Otp Code");
    	requestBody.put("htmlbody","<h2>Your otp is <b>"+otp+"</b></h2>"	);
    	
    	
    	 HttpHeaders headers = new HttpHeaders();
    	 headers.setContentType(MediaType.APPLICATION_JSON);
    	 headers.set("Authorization", "Zoho-enczapikey " + zeptoMailApiKey);
    	 
    	 HttpEntity<Map<String, Object>>entity=new HttpEntity<>(requestBody,headers);
//    	 try {
//    		 ResponseEntity<String>response=template.exchange(zeptoMailUrl	,HttpMethod.POST,entity,String.class);
//    		 System.out.println("Email sent Response"+response.getBody());
//    	 }
//    	 catch(Exception e) {
//    		 e.printStackTrace();
//    		 System.out.println("Error sending email"+e.getMessage());
//    	 }
    	 try {
    		    ResponseEntity<String> response = template.exchange(
    		        zeptoMailUrl, HttpMethod.POST, entity, String.class);
    		    System.out.println("Email sent Response: " + response.getStatusCode() + " " + response.getBody());
    		} catch (org.springframework.web.client.HttpClientErrorException e) {
    		    System.out.println("Status Code: " + e.getStatusCode());
    		    System.out.println("Response Body: " + e.getResponseBodyAsString());
    		} catch (Exception e) {
    			 System.out.println("error sending email"+e.getMessage());
    		    e.printStackTrace();
    		   
    		}
    }
}
