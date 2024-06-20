package librarymanagementsystem.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import librarymanagementsystem.assignment.service.ReminderService;

@RestController
@RequestMapping("api/reminder")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;
    
    @PostMapping
    public void sendReminderEmail(){
        reminderService.sendReminders();
    }
}
