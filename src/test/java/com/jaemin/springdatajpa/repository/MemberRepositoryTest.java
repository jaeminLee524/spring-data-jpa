package com.jaemin.springdatajpa.repository;

import com.jaemin.springdatajpa.dto.MemberDto;
import com.jaemin.springdatajpa.entity.Member;
import com.jaemin.springdatajpa.entity.Team;
import jdk.jfr.Description;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext EntityManager em;

    @Test
    public void testMember() {
        System.out.println("memberRepository = " + memberRepository.getClass());

        Member member = new Member("memberA");

        Member saveMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);


    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        // 리스트 조회 검증 - size
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 개수 검증 - count
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();

        usernameList.stream().forEach(s -> System.out.println("s = " + s));
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);


        List<MemberDto> memberDto = memberRepository.findMemberDto();
        memberDto.stream().forEach(dto -> System.out.println("dto = " + dto));
    }

    @Description("Collection In절 테스트")
    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> usernameList = memberRepository.frindByNames(Arrays.asList("AAA", "BBB"));

        usernameList.stream().forEach(s -> System.out.println("username = " + s));
    }

    @Description("반환 타입 테스트")
    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findListMemberByUsername("asdsbs");

        // 불필요한 코드
        //if(result != null) {
            // 블라블라
        //}

        System.out.println("result.size() = " + result.size());
    }

    @Description("springDataJpa-paging_Test")
    @Test
    public void paging() {
        memberRepository.save(new Member("mem1", 10));
        memberRepository.save(new Member("mem2", 10));
        memberRepository.save(new Member("mem3", 10));
        memberRepository.save(new Member("mem4", 10));
        memberRepository.save(new Member("mem5", 10));
        memberRepository.save(new Member("mem6", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        // total count query까지 실행
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

        // 절대 Entity를 그대로 노출해선 안된다 => dto로 변환해야 함
        Slice<MemberDto> EntitytoDto = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //then
        // paging으로 갖고온 데이터 추출
        List<Member> content = page.getContent();
        // totalCount
        //long totalElements = page.getTotalElements(); //Slice에는 데이터를 갖고오지 않음

        content.stream().forEach(m -> System.out.println("member = " + m));
        //System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        // 전체 데이터 개수
        //assertThat(totalElements).isEqualTo(6);
        assertThat(page.getNumber()).isEqualTo(0);
        //assertThat(page.getTotalPages()).isEqualTo(2); //Slice는 총 개수를 갖고오지 않음 limit + 1
        // 첫번재 페이지 체크
        assertThat(page.isFirst()).isTrue();
        // 다음 페이지 존재여부 체크
        assertThat(page.hasNext()).isTrue();
    }

    @Description("bulk_update_test")
    @Test
    public void bulkUpdate() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 41));

        //when
        int resultCount = memberRepository.bulkAgePlus(20); //bulk 연산 수행
        em.flush(); // 혹시 남아있는, 변경되지 않은게 db에 반영
        em.clear(); // 1차 캐시를 비움(영속성 컨텍스트)

        Member member5 = memberRepository.findByUsername("member5");
        System.out.println("member5 = " + member5);

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Description("fetch join")
    @Test
    public void findMemberLazy() {
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findAll(); // N + 1 => EntityGraph(attri~team)을 통해 fetch join 가능
//        List<Member> members = memberRepository.findMemberFetchJoin();

        members.stream().forEach( m -> {
            System.out.println("m.getUsername() = " + m.getUsername());
            System.out.println("m.getTeam().getClass() = " + m.getTeam().getClass());
            System.out.println("m.getTeam().getName() = " + m.getTeam().getName());
        });

    }

    @Description("JPA Hint")
    @Test
    public void queryHint() {
        Member member1 = new Member("member1", 10);// 영속성 컨텍스트에 저장
        memberRepository.save(member1);
        em.flush(); //보통 transaction commit 시점에 flush가 일어나지만 강제로 flush
        em.clear();

        Member findMember = memberRepository.findById(member1.getId()).get();
        findMember.setUsername("member2");

        em.flush();
    }

}