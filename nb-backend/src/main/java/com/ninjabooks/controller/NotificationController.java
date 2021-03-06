package com.ninjabooks.controller;

import com.ninjabooks.json.message.MessageResponse;
import com.ninjabooks.json.notification.BorrowNotification;
import com.ninjabooks.json.notification.QueueNotification;
import com.ninjabooks.service.rest.notification.NotificationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
public class NotificationController
{
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RequestMapping(value = "/api/notification/{userID}", method = RequestMethod.GET)
    ResponseEntity<?> userNotification(@PathVariable Long userID) {
        List<BorrowNotification> borrowNotifications = notificationService.findUserBorrows(userID);
        List<QueueNotification> queueNotifications = notificationService.findUserQueues(userID);

        Map<String, List> map = new HashMap<>();
        map.put("Books list", borrowNotifications);
        map.put("Queues list", queueNotifications);

        if (borrowNotifications.isEmpty() && queueNotifications.isEmpty()) {
            String message = "User has no notification";
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.OK);
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
