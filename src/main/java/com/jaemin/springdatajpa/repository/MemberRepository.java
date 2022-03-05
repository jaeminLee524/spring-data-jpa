package com.jaemin.springdatajpa.repository;

import com.jaemin.springdatajpa.dto.MemberDto;
import com.jaemin.springdatajpa.entity.Member;
import jdk.jfr.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    @Query("select m from Member m where m.username=:username and m.age=:age")
    List<Member> findMember (@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // dto로 조회하기 위해선 new operation이 꼭 필요함
    @Query("select new com.jaemin.springdatajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // Collection in절
    @Query("select m from Member m where m.username in :names")
    List<Member> frindByNames(@Param("names") Collection<String> names);

    // 컬렉션
    List<Member> findListMemberByUsername(String name);
    // 단건
    Member findByUsername(String name);
    // 단건
    Optional<Member> findOptionalByUsername(String name);

    // 페이징
    // 쿼리가 복잡할 경우 countQuery를 분리해도 된다 ⇒ 성능 테스트를 수행 후 분리!
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
    // 페이징 - Slice
    Slice<Member> findSliceByAge(int age, Pageable pageable);

    @Description("bulk query")
    @Modifying(clearAutomatically = true) //해당 annotaion을 붙여주지 않을 경우 executeUpdate로 인식하지 못함(getSingleResult or getResultList)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Description("fetch join")
    @Query("select m from Member m join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Description("use_Entity_Graph jpql로 join fetch를 안해도됨")
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();
}
