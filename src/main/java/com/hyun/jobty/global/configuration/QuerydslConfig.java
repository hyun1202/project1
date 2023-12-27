package com.hyun.jobty.global.configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class QuerydslConfig {

    private final EntityManager entityManager;

    /**
     * Querydsl을 사용하기 위한 빈 생성 및 객체 반환 <br>
     * 사용법:
     * <pre class="code">
     *     private final JPAQueryFactory queryFactory;
     *     queryFactory.select()...fetch();
     * </pre>
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }
}
