package com.kamsan.notificationservice.service;

import com.kamsan.notificationservice.dto.SendFilesEmailDTO;
import com.kamsan.notificationservice.dto.SendTicketEmailDTO;
import com.kamsan.notificationservice.dto.SendTokenEmailDTO;

public interface EmailService {

    void sendNewAccountHtmlEmail(SendTokenEmailDTO sendTokenEmailDTO);

    void sendPasswordResetHtmlEmail(SendTokenEmailDTO sendTokenEmailDTO);

    void sendNewTicketHtmlEmail(SendTicketEmailDTO sendTicketEmailDTO);

    void sendNewFilesHtmlEmail(SendFilesEmailDTO sendFilesEmailDTO);

}
