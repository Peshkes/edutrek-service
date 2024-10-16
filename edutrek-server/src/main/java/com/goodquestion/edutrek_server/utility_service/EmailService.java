package com.goodquestion.edutrek_server.utility_service;

import com.goodquestion.edutrek_server.utility_service.logging.Loggable;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.from}")
    private String from;

    private final JavaMailSender mailSender;

    private MimeMessageHelper createMimeMessageHelper(String toEmail, String subject, String body, boolean multipart) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart);

        helper.setFrom(from);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body);

        return helper;
    }

    @Loggable
    public void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        MimeMessageHelper helper = createMimeMessageHelper(toEmail, subject, body, false);
        mailSender.send(helper.getMimeMessage());
    }

    @Loggable
    public void sendEmailWithAttachment(String toEmail, String subject, String body, byte[] attachmentData, String attachmentName) throws MessagingException {
        MimeMessageHelper helper = createMimeMessageHelper(toEmail, subject, body, true);
        helper.addAttachment(attachmentName, new ByteArrayResource(attachmentData));
        mailSender.send(helper.getMimeMessage());
    }

    @Loggable
    public void sendRegistrationEmail(String toEmail, String login, String password) throws MessagingException, IOException {
        ClassPathResource resource = new ClassPathResource("registration.webp");
        byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        String body = "<h1>Welcome to the EduTrek Team!</h1>"
                + "<p>We are thrilled to have you onboard as a new employee of EduTrek. Below are your login credentials to access your employee portal:</p>"
                + "<p><strong>Login:</strong> " + login + "</p>"
                + "<p><strong>Password:</strong> " + password + "</p>"
                + "<p>We look forward to working with you and hope that your journey with us will be fulfilling and rewarding. Please do not hesitate to reach out if you have any questions.</p>"
                + "<img src=\"data:image/jpeg;base64," + base64Image + "\" alt=\"Welcome to the team\" style=\"max-width: 100%; height: auto;\">";

        MimeMessageHelper helper = createMimeMessageHelper(toEmail, "🎉 Congratulations on Joining the Team, EduTrek Employee!", body, true);
        mailSender.send(helper.getMimeMessage());
    }
}
