package io.ssosso.springdatajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"}) // 연관관계 필드는 toString x -> 순환참조에 걸릴 수 있음
public class Member {

  @Id @GeneratedValue
  @Column(name = "member_id") // 관례상 테이블_id
  private Long id;
  private String username;
  private int age;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  // JPA -> 기본적으로 ,디폴트 생성자 필수 -> Proxy 기술을 사용할 때 필요하게 됨. private는 안됨 그래서
//  protected Member() {
//  }

  public Member(String username) {
    this.username = username;
  }

  public Member(String username, int age, Team team) {
    this.username = username;
    this.age = age;
    this.team = team;
  }


  public void changeTeam(Team team) {
    this.team = team;
    team.getMembers().add(this);
  }

}
