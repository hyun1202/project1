package com.hyun.jobty.domain.setting.menu.repository;

import com.hyun.jobty.domain.setting.menu.domain.Menu;
import com.hyun.jobty.domain.setting.menu.domain.QMenu;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MenuCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    QMenu main = new QMenu("main");
    QMenu sub = new QMenu("sub");

    /**
     * 서브메뉴 정렬을 위한 쿼리 작성
     * @param domain 도메인
     * @return 메인, 서브 모두 seq로 오름차순 정렬된 메뉴
     */
    public List<Menu> findAllMenuOrderMenuSeqByDomain(String domain) {
        return jpaQueryFactory
                .select(main)
                .distinct()
                .from(main)
                .leftJoin(main.sub, sub)
                .fetchJoin()
                .where(
                        main.main.isNull(),
                        main.setting.domain.eq(domain)
                )
                .orderBy(main.seq.asc(), sub.seq.asc())
                .fetch();
    }
}
