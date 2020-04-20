package com.example.demo.study;

import com.example.demo.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {
  Optional<Study> findById(Long id);
}
