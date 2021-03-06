package com.jaemin.springdatajpa.repository;

import com.jaemin.springdatajpa.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    // Create
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    // findAll
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // findById
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);

        return Optional.ofNullable(member);
    }

    // findOne
    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    // count
    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    // Delete
    public void delete(Member member) {
        em.remove(member);
    }

    // select by username, age
    public List<Member> findByUsernameAndAgeGreaterThen(String username, int age) {
        return em.createQuery("select m from Member m where m.username=:username and m.age >:age", Member.class)
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    // paging
    public List<Member> findByPaging(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age=:age order by m.username desc", Member.class)
                .setParameter("age", age)
                .setFirstResult(offset) // 시작점
                .setMaxResults(limit) // 최대치
                .getResultList();
    }

    // total count (current / total)
    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age=:age", long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    // bulk 수정 쿼리
    public int bulkAgePlus(int age) {
        return em.createQuery("update Member m set m.age = m.age + 1 where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
    }
}
