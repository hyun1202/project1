package com.hyun.jobty.domain.setting.menu.dto;

import com.hyun.jobty.domain.setting.menu.domain.BlogMainCategory;
import com.hyun.jobty.domain.setting.menu.domain.Menu;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MenuDto {

    /**
     * 메뉴 추가 요청 데이터
     */
    @Getter
    public static class Req {
        @Nullable
        @Schema(description = "상위 메뉴 번호(소메뉴일 때만 사용)")
        Long upper_menu_seq;
        @Schema(description = "대메뉴 분류 번호(대메뉴일 때만 사용)")
        @Nullable
        Long main_category_seq;
        @Nullable
        @Schema(description = "분류명")
        String category_name;
        @Schema(description = "메뉴명")
        String menu_name;
        @Schema(description = "대메뉴에서의 정렬 값")
        int group_no;
        @Schema(description = "소메뉴에서의 정렬 값")
        int sort_no;
        @Schema(description = "0:대메뉴, 1:소메뉴")
        int depth;

        public Req(){}
    }

    /**
     * 메뉴 추가, 수정 응답 데이터
     */
    @Getter
    public static class Res {
        @Schema(description = "메뉴 번호")
        int menu_seq;
        @Schema(description = "상위 메뉴 번호(소메뉴일 때만 사용)")
        Long upper_menu_seq;
        @Schema(description = "대메뉴 분류 번호(대메뉴일 때만 사용)")
        Long main_category_seq;
        @Schema(description = "분류명")
        String category_name;
        @Schema(description = "메뉴명")
        String menu_name;
        @Schema(description = "대메뉴에서의 정렬 값")
        int group_no;
        @Schema(description = "소메뉴에서의 정렬 값")
        int sort_no;
        @Schema(description = "0:대메뉴, 1:소메뉴")
        int depth;

        public Res(Menu menu){
            this.menu_seq = menu.getSeq();
            this.upper_menu_seq = menu.getUpperSeq();
            // 대메뉴(depth 0)의 경우 대메뉴 분류번호 필요
            this.main_category_seq = menu.getDepth() == 0? (long) menu.getMainCategory().getSeq() : null;
            this.category_name = menu.getMenuCategoryName();
            this.menu_name = menu.getName();
            this.group_no = menu.getGroupNo();
            this.sort_no = menu.getSortNo();
            this.depth = menu.getDepth();
        }
    }

    /**
     * 메뉴 업데이트 요청, 응답 데이터
     */
    @Getter
    public static class UpdateReq{
        // 업데이트의 경우 전체 데이터를 가져오기 때문에.....
        // 전체 main 메뉴와 sub 메뉴를 받아 처리해야 한다.
        List<UpdateReq.Menu> menus;
        @Getter
        public static class Menu extends Req{
            @Schema(description = "유저 아이디(이메일)")
            String id;
            @Schema(description = "메뉴 아이디")
            int menu_seq;
            @Schema(description = "소메뉴")
            List<Sub> subs;
        }
    }

    /**
     * 메뉴 조회 응답 데이터
     */
    @Getter
    public static class Read {
        @Schema(description = "대메뉴 번호")
        int menu_seq;
        @Schema(description = "대메뉴 분류명")
        String category_name;
        @Schema(description = "대메뉴명")
        String menu_name;
        @Schema(description = "대메뉴에서의 정렬 값")
        int group_no;
        @Schema(description = "소메뉴")
        List<Sub> subs;

        public Read(Menu menu){
            this.menu_seq = menu.getSeq();
            this.menu_name = menu.getName();
            this.category_name = menu.getMainCategory().getMainCategoryName();
            this.group_no = menu.getGroupNo();
            this.subs = menu.getSub().stream().map(Sub::new).collect(Collectors.toList());
        }

        public Read(int menu_seq, BlogMainCategory category, String menu_name, int group_no, List<Menu> subs){
            this.menu_seq = menu_seq;
            this.category_name = category.getMainCategoryName();
            this.menu_name = menu_name;
            this.group_no = group_no;
            this.subs = subs.stream().map(Sub::new).collect(Collectors.toList());
        }
    }
    @Getter
    public static class Sub {
        @Schema(description = "소메뉴 번호")
        int menu_seq;
        @Schema(description = "소메뉴 분류명")
        String category_name;
        @Schema(description = "소메뉴명")
        String menu_name;
        @Schema(description = "소메뉴에서의 정렬 값")
        int sort_no;
        public Sub(){}
        public Sub(Menu menu){
            this.menu_seq = menu.getSeq();
            this.category_name = menu.getSubCategoryName();
            this.menu_name = menu.getName();
            this.sort_no = menu.getSortNo();
        }
    }
}
