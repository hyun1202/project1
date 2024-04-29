package com.jobty.domain.blog.domain;

import com.jobty.domain.blog.domain.id.ViewId;
import com.jobty.domain.member.domain.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor @Builder
@Entity @Table(name = "post_view")
@Getter
@IdClass(ViewId.class)
public class View extends Timestamped {
    @Id @Column(name = "post_seq")
    private Long postSeq;
    @Id @Column(name = "view_date")
    private LocalDate date;
    @Id
    private String ip;

    public View() {

    }
}
