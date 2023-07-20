package com.example.notatnik.service;

import com.example.notatnik.model.Paczka;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String text, boolean isHtmlContent) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom("krzysztof_kandyba@o2.pl");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, isHtmlContent);
        javaMailSender.send(mimeMessage);
    }
    void sendReminderEmail(Paczka paczka) throws MessagingException {
        String to= paczka.getEmailOdbiorcy();
        String subject= "Przypomnienie dotyczące paczki";
        String text= "Przypominamy, że paczka o kodzie odbioru:" + paczka.getKodOdbioru() +
                "                \" jest wciąż dostępna. Pamiętaj o jej odebraniu przed upływem czasu ważności.\"";


        sendEmail(to,subject,text ,true);
    }
    public void sendEmailWhenPackageIsSending(String adresEmail, String kodOdbioru, boolean isHtmlContent) throws MessagingException {
        sendEmail(adresEmail, "Twój kod odbioru paczki", "Kod odbioru: " + kodOdbioru,isHtmlContent);

    }
}

