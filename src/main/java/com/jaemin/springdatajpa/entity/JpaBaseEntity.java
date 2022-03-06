package com.jaemin.springdatajpa.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass // 해당 클래스를 상속 받는 객체는 해당 클래스의 속성들만 상속받음
public class JpaBaseEntity {

    @Column(updatable = false) //실수로 변경해도 DB에 값이 변경되지 않음
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist // insert 전 이벤트 발생
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now;
        this.updatedDate = now;
    }

    @PreUpdate // update 전 이벤트 발생
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

}
