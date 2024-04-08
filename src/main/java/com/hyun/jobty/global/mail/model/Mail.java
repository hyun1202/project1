package com.hyun.jobty.global.mail.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Mail {
    private final String html_link_str = "link9lKita1lXyQ";
    private String receiverMail;
    private String senderName = "Jobty";
    private String subject;
    private String content;
    private String charset = "utf-8";
    private String type = "html";
    private String appHost;
    private String url;
    private List<UrlParam> urlParams;

    @Builder
    public Mail(String receiverMail, String subject, String content, String url, List<UrlParam> urlParams){
        this.receiverMail = receiverMail;
        this.subject = subject;
        this.content = content;
        this.url = url;
        this.urlParams = urlParams;
    }

    public void setAppHost(String appHost) {
        this.appHost = appHost;
    }

    public String getMailBody() {
        // 메일 제목, 내용, 인증 내용 포함
        String str1 = """
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                	<meta charset="UTF-8">
                	<meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body style="margin: 0;padding: 0;-webkit-text-size-adjust: 100%%;">
                	<table class="wrapper" style="border-collapse: collapse;table-layout: fixed;min-width: 320px;width: 100%%;background-color: #fff;" cellpadding="0" cellspacing="0" role="presentation">
                	<tbody>
                		<tr>
                			<td>
                				<div style="Margin: 0 auto;max-width: 600px;min-width: 320px; width: 320px;width: calc(28000%% - 167400px);overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;">
                					<div style="border-collapse: collapse;display: table;width: 100%%;background-color: #fff;">
                						<div style="text-align: left;color: #595959;font-size: 14px;line-height: 21px;font-family: 'Apple SD Gothic Neo','맑은고딕','Malgun Gothic','돋움',Dotum,'굴림',gulim,sans-serif;">
                							<div style="Margin-left: 20px;Margin-right: 20px;Margin-top: 24px;">
                								<div style="mso-line-height-rule: exactly;line-height: 5px;font-size: 1px;">&nbsp;</div>
                							</div>
                							<div style="mso-line-height-rule: exactly;mso-text-raise: 11px;vertical-align: middle;">
                								<h1 style="Margin-top: 0;Margin-bottom: 0; padding: 24px 0; box-sizing: border-box; font-style: normal;font-weight: 500;color: #333; background: #fff; font-size: 14px;line-height: 28px;text-align: left;" lang="x-size-24">
                										<img src="cid:logo" height="24px" alt="logo">
                								</h1>
                								<div>                                \s
                										<!-- 메일 제목 1 -->
                										<span style="display: block; font-weight: 700; color: #3A147C; font-size: 22px; letter-spacing: -1.5px;">%s</span>
                								</div>
                								<div style="Margin-top: 20px;Margin-bottom: 20px; padding-top: 20px; font-size: 16px;line-height: 25px; letter-spacing: -1.5px;" >
                										<!-- 메일 내용 2 -->
                										<span style="text-decoration: inherit; display: block;">안녕하세요. 블로그 개설 홈페이지 JOBTY 입니다.</span>
                										<span style="text-decoration: inherit; display: block;"><span style="color: #2BDFAE; letter-spacing: 0; font-weight: 500;">%s</span> %s</span>
                								</div>
                """;
        String str2 = """
                                                <div style="Margin-bottom: 20px;text-align: left; width: 100%%;">
                                                    <!-- 링크 2 -->
                                                    <a style="width: 100%%; max-width: 412px; border-radius: 5px;display: inline-block;font-size: 16px;font-weight: normal;line-height: 24px;padding: 12px 0;text-align: center;text-decoration: none !important;transition: opacity 0.1s ease-in;color: #ffffff !important;background-color: #3A147C;font-family: Lato, Tahoma, sans-serif; letter-spacing: -1.5px;" 
                                                    href="%s/%s">이메일 인증하기</a>
                                                </div>
                """;
        String str3 = """
                                                <div style="Margin-bottom: 24px; ">
                                        									<div style="mso-line-height-rule: exactly;mso-text-raise: 11px;vertical-align: middle; letter-spacing: -1.5px;">
                                        											<p style="Margin-top: 0;Margin-bottom: 10px;font-size: 14px;line-height: 16px; font-weight: 400; color: #999;" >가입을 시도하지 않으셨나요? <a style="color: #007AD2; text-decoration: none;" href="#">온라인 고객센터 바로가기</a></p>
                                        									</div>
                                        								</div>
                                        								<div>
                                        									<div style="mso-line-height-rule: exactly;mso-text-raise: 11px;vertical-align: middle;">
                                        											<p style="Margin-top: 50px;Margin-bottom: 10px;font-size: 16px;line-height: 26px; color: #666; font-weight: 600; letter-spacing: -1px;" lang="x-size-16">
                                        													<span style="text-decoration: inherit;"><span style="letter-spacing: 0;">JOBTY</span> 팀 드림</span>
                                        											</p>
                                        											<p style="Margin-top: 10px;Margin-bottom: 10px;font-size: 14px;line-height: 21px; color: #999; font-weight: 400; " >
                                        													<span style="text-decoration: inherit;">The Blogging Site, JOBTY</span>
                                        											</p>
                                        											<p style="Margin-top: 10px;Margin-bottom: 10px;font-size: 14px;line-height: 21px; color: #999; font-weight: 400; " >
                                        													<span style="text-decoration: inherit;"><a style="color: #007AD2; text-decoration: none;" href="">https://jobty.co.kr</a></span>
                                        											</p>
                                        									</div>
                                        								</div>
                                        							</div>
                                        						</div>
                                        					</div>
                                        				</div>
                                        			</td>
                                        		</tr>
                                        	</tbody>
                                        </table>
                                        	
                                        </body>
                                        </html>
                        """;
        String name = "";
        String newContent = this.content;
        String link_html = "";
        if (this.url != null){
            name = this.receiverMail;
            newContent = "고객님, 아래 버튼 클릭 하여 이메일 인증을 완료해주세요.";
            link_html = str2.formatted(this.appHost, this.url);
        }
        return str1.formatted(this.subject, name, newContent) + link_html + str3;
    }

    public void setUrlParam(){
        // 파라미터가 있을 때 새로 url 생성
        if (this.urlParams != null) {
            String newUrl = this.url + "?";
            for (UrlParam param : this.urlParams) {
                newUrl = newUrl + param.getKey() + "=" + param.getValue() + "&";
            }
            // 문자열 마지막(&) 자르기
            this.url = newUrl.substring(0, newUrl.length()-1);
        }
    }

    public String getFullUrl(){
        if (this.urlParams != null){
            return this.appHost + "/" + this.url;
        }
        return this.appHost;
    }
}
