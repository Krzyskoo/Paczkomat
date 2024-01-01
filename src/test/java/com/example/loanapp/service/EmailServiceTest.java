package com.example.loanapp.service;

import com.example.loanapp.model.Packs;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;


import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


import static org.mockito.Mockito.*;

@SpringBootTest
@ComponentScan(basePackages = "com.example.loanapp.service")
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void shouldSendEmail() throws MessagingException {
        // GIVEN (Zakładając)
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        String to = "test@example.com";
        String text = "Testowa wiadomość";
        String subject = "Temat testowej wiadomości";
        emailService.sendEmail(to, text, subject);

        //WHEN
        ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender).send(mimeMessageCaptor.capture());
        MimeMessage capturedMimeMessage = mimeMessageCaptor.getValue();
        InternetAddress toAddress = (InternetAddress) capturedMimeMessage.getRecipients(Message.RecipientType.TO)[0];

        //then
        assertThat(capturedMimeMessage.getSubject()).isEqualTo(subject);
        assertThat(toAddress.getAddress()).isEqualTo(to);

    }
    @Test
    void shouldSendEmailWhenPackIsSending() throws Exception {
        //GIVEN
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        String email = "test@example.com";
        String pickupCode ="123asd";
        emailService.sendEmailWhenPackIsSending(email,pickupCode);
        //when
        ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender).send(mimeMessageCaptor.capture());
        MimeMessage capturedMimeMessage = mimeMessageCaptor.getValue();
        InternetAddress toAddress = (InternetAddress) capturedMimeMessage.getRecipients(Message.RecipientType.TO)[0];
        String capturedText = getTextFromMimeMessage(capturedMimeMessage);
        //then
        assertThat(capturedMimeMessage.getSubject()).isEqualTo("Kod odbioru paczki");
        assertThat(toAddress.getAddress()).isEqualTo(email);
        assertThat(capturedText).isEqualTo("Twoj kod odbiuru paczki to: " + pickupCode);


    }

    @Test
    void sendReminderEmail() throws Exception {
        //given
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        Packs packs = new Packs();
        packs.setEmailReceiver("test@example.com");
        packs.setId(123L);
        emailService.sendReminderEmail(packs);

        //when
        ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender).send(mimeMessageCaptor.capture());
        MimeMessage capturedMimeMessage = mimeMessageCaptor.getValue();
        InternetAddress toAddress = (InternetAddress) capturedMimeMessage.getRecipients(Message.RecipientType.TO)[0];
        String capturedText = getTextFromMimeMessage(capturedMimeMessage);

        // then
        assertThat(capturedMimeMessage.getSubject()).isEqualTo("Przypomnienie o paczce");
        assertThat(toAddress.getAddress()).isEqualTo(packs.getEmailReceiver());
        assertThat(capturedText).isEqualTo("Twoja paczka o numerze" + packs.getId() + " wygasa za 3 dni");


    }
    private String getTextFromMimeMessage(MimeMessage mimeMessage) throws Exception {
        Object content = mimeMessage.getContent();
        if (content instanceof String) {
            return (String) content;
        } else if (content instanceof MimeMultipart) {
            return getTextFromMimeMultipart((MimeMultipart) content);
        }
        return null;
    }
    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        StringBuilder textBuilder = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            if (mimeMultipart.getBodyPart(i).getContent() instanceof MimeMultipart) {
                textBuilder.append(getTextFromMimeMultipart((MimeMultipart) mimeMultipart.getBodyPart(i).getContent()));
            } else {
                textBuilder.append(mimeMultipart.getBodyPart(i).getContent());
            }
        }
        return textBuilder.toString();
    }
}