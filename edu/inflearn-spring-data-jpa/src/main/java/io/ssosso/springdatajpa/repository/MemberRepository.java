package io.ssosso.springdatajpa.repository;

import io.ssosso.springdatajpa.dto.MemberDto;
import io.ssosso.springdatajpa.dto.UsernameOnlyDto;
import io.ssosso.springdatajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member>{
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

  // 반환타입 + @ (doc 찾아보기)
  List<Member> findListByUsername(String username);         // 컬렉션
  Member findMemberByUsername(String username);             // 단건
  Optional<Member> findOptionalByUsername(String username); // Optional


  // 페이징 및 정렬
  Page<Member> findByAge(int age, Pageable pageable);
  Slice<Member> findSliceByAge(int age, Pageable pageable);

  @Query(value = "select m from Member m left join m.team t",
          countQuery = "select count(m) from Member m")
  Page<Member> findQueryByAge(int age, Pageable pageable);

  // bulk update
  @Modifying(clearAutomatically = true)  // 꼭 넣어줘야함 벌크성 delete, update
  @Query("update Member m set m.age = m.age+1 where m.age >= :age")
  int bulkAgePlus(@Param("age") int age);

  // 페치조인 -> 즉시로딩화
  @Query("select m from Member m left join fetch m.team")
  List<Member> findMemberFetchJoin();

  // 엔티티그래프
  @Override
  @EntityGraph(attributePaths = {"team"})
  List<Member> findAll();

  @EntityGraph(attributePaths = {"team"})
  @Query("select m from Member m")
  List<Member> findMemberEntityGraph();

  @EntityGraph(attributePaths = {"team"})
  List<Member> findEntityGraphByUsername(@Param("username") String username);

  @EntityGraph("Member.all")  // 잘 사용하지 않음
  List<Member> findEntityGraphNamedByUsername(@Param("username") String username);

  // JPA 힌트 , 잘쓰진 않음, 조회 트래픽이 미친듯이 많아.... -> 다른기술 적용 선택하는게 더 빠를지도..
  @QueryHints(value= @QueryHint( name = "org.hibernate.readOnly", value = "true"))
  Member findReadOnlyByUsername(String username);

  // Lock
  // select for update
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  List<Member> findLockByUsername(String username);

  // Projection
  List<UsernameOnly> findProjectionByUsername(String username);

  List<UsernameOnlyDto> findProjectionDtoByUsername(@Param("username") String username);

  <T> List<T> findProjectionDtoByUsername(@Param("username") String username, Class<T> type);

  // native 쿼리
  @Query(value = "select * from member where username = ?", nativeQuery = true)
  Member findByNativeQuery(String username);

  @Query(value = "select m.member_id as id, m.username, t.name as teamName " +
          "from member as m left join team t ",
          countQuery="select count(*) from member",
          nativeQuery=true)
  Page<MemberProjection> findByNativeProjection(Pageable pageable);

}
