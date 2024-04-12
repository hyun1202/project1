package com.hyun.jobty.domain.blog.domain.id;

import java.io.Serializable;
import java.time.LocalDate;

public class ViewId implements Serializable {
    private Long postSeq;
    private LocalDate date;
    private String ip;
}
