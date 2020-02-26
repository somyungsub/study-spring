package io.ssosso.rest.events;

import io.ssosso.rest.accounts.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id")//  이유: 해쉬코드 모든필드를 기본적으로 사용함 -> 다른 엔티티와 묶음을 만드는것은 좋지 않음.. 상호 참조 때문에 스택오버 플로우 발생이 있음
//@MyEntity // Spring -> 커스텀 애노테이션 구성 후 적용, Lombok은 아직 안되는중.. 줄일 방법이 없음 인지
//@Data // 모든게 포함되어 있고, EqualsHashCode, 상호참조에 의해 스택오버플로우 발생 가능성이 있음. 사용 하지 말 것.
@Entity
public class Event {
  @Id
  private Integer id;
  private String name;
  private String description;
  private LocalDateTime beginEnrollmentDateTime;
  private LocalDateTime closeEnrollmentDateTime;
  private LocalDateTime beginEventDateTime;
  private LocalDateTime endEventDateTime;
  private String location;  // (optional) 이게 없으면 온라인 모임
  private int basePrice;    // (optional)
  private int maxPrice;     // (optional)
  private int limitOfEnrollment;
  private boolean offline;
  private boolean free;
  @Enumerated(EnumType.STRING)
  private EventStatus eventStatus = EventStatus.DRAFT;

  @ManyToOne
  private Account account;


  public void update() {

    // Update Free
    if (this.basePrice == 0 && this.maxPrice == 0) {
      this.free = true;
    } else {
      this.free = false;
    }

    // Update Location
    if (this.location == null || this.location.isBlank()) {
      this.offline = false;
    } else {
      this.offline = true;
    }
  }
}
