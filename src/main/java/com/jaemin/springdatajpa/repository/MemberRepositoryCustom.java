package com.jaemin.springdatajpa.repository;

import com.jaemin.springdatajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
