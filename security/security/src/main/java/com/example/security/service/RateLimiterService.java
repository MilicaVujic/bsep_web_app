package com.example.security.service;

import com.example.security.dto.NotificationMessageDto;
import com.example.security.model.CriticalEventFailedLogin;
import com.example.security.model.PackageType;
import com.example.security.model.User;
import com.example.security.repository.CriticalEventBlockedUserLoginRepository;
import com.example.security.repository.CriticalEventFailedLoginRepository;
import com.example.security.repository.UserRepository;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class RateLimiterService implements IRateLimiterService {

    Logger logger= LoggerFactory.getLogger(RateLimiterService.class);

    private static final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    @Autowired
    private CriticalEventFailedLoginRepository criticalEventFailedLoginRepository;
    @Autowired
    private CriticalEventBlockedUserLoginRepository criticalEventBlockedUserLoginRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private NotificationService notificationService;

    public boolean tryAcquire(String packageType, Long advertisementId) {
        try {
            logger.info("Trying to acquire permit for package type '{}' and advertisement ID '{}'", packageType, advertisementId);
            String key = packageType + "_" + advertisementId;
            Bucket bucket = buckets.computeIfAbsent(key, this::createBucketForPackageAndAdvertisement);
            boolean acquired = bucket.tryConsume(1);
            if (acquired) {
                logger.info("Permit acquired for package type '{}' and advertisement ID '{}'", packageType, advertisementId);
            } else {
                logger.warn("Permit not acquired for package type '{}' and advertisement ID '{}'. Bucket is empty.", packageType, advertisementId);
            }
            return acquired;
        } catch (Exception e) {
            logger.error("An error occurred while trying to acquire permit for package type '{}' and advertisement ID '{}': {}", packageType, advertisementId, e.getMessage(), e);
            return false;
        }
    }


    private Bucket createBucketForPackageAndAdvertisement(String key) {
        int capacity;
        switch (key.split("_")[0]) {
            case "BASIC":
                capacity = 10;
                break;
            case "STANDARD":
                capacity = 100;
                break;
            case "GOLD":
                capacity = 10000;
                break;
            default:
                throw new IllegalArgumentException("Invalid package type: " + key.split("_")[0]);
        }
        Bandwidth limit = Bandwidth.classic(capacity, Refill.intervally(capacity, Duration.ofMinutes(10)));
        return Bucket4j.builder().addLimit(limit).build();
    }

    @Scheduled(cron = "0 * * * * *")
    public void checkCriticalEventFailedLogin()
    {
        List<User> admins = userRepository.findAdmins();
        List<String> blockedUsers = criticalEventBlockedUserLoginRepository.findUsersWithFiveOrMoreBlockedUserLoginsWithinLastMinute(LocalDateTime.now().minusMinutes(1));
        if(blockedUsers != null && !blockedUsers.isEmpty())
        {
            String notificationMessage = "Hello, you have a new notification!";
            notificationService.sendNotificationToUser("anja@gmail.com", notificationMessage);
            for(User u: admins)
                emailService.sendBlockedUserLoginToAdmin(u.getUsername(),blockedUsers);
            emailService.sendBlockedUserLoginToAdmin("anja.lovric@gmail.com",blockedUsers);
        }
        List<String> emails = criticalEventFailedLoginRepository.findUsersWithFiveOrMoreFailedLoginsWithinLastMinute(LocalDateTime.now().minusMinutes(1));
        if(emails != null && !emails.isEmpty())
        {
            String notificationMessage = "Hello, you have a new notification!";
            notificationService.sendNotificationToUser("anja@gmail.com", notificationMessage);
            for(User u: admins)
                emailService.sendFailedLoginToAdmin(u.getUsername(),emails);
            emailService.sendFailedLoginToAdmin("kivana0191@gmail.com",emails);
        }
    }

    public NotificationMessageDto checkCriticalEventFailedLoginFromFront()
    {
        List<String> blockedUsers = criticalEventBlockedUserLoginRepository.findUsersWithFiveOrMoreBlockedUserLoginsWithinLastMinute(LocalDateTime.now().minusMinutes(1));
        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setBody("/");
        if(blockedUsers != null && !blockedUsers.isEmpty())
        {
            StringBuilder messageText = new StringBuilder();
            messageText.append("Critical event blocked user login detected for the following users:\n\n");
            for (String userEmail : blockedUsers) {
                messageText.append("- ").append(userEmail).append("\n");
            }
            messageText.append("\nPlease take appropriate action to address these login failures.");
            notificationMessageDto.setBody(messageText.toString());
        }
        List<String> emails = criticalEventFailedLoginRepository.findUsersWithFiveOrMoreFailedLoginsWithinLastMinute(LocalDateTime.now().minusMinutes(1));
        if(emails != null && !emails.isEmpty())
        {
            StringBuilder messageText = new StringBuilder();
            messageText.append("Critical event failed login detected for the following users:\n\n");
            for (String userEmail : emails) {
                messageText.append("- ").append(userEmail).append("\n");
            }
            messageText.append("\nPlease take appropriate action to address these login failures.");
            notificationMessageDto.setBody(messageText.toString());
        }
        return notificationMessageDto;
    }
}
