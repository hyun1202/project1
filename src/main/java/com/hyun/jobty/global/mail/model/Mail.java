package com.hyun.jobty.global.mail.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Mail {
    private String receiverMail;
    private String senderName = "Jobty";
    private String subject;
    private String body;
    private String link_msg = "이 링크는 발급 후 5분 동안만 유효합니다. 링크에 문제가 있거나 다른 문제가 발생하면 언제든지 고객 센터로 연락주시기 바랍니다.";
    private String charset = "utf-8";
    private String type = "html";
    private String appHost;
    private String url;

    @Builder
    public Mail(String receiverMail, String senderName, String subject, String body, String url){
        this.receiverMail = receiverMail;
        this.senderName = senderName;
        this.subject = subject;
        this.body = body;
        this.url = url;
    }

    public void setAppHost(String appHost) {
        this.appHost = appHost;
    }

    public String getMailBody(){
        String str1 = """
                <html>
                                
                <body class="main full-padding" style="margin: 0;padding: 0;-webkit-text-size-adjust: 100%%;">
                    <table class="wrapper" style="border-collapse: collapse;table-layout: fixed;min-width: 320px;width: 100%%;background-color: #fafafa;" cellpadding="0" cellspacing="0" role="presentation">
                        <tbody>
                            <tr>
                                <td>
                                    <div class="layout one-col fixed-width stack" style="Margin: 0 auto;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000%% - 167400px);overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;">
                                        <div class="layout__inner" style="border-collapse: collapse;display: table;width: 100%%;background-color: #fafafa;">
                                            <div class="column" style="text-align: left;color: #595959;font-size: 14px;line-height: 21px;font-family: Lato,Tahoma,sans-serif;">
                                                <div style="Margin-left: 20px;Margin-right: 20px;Margin-top: 24px;">
                                                    <div style="mso-line-height-rule: exactly;line-height: 5px;font-size: 1px;">&nbsp;</div>
                                                </div>
                                                <div style="Margin-left: 20px;Margin-right: 20px;">
                                                    <div style="mso-line-height-rule: exactly;mso-text-raise: 11px;vertical-align: middle;">
                                                        <h1 class="size-24" style="Margin-top: 0;Margin-bottom: 0;font-style: normal;font-weight: normal;color: #b8bdc9;font-size: 20px;line-height: 28px;text-align: left;" lang="x-size-24">
                                                            <!-- 메일 제목 -->
                                                            <font color="#8690a8">%s</font>
                                                        </h1>
                                                        <p class="size-17" style="Margin-top: 20px;Margin-bottom: 20px;font-size: 17px;line-height: 26px;" lang="x-size-17">
                                                            <!-- 메일 내용 -->
                                                            <span style="text-decoration: inherit;">%s</span></p>
                                                    </div>
                                                </div>
                                                <div style="Margin-left: 20px;Margin-right: 20px;">
                                                    <div style="mso-line-height-rule: exactly;line-height: 20px;font-size: 1px;">&nbsp;</div>
                                                </div>""";
        String str2 = """
                                                <!-- 링크 -->
                                                <div style="Margin-left: 20px;Margin-right: 20px;">
                                                    <div class="btn btn--flat btn--large" style="Margin-bottom: 20px;text-align: left;">
                                                        <a style="border-radius: 4px;display: inline-block;font-size: 14px;font-weight: bold;line-height: 24px;padding: 12px 24px;text-align: center;text-decoration: none !important;transition: opacity 0.1s ease-in;color: #ffffff !important;background-color: #6b7489;font-family: Lato, Tahoma, sans-serif;" 
                                                        href="%s/%s">메일 인증</a>
                                                    </div>
                                                </div>
                                                <div style="Margin-left: 20px;Margin-right: 20px;">
                                                    <div style="mso-line-height-rule: exactly;line-height: 20px;font-size: 1px;">&nbsp;</div>
                                                </div>
                                                <div style="Margin-left: 20px;Margin-right: 20px;Margin-bottom: 24px;">
                                                    <div style="mso-line-height-rule: exactly;mso-text-raise: 11px;vertical-align: middle;">
                                                        <p class="size-17" style="Margin-top: 0;Margin-bottom: 0;font-size: 17px;line-height: 26px;" lang="x-size-17">이 링크는 발급 후 30분 동안만 유효합니다. 링크에 문제가 있거나 다른 문제가 발생하면 언제든지 고객 센터로 연락주시기 바랍니다.</p>
                                                    </div>
                                                </div>
                """;
        String str3 =  """
                                                    <div style="Margin-left: 20px;Margin-right: 20px;">
                                                            <div style="mso-line-height-rule: exactly;mso-text-raise: 11px;vertical-align: middle;">
                                                                <p class="size-17" style="Margin-top: 20px;Margin-bottom: 20px;font-size: 17px;line-height: 26px;" lang="x-size-17">
                                                                    <span style="text-decoration: inherit;">감사합니다.</span>
                                                                </p>
                                                            </div>
                                                    </div>
                                                </div>
                                            </div>
                                            </div>
                                            <div role="contentinfo">
                                                <div style="line-height:4px;font-size:4px;" id="footer-top-spacing">&nbsp;</div>
                                                <div class="layout email-flexible-footer email-footer" style="Margin: 0 auto;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000% - 167400px);overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;" dir="rtl" id="footer-content">
                                                    <div class="layout__inner right-aligned-footer" style="border-collapse: collapse;display: table;width: 100%;">
                                                        <table style="border-collapse: collapse;table-layout: fixed;display: inline-block;width: 600px;" cellpadding="0" cellspacing="0">
                                                            <tbody>
                                                                <tr>
                                                                    <td>
                                                                        <div class="column js-footer-additional-info" style="text-align: right;font-size: 12px;line-height: 19px;color: #c2c2c2;font-family: Lato,Tahoma,sans-serif;width: 600px;" dir="ltr">
                                                                            <div style="margin-left: 0;margin-right: 0;Margin-top: 10px;Margin-bottom: 10px;">
                                                                                <div class="email-footer__additional-info" style="font-size: 12px;line-height: 19px;margin-bottom: 18px;margin-top: 0px;">
                                                                                    <div bind-to="address">
                                                                                        <p class="email-flexible-footer__additionalinfo--center" style="Margin-top: 0;Margin-bottom: 0;">Jobty</p>
                                                                                    </div>
                                                                                </div>
                                                                                <div class="email-footer__additional-info" style="font-size: 12px;line-height: 19px;margin-bottom: 15px;Margin-top: 18px;">
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div style="line-height:40px;font-size:40px;" id="footer-bottom-spacing">&nbsp;</div>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </body>
                                        
                        </html>
                        """;
        return str1.formatted(this.subject, this.body) + (url!= null? str2.formatted(this.appHost, this.url): "") + str3;
    }
}
