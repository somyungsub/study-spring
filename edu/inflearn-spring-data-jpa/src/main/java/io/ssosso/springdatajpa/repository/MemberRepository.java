package io.ssosso.springdatajpa.repository;

import io.ssosso.springdatajpa.dto.MemberDto;
import io.ssosso.springdatajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
  List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

  List<Member> findTop3MemberBy();

  // Named Query -> @Param은 :username 과 매핑, 실무에서 거의 사용을 안함
//  @Query(name = "Member.findByUsername")  // 생략해도 작동함, 관례 : 엔티티명.메서드명 으로 name을 찾음, 없으면 메서드네임쿼리로 실행
  List<Member> findByUsername(@Param("username") String username);

  // 실무에서 많이씀 -> jpql
  @Query("select m from Member m where m.username = :username and m.age = :age")
  List<Member> findUser(@Param("username") String username, @Param("age") int age);

  @Query("select m.username from Member m")
  List<String> findUsernameList();

  @Query("select new io.ssosso.springdatajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
  List<MemberDto> findMemberDto();

  @Query("select m from Member m where m.username in :names")
  List<Member> findByNames(@Param("names") Collection<String> names);


  List<Member> findListByUsername(String username);         // 컬렉션
  Member findMemberByUsername(String username);             // 단건
  Optional<Member> findOptionalByUsername(String username); // Optional


  // 페이징 및 정렬
  Page<Member> findByAge(int age, Pageable pageable);

  Slice<Member> findSliceByAge(int age, Pageable pageable);

}
