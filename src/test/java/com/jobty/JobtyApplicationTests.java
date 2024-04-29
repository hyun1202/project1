package com.jobty;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:application-dev.yaml")
@SpringBootTest(properties = "spring.profiles.active:dev")
public class JobtyApplicationTests {

//    @Autowired
//    private ApplicationContext applicationContext;
//
//    @DisplayName("빈 목록 확인")
//    @Test
//    public void contextLoads() throws Exception {
//        if (applicationContext != null) {
//            String[] beans = applicationContext.getBeanDefinitionNames();
//
//            for (String bean : beans) {
//                System.out.println("bean : " + bean);
//            }
//        }
//    }
//
//    @DisplayName("암호화 테스트")
//    @Test
//    public void EncTest(){
//        try {
//            String str = "hello";
//            String encStr = CipherUtil.encrypt(CipherUtil.NORMAL, str);
//            String decStr = CipherUtil.decrypt(CipherUtil.NORMAL, encStr);
//
//            System.out.println("str: " + str);
//            System.out.println("encStr: " + encStr);
//            System.out.println("decStr: " + decStr);
//
//            str = "fdgahasnbsahd124";
//            encStr = CipherUtil.encrypt(CipherUtil.NORMAL, str);
//            decStr = CipherUtil.decrypt(CipherUtil.NORMAL, encStr);
//
//            System.out.println("str: " + str);
//            System.out.println("encStr: " + encStr);
//            System.out.println("decStr: " + decStr);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void exceptionTest(){
//        throw new CustomException(ErrorCode.FAIL);
//    }
}
