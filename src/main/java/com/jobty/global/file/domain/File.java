package com.jobty.global.file.domain;

import com.jobty.domain.member.domain.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "file")
public class File extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_seq")
    private int seq;
    @Column(name = "post_seq")
    private Long postSeq;
    @Column(name = "ori_file_name")
    private String oriFileName;
    @Column(name = "save_file_name")
    private String saveFileName;
    @Column(name = "file_path")
    private String filePath;
    @Builder
    public File(int seq, Long postSeq, String oriFileName, String saveFileName, String filePath){
        this.seq = seq;
        this.postSeq = postSeq;
        this.oriFileName = oriFileName;
        this.saveFileName = saveFileName;
        this.filePath = filePath;
    }
}
