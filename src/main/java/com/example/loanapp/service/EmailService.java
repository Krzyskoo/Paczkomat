package com.example.loanapp.service;

import com.example.loanapp.model.Packs;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to,String text, String subject) throws MessagingException{
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setFrom("krzysztof_kandyba@o2.pl");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, true);
        javaMailSender.send(mimeMessage);
    }
    public void sendEmailWhenPackIsSending(String email, String pickupCode) throws MessagingException{
        sendEmail(email, "Twoj kod odbiuru paczki to: " + pickupCode, "Kod odbioru paczki");
    }

    public void sendReminderEmail(Packs packs) throws MessagingException {
        sendEmail(packs.getEmailReceiver(),
                "Twoja paczka o numerze" + packs.getId()+" wygasa za 3 dni",
                "Przypomnienie o paczce");
    }

}
