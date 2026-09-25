package com.kamsan.notificationservice.service.implementation;

import com.kamsan.notificationservice.dto.SendFilesEmailDTO;
import com.kamsan.notificationservice.dto.SendTicketEmailDTO;
import com.kamsan.notificationservice.dto.SendTokenEmailDTO;
import com.kamsan.notificationservice.service.EmailService;

public class EmailServiceImpl implements EmailService {
    @Override
    public void sendNewAccountHtmlEmail(SendTokenEmailDTO sendTokenEmailDTO) {
        
    }

    @Override
    public void sendPasswordResetHtmlEmail(SendTokenEmailDTO sendTokenEmailDTO) {

    }

    @Override
    public void sendNewTicketHtmlEmail(SendTicketEmailDTO sendTicketEmailDTO) {

    }

    @Override
    public void sendNewFilesHtmlEmail(SendFilesEmailDTO sendFilesEmailDTO) {

    }
}
