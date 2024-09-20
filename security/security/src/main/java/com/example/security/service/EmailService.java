package com.example.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    Logger logger= LoggerFactory.getLogger(EmailService.class);


    public void sendPassworlessLogingEmail(String email) {
        try {
            logger.info("Sending passwordless login email to: {}", email);
            String activationLink = HMACUtils.generateLogingLink(email);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Log in");
            message.setText("Click the following link to log in: " + activationLink);
            javaMailSender.send(message);
            logger.info("Passwordless login email sent successfully to: {}", email);
        } catch (Exception e) {
            logger.error("An error occurred while sending passwordless login email to {}: {}", email, e.getMessage(), e);
        }
    }


    public void sendActivationEmail(String recipientEmail, String username) {
        try {
            logger.info("Sending activation email to: {}", recipientEmail);
            String activationLink = HMACUtils.generateActivationLink(username);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Activate Your Account");
            message.setText("Click the following link to activate your account: " + activationLink);
            javaMailSender.send(message);
            logger.info("Activation email sent successfully to: {}", recipientEmail);
        } catch (Exception e) {
            logger.error("An error occurred while sending activation email to {}: {}", recipientEmail, e.getMessage(), e);
        }
    }



    public void sendRejectingExplanation(String recipientEmail, String explanation) {
        try {
            logger.info("Sending rejection explanation email to: {}", recipientEmail);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Your registration request is rejected");
            message.setText("Reason for rejecting id: " + explanation);
            javaMailSender.send(message);
            logger.info("Rejection explanation email sent successfully to: {}", recipientEmail);
        } catch (Exception e) {
            logger.error("An error occurred while sending rejection explanation email to {}: {}", recipientEmail, e.getMessage(), e);
        }
    }




    public void sendResetToken(String recipientEmail, String token) {
        try {
            logger.info("Sending reset token email to: {}", recipientEmail);
            String resetTokenLink = HMACUtils.generateResetTokenLink(token, recipientEmail);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Reset password");
            message.setText("Click the following link to reset your password: " + resetTokenLink);
            javaMailSender.send(message);
            logger.info("Reset token email sent successfully to: {}", recipientEmail);
        } catch (Exception e) {
            logger.error("An error occurred while sending reset token email to {}: {}", recipientEmail, e.getMessage(), e);
        }
    }

    public void sendFailedLoginToAdmin(String recipientEmail,List<String> failedUserEmails) {
        try {
            logger.info("Sending critical event failed login email to admin...");

            StringBuilder messageText = new StringBuilder();
            messageText.append("Critical event failed login detected for the following users:\n\n");
            for (String userEmail : failedUserEmails) {
                messageText.append("- ").append(userEmail).append("\n");
            }
            messageText.append("\nPlease take appropriate action to address these login failures.");

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Warning: Multiple Critical Event Failed Logins");
            message.setText(messageText.toString());

            javaMailSender.send(message);

            logger.info("Critical event failed login email sent successfully to admin.");
        } catch (Exception e) {
            logger.error("An error occurred while sending critical event failed login email to admin: {}", e.getMessage(), e);
        }
    }

    public void sendBlockedUserLoginToAdmin(String recipientEmail,List<String> failedUserEmails) {
        try {
            logger.info("Sending critical event blocked user login email to admin...");

            StringBuilder messageText = new StringBuilder();
            messageText.append("Critical event blocked user login detected for the following users:\n\n");
            for (String userEmail : failedUserEmails) {
                messageText.append("- ").append(userEmail).append("\n");
            }
            messageText.append("\nPlease take appropriate action to address these login failures.");

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Warning: Multiple Critical Event Blocked User Logins");
            message.setText(messageText.toString());

            javaMailSender.send(message);

            logger.info("Critical event blocked user login email sent successfully to admin.");
        } catch (Exception e) {
            logger.error("An error occurred while sending critical event blocked user login email to admin: {}", e.getMessage(), e);
        }
    }


}