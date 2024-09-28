package com.study.contactapi.mail;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private MailBuilder mailBuilder;

    @Autowired
    private JavaMailSender mailSender;

    public void sendAccountConfirmationMail(String userEmail, String accountConfirmationToken) {
        Context context = new Context();
        context.setVariable("accountConfirmationToken", accountConfirmationToken);

        this.sendEmail("contact-api@mail.com", userEmail, "Account Confirmation", "sendAccountConfirmationMail", context);
    }

    private void sendEmail(String from, String to, String subject, String templateName, Context context) {
        try {
            MimeMessage createdMail = this.mailBuilder.buildMail(from, to, subject, templateName, context);
            this.mailSender.send(createdMail);
        } catch (MessagingException | MailException exception) {
            String mailAction = exception instanceof MessagingException ? "build" : "send";

            String formattedErrorMessage = String.format(
                    """
                                Failed to %s mail
                                Data
                                from: %s, to: %s, subject: %s, template name: %s, context: %s
                                Error: %s
                            """,
                    mailAction, from, to, subject, templateName, context.getVariableNames(), exception.getMessage()
            );

            System.err.println(formattedErrorMessage);
        }
    }
}