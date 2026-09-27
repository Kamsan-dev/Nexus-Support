package com.kamsan.notificationservice.service.implementation;

import com.kamsan.notificationservice.dto.SendCommentEmailDTO;
import com.kamsan.notificationservice.dto.SendFilesEmailDTO;
import com.kamsan.notificationservice.dto.SendTicketEmailDTO;
import com.kamsan.notificationservice.dto.SendTokenEmailDTO;
import com.kamsan.notificationservice.service.EmailService;
import com.kamsan.notificationservice.utils.NotificationUtils;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.OffsetDateTime;
import java.util.Map;

import static com.kamsan.notificationservice.utils.DateFormatter.shortDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String ACCOUNT_VERIFICATION_TEMPLATE = "newaccount";
    public static final String PASSWORD_RESET_TEMPLATE = "resetpassword";
    public static final String NEW_TICKET_TEMPLATE = "newticket";
    public static final String NEW_COMMENT_TEMPLATE = "newcomment";
    public static final String NEW_FILE_TEMPLATE = "newfile";
    public static final String NEW_TICKET_REQUEST = "New Support Ticket";
    public static final String PASSWORD_RESET_REQUEST = "Password Reset Request";
    public static final String NEW_FILES_REQUEST = "New file(s) uploaded on ";
    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New Account Verification";
    public static final String NEW_COMMENT_REQUEST = "New comment(s) on ";

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendNewAccountHtmlEmail(SendTokenEmailDTO sendTokenEmailDTO) {
        try {
            var context = new Context();
            context.setVariables(Map.of(
                    "name", sendTokenEmailDTO.name(),
                    "url", NotificationUtils.getVerificationUrl(this.host, sendTokenEmailDTO.token())
            ));
            String text = templateEngine.process(ACCOUNT_VERIFICATION_TEMPLATE, context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setFrom("contact@nexus-support.com");
            helper.setPriority(1);
            helper.setTo(sendTokenEmailDTO.email());
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setText(text, true);
            emailSender.send(message);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendPasswordResetHtmlEmail(SendTokenEmailDTO sendTokenEmailDTO) {
        try {
            var context = new Context();
            context.setVariables(Map.of(
                    "name", sendTokenEmailDTO.name(),
                    "url", NotificationUtils.getResetPasswordUrl(this.host, sendTokenEmailDTO.token())
            ));
            String text = templateEngine.process(PASSWORD_RESET_TEMPLATE, context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setFrom("contact@nexus-support.com");
            helper.setPriority(1);
            helper.setTo(sendTokenEmailDTO.email());
            helper.setSubject(PASSWORD_RESET_REQUEST);
            helper.setText(text, true);
            emailSender.send(message);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendNewTicketHtmlEmail(SendTicketEmailDTO sendTicketEmailDTO) {
        try {
            var context = new Context();
            context.setVariables(Map.of(
                    "name", sendTicketEmailDTO.name(),
                    "ticketTitle", sendTicketEmailDTO.ticketTitle(),
                    "ticketNumber", "Ticket #" + sendTicketEmailDTO.ticketNumber(),
                    "date", shortDate(OffsetDateTime.now()),
                    "priority", sendTicketEmailDTO.priority(),
                    "url", NotificationUtils.getTicketUrl(this.host, sendTicketEmailDTO.ticketNumber())
            ));
            String text = templateEngine.process(NEW_TICKET_TEMPLATE, context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setFrom("contact@nexus-support.com");
            helper.setPriority(1);
            helper.setTo(sendTicketEmailDTO.email());
            helper.setSubject(NEW_TICKET_REQUEST);
            helper.setText(text, true);
            emailSender.send(message);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendNewFilesHtmlEmail(SendFilesEmailDTO sendFilesEmailDTO) {
        try {
            var context = new Context();
            context.setVariables(Map.of(
                    "name", sendFilesEmailDTO.name(),
                    "ticketTitle", sendFilesEmailDTO.ticketTitle(),
                    "ticketNumber", "Ticket #" + sendFilesEmailDTO.ticketNumber(),
                    "files", sendFilesEmailDTO.files().split(","),
                    "date", sendFilesEmailDTO.date(),
                    "priority", sendFilesEmailDTO.priority(),
                    "url", NotificationUtils.getTicketUrl(this.host, sendFilesEmailDTO.ticketNumber())
            ));
            String text = templateEngine.process(NEW_FILE_TEMPLATE, context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setFrom("contact@nexus-support.com");
            helper.setPriority(1);
            helper.setTo(sendFilesEmailDTO.email());
            helper.setSubject(NEW_FILES_REQUEST + "Ticket #" + sendFilesEmailDTO.ticketNumber());
            helper.setText(text, true);
            emailSender.send(message);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendNewCommentHtmlEmail(SendCommentEmailDTO sendCommentEmailDTO) {
        try {
            var context = new Context();
            context.setVariables(Map.of(
                    "name", sendCommentEmailDTO.name(),
                    "ticketTitle", sendCommentEmailDTO.ticketTitle(),
                    "ticketNumber", "Ticket #" + sendCommentEmailDTO.ticketNumber(),
                    "comment", sendCommentEmailDTO.comment(),
                    "date", sendCommentEmailDTO.date(),
                    "priority", sendCommentEmailDTO.priority(),
                    "url", NotificationUtils.getTicketUrl(this.host, sendCommentEmailDTO.ticketNumber())
            ));
            String text = templateEngine.process(NEW_COMMENT_TEMPLATE, context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setFrom("contact@nexus-support.com");
            helper.setPriority(1);
            helper.setTo(sendCommentEmailDTO.email());
            helper.setSubject(NEW_COMMENT_REQUEST + "Ticket #" + sendCommentEmailDTO.ticketNumber());
            helper.setText(text, true);
            emailSender.send(message);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }
}
