package com.jaemin.springdatajpa.repository;

import com.jaemin.springdatajpa.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
