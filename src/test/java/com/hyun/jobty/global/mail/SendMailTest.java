package com.hyun.jobty.global.mail;

import com.hyun.jobty.global.mail.model.Mail;
import com.hyun.jobty.global.mail.model.UrlParam;
import com.hyun.jobty.global.mail.service.MailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.concurrent.TimeUnit;

@TestPropertySource(locations = "classpath:application-dev.yaml")
@SpringBootTest(properties = "spring.profiles.active:dev")
public class SendMailTest {
    @Autowired
    private MailSenderService mailSenderService;

    @Test
    public void SendMail() throws InterruptedException {
        String id = "id123412";
        String token = "12234token";
        String receiver = "";
        mailSenderService.send(
                Mail.builder()
                        .receiverMail(receiver)
                        .subject("이메일 주소 인증")
                        .url("signup")
                        .urlParams(List.of(new UrlParam("id", id), new UrlParam("token", token)))
                        .build()
        );
        TimeUnit.SECONDS.sleep(30);
    }

    @Test
    public void strFormat(){
//        String url = "url?token=1234!";
        String url = null;
        String str2 = """
                                                    <!-- 링크 -->
                                                    <div style="Margin-left: 20px;Margin-right: 20px;">
                                                        <div class="btn btn--flat btn--large" style="Margin-bottom: 20px;text-align: left; width: 100%;">
                                                            <a style="width: 412px; border-radius: 5px;display: inline-block;font-size: 16px;font-weight: normal;line-height: 24px;padding: 12px 0;text-align: center;text-decoration: none !important;transition: opacity 0.1s ease-in;color: #ffffff !important;background-color: #3A147C;font-family: Lato, Tahoma, sans-serif; letter-spacing: -1.5px;" href="%s/%s">이메일 인증하기</a>
                                                        </div>
                                                    </div>
                """;

        str2 = str2.formatted("this.subject", "this.content");
        System.out.println(str2);
        System.out.printf("%s/%s\nhwllo;", "TarrifType", "AnnualCost");
    }
}
