package com.hyun.jobty.setting.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyun.jobty.domain.setting.menu.domain.Menu;
import com.hyun.jobty.domain.setting.menu.repository.MenuCustomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@TestPropertySource(locations = "classpath:application-dev.yaml")
@SpringBootTest(properties = "spring.profiles.active:dev")
@AutoConfigureMockMvc
public class MenuControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MenuCustomRepository menuCustomRepository;

    @BeforeEach
    public void mockMvcSetup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @DisplayName("querydsl main, sub메뉴 order 테스트")
    @Test
    public void customRepositoryTest(){
        List<Menu> menus = menuCustomRepository.findAllMenuOrderMenuSeqByDomain("apfhd0257");
        System.out.println(menus);
    }

}
