package com.chilly.main_svc.repository;

import com.chilly.main_svc.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}
