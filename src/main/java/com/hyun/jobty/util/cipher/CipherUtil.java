package com.hyun.jobty.util.cipher;

import com.hyun.jobty.conf.property.ApplicationContextProvider;
import com.hyun.jobty.conf.property.GlobalProperty;
import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;

import java.util.HashMap;

public class CipherUtil{
    public static final int NORMAL = 0;
    public static final int ADMIN = 1;
    public static final int SUPER = 2;
    public static final int ID = 3;

    private static final String plus = "+";
    private static final String equal = "=";
    private static final String slash = "/";

    private static final HashMap<String, String> special_char_replace = new HashMap<>() {{
       put(plus, "-");
       put(equal, "_");
       put(slash, ".");
    }};

    public static String encrypt(int keyType, String text){
        try {
            String encodeText = new AES256(getKey(keyType)).encrypt(text);
            return urlEncoding(encodeText);
        }catch (Exception e){
            throw new CustomException(ErrorCode.EncError);
        }
    }

    public static String decrypt(int keyType, String cipherText){
        try {
            String decodeText = urlDecoding(cipherText);
            return new AES256(getKey(keyType)).decrypt(decodeText);
        }catch (Exception e){
            throw new CustomException(ErrorCode.DecError);
        }
    }

    // base64의 +=/ 특수문자 대체
    public static String urlEncoding(String text){
        StringBuilder encodeText = new StringBuilder();
        for (int i=0; i<text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == plus.charAt(0))
                encodeText.append(special_char_replace.get(plus));
            else if (ch == equal.charAt(0))
                encodeText.append(special_char_replace.get(equal));
            else if (ch == slash.charAt(0))
                encodeText.append(special_char_replace.get(slash));
            else
                encodeText.append(text.charAt(i));
        }
        return encodeText.toString();
    }

    public static String urlDecoding(String text){
        StringBuilder encodeText = new StringBuilder();
        for (int i=0; i<text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == special_char_replace.get(plus).charAt(0))
                encodeText.append(plus);
            else if (ch == special_char_replace.get(equal).charAt(0))
                encodeText.append(equal);
            else if (ch == special_char_replace.get(slash).charAt(0))
                encodeText.append(slash);
            else
                encodeText.append(text.charAt(i));
        }
        return encodeText.toString();
    }

    private static String getKey(int keyType){
        GlobalProperty.Jobty.Key key = ApplicationContextProvider.getBean("globalProperty.Jobty.Key", GlobalProperty.Jobty.Key.class);
        if (keyType == NORMAL)
            return key.getNormal_key();
        if (keyType == ADMIN)
            return key.getAdmin_key();
        if (keyType == SUPER)
            return key.getSuper_key();
        if (keyType == ID)
            return key.getId_key();
        throw new CustomException(ErrorCode.IncorrectKeyType);
    }
}
