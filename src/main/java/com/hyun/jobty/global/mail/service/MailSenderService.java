package com.hyun.jobty.global.mail.service;

import com.hyun.jobty.global.mail.model.Mail;
import com.hyun.jobty.conf.MailConfig;
import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender javaMailSender;
    private final MailConfig mailConfig;

    @Async
    public void send(Mail mail) {
        // 링크 전송을 위한 호스트 지정
        mail.setAppHost(mailConfig.getLinkHost());
        // 링크 전송을 위한 url 세팅
        mail.setUrlParam();
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            // 전송 대상
            message.addRecipients(Message.RecipientType.TO, mail.getReceiverMail());
            // 제목
            message.setSubject("Jobty " + mail.getSubject());
            // 내용
            message.setText(mail.getMailBody(), mail.getCharset(), mail.getType());
            // 보내는 사람
            message.setFrom(new InternetAddress(mailConfig.getSenderEmail(), mail.getSenderName()));
            // 메일 전송
            javaMailSender.send(message);
            log.info("{} 메일 전송 완료", mail.getReceiverMail());
        } catch (MessagingException | MailException | UnsupportedEncodingException e){
            e.printStackTrace();
            throw new CustomException(ErrorCode.FAIL);
        }
    }
}
