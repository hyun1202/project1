package com.jobty.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 접속한 클라이언트의 ip를 가져온다.
 */
public class ClientUtils {
    private static String ip;
    public static void setRemoteIP(HttpServletRequest request){
        String ip = request.getHeader("X-FORWARDED-FOR");

        //proxy 환경일 경우
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        //웹로직 서버일 경우
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr() ;
        }
        ClientUtils.ip = ip;
    }

    public static String getIp() {
        return ip;
    }
}
