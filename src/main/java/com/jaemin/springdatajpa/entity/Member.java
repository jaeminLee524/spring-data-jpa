package com.jaemin.springdatajpa.entity;

import lombok.*;

import javax.persistence.*;

import java.util.Locale;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@NamedQuery(
        name= "Member.findByUsername",
        query= "select m from Member m where m.username=:username"
)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            this.team = team;
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    //연관관계 생성 메서드
    public void changeTeam(Team team) {
        this.team = team;
        team.getMemberList().add(this);
    }
}
