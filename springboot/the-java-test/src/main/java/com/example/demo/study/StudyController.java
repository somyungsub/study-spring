package com.example.demo.study;

import com.example.demo.domain.Study;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/study")
public class StudyController {

  @Autowired
  StudyRepository repository;

  @GetMapping("/{id}")
  public Study getStudy(@PathVariable Long id) {
    return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Study not found " + id));
  }

  @PostMapping
  public Study createStudy(@RequestBody Study study) {
    return repository.save(study);
  }
}
