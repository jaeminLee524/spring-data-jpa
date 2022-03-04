package com.jaemin.springdatajpa.repository;

import com.jaemin.springdatajpa.dto.MemberDto;
import com.jaemin.springdatajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
