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
}
