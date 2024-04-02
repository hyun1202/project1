package com.hyun.jobty.domain.setting.menu.repository;

import com.hyun.jobty.domain.setting.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Optional<Menu> findBySetting_Domain(String domain);
    /**
     * 메뉴 리스트 조회
     * @param domain 도메인
     * @return 해당 도메인의 메뉴 조회
     */
    Optional<List<Menu>> findAllBySetting_DomainAndUpperSeqIsNullOrderBySeq(String domain);
    /**
     * 상위 메뉴가 존재하는지 확인
     * @param domain 도메인
     * @param upperSeq 메뉴 분류 번호
     * @return 상위 메뉴 존재 여부(true:있음, false:없음)
     */
    boolean existsBySetting_DomainAndSeqAndUpperSeqIsNull(String domain, int upperSeq);
    /**
     * 하위 메뉴 존재하는지 확인
     * @param domain 도메인
     * @param lowerSeq 하위 메뉴 분류 번호
     * @return 하위 메뉴 존재 여부
     */
    boolean existsBySetting_DomainAndUpperSeq(String domain, int lowerSeq);
    /**
     * 정렬 변경을 위한 그룹에 해당한 메뉴 조회
     * @param domain 도메인
     * @param groupNo 그룹 번호
     * @return 그룹에 해당하는 메뉴 리스트
     */
    Optional<Menu> findBySetting_DomainAndGroupNoAndUpperSeqIsNull(String domain, int groupNo);
}
