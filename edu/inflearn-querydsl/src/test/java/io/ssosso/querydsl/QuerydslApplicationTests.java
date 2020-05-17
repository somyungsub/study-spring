package io.ssosso.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.ssosso.querydsl.entity.Hello;
import io.ssosso.querydsl.entity.QHello;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@Commit
class QuerydslApplicationTests {

  @Autowired
  EntityManager em;

  @Test
  void contextLoads() {
    Hello hello = new Hello();
    em.persist(hello);

    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    QHello qHello = new QHello("h");

    Hello result = queryFactory
            .selectFrom(qHello)
            .fetchOne();

    assertThat(result).isEqualTo(hello);
    assertThat(result.getId()).isEqualTo(hello.getId());

  }

}
