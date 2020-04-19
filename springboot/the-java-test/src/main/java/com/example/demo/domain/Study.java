package com.example.demo.domain;


import com.example.demo.study.StudyStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
public class Study {

  @Id
  @GeneratedValue
  private Long id;

  private StudyStatus status = StudyStatus.DRAFT;

  @Column(name = "limit_count")
  private int limit;

  private String name;

  @Temporal(TemporalType.TIMESTAMP)
  private Date startDate;
//  private LocalDateTime startDate;

  @ManyToOne(cascade = CascadeType.ALL)
  private Member member;

  public Study(int limit, String name) {
    this.limit = limit;
    this.name = name;
  }

  public Study() {
  }

  public Study(int limit) {
    if (limit < 0) {
      throw new IllegalArgumentException("limit는 0보다 커야한다.");
    }
    this.limit = limit;
  }

  @Override
  public String toString() {
    return "Study{" +
            "status=" + status +
            ", limit=" + limit +
            ", name='" + name + '\'' +
            '}';
  }


  public void setOwner(Member member) {
    this.member = member;
  }

  public void open() {
    this.status = StudyStatus.OPENED;
    this.startDate = new Date();
//    this.startDate = LocalDateTime.now();
    System.out.println("Study open !!!");
  }
}
