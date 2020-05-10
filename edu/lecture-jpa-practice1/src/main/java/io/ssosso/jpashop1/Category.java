package io.ssosso.jpashop1;

import io.ssosso.jpashop1.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {
  @Id
  @GeneratedValue
  @Column(name = "catrgory_id")
  private Long id;

  private String name;

  // 실무에서는 쓰지 말것...
  @ManyToMany
  @JoinTable(name = "category_item",
     joinColumns = @JoinColumn(name = "category_id"),
      inverseJoinColumns = @JoinColumn(name = "item_id")
  )  // 중간 테이블 연결 필요
  private List<Item> items = new ArrayList<>();

  // 순환 참조 테이블
  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent")
  private List<Category> child = new ArrayList<>();

}
