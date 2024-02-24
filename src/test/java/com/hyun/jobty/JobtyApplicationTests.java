package com.hyun.jobty;

import com.hyun.jobty.util.cipher.CipherUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:application-dev.yaml")
@SpringBootTest(properties = "spring.profiles.active:dev")
class JobtyApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @DisplayName("빈 목록 확인")
    @Test
    public void contextLoads() throws Exception {
        if (applicationContext != null) {
            String[] beans = applicationContext.getBeanDefinitionNames();

            for (String bean : beans) {
                System.out.println("bean : " + bean);
            }
        }
    }

    @DisplayName("암호화 테스트")
    @Test
    public void EncTest(){
        try {
            String str = "hello";
            String encStr = CipherUtil.encrypt(CipherUtil.NORMAL, str);
            String decStr = CipherUtil.decrypt(CipherUtil.NORMAL, encStr);

            System.out.println("str: " + str);
            System.out.println("encStr: " + encStr);
            System.out.println("decStr: " + decStr);

            str = "fdgahasnbsahd124";
            encStr = CipherUtil.encrypt(CipherUtil.NORMAL, str);
            decStr = CipherUtil.decrypt(CipherUtil.NORMAL, encStr);

            System.out.println("str: " + str);
            System.out.println("encStr: " + encStr);
            System.out.println("decStr: " + decStr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
