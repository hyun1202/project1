package com.hyun.jobty.global.file.repository;

import com.hyun.jobty.global.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
}
