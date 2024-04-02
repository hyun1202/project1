package com.hyun.jobty.global.mail.service;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.conf.MailConfig;
import com.hyun.jobty.global.mail.model.Mail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
            // 이미지와 첨부파일 전송을 위해 사용
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
            // 받는 사람 세팅
            mimeMessageHelper.addTo(mail.getReceiverMail());
            // 보내는 사람 세팅
            mimeMessageHelper.setFrom(new InternetAddress(mailConfig.getSenderEmail(), mail.getSenderName()));
            // 제목 작성
            mimeMessageHelper.setSubject("Jobty " + mail.getSubject());
            // 내용 작성
            mimeMessageHelper.setText(mail.getMailBody(), true);
            // 이미지 삽입
            mimeMessageHelper.addInline("logo", new ClassPathResource("static/images/logo_mail.png"));
            // 메일 전송
            javaMailSender.send(message);
            log.info("{} 메일 전송 완료", mail.getReceiverMail());
        } catch (MessagingException | MailException | UnsupportedEncodingException e){
            e.printStackTrace();
            throw new CustomException(ErrorCode.FAIL);
        }
    }
}
