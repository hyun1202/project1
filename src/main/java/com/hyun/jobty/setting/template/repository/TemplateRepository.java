package com.hyun.jobty.setting.template.repository;

import com.hyun.jobty.setting.template.domain.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Integer> {

}
