package com.web.movie.service.implement;

import com.web.movie.service.iterface.IEmailService;
import jakarta.mail.MessagingException;
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

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    @Value("${app.origin}")
    private String origin;

    @Value("${app.port}")
    private Integer port;
    @Override
    @Async
    public void sendMailConfirmRegistration(Map<String, String> data) {
        log.info("sendMailConfirmRegistration");
        log.info("Sending email request : {}", data);
        try{
            log.info("sendMailConfirmRegistration");
            log.info("Sending email request : {}", data);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(data.get("email"));
            helper.setSubject("Xác nhận đăng ký tài khoản");

            // Create the Thymeleaf context
            Context context = new Context();
            context.setVariable("username", data.get("username"));
            context.setVariable("token", data.get("token"));
            context.setVariable("origin", origin);
            context.setVariable("port", port);

            // Use the template engine to process the template
            String htmlContent = templateEngine.process("mail-template/confirmation-account", context);
            helper.setText(htmlContent, true); // Enable HTML content

            javaMailSender.send(message);
        } catch (MessagingException e){
            log.error("Error when sending email: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Async
    public void sendMailResetPassword(Map<String, String> data) {
        log.info("sendMailResetPassword");
        log.info("Sending email request : {}", data);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(data.get("email"));
            helper.setSubject("Đặt lại mật khẩu");
            // Create a Thymeleaf context
            Context context = new Context();
            context.setVariable("username", data.get("username"));
            context.setVariable("pwd", data.get("pwd"));

            // Use the template engine to process the template
            String htmlContent = templateEngine.process("mail-template/reset-password", context);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        }catch (MessagingException e){
            log.error("Error when sending email: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Async
    public void sendMailConfirmOrder(Map<String, Object> data) {

    }
}
