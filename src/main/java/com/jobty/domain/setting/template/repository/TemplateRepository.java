package com.jobty.domain.setting.template.repository;

import com.jobty.domain.setting.template.domain.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Integer> {

}
