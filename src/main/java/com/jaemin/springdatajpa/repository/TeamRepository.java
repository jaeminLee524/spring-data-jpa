package com.jaemin.springdatajpa.repository;

import com.jaemin.springdatajpa.entity.Team;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamRepository {

    @PersistenceContext
    private EntityManager em;

    // create
    public Team save(Team team) {
        em.persist(team);

        return team;
    }

    // findAll
    public List<Team> findAll() {
        return em.createQuery("select t from Team t", Team.class)
                .getResultList();
    }

    // findById
    public Optional<Team> findById(Long id) {
        Team team = em.find(Team.class, id);

        return Optional.ofNullable(team);
    }

    // delete
    public void delete(Team team) {
        em.remove(team);
    }

    // count
    public long count() {
        return em.createQuery("select count(t) from Team t", Long.class)
                .getSingleResult();
    }
}
