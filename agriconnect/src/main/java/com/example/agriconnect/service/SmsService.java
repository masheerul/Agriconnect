package com.example.agriconnect.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsService {

	
	@Value("${sms.api.user}")
    private String smsUser;

    @Value("${sms.api.password}")
    private String smsPassword;

    @Value("${sms.api.url}")
    private String smsUrl;
    
    public void sendOtpSms(String mobileNumber, String otp) {
        String url = smsUrl + "?type=smsquicksend&user=" + smsUser + "&pass=" + smsPassword
                + "&mobile=" + mobileNumber + "&sms_text=Your OTP is " + otp;

        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("SMS sent! Response: " + response);
        } catch (Exception e) {
            System.out.println("Error sending SMS: " + e.getMessage());
        }
    }
}
