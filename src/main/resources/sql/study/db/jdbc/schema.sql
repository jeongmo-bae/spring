drop table if exists project_spring_db_study.member cascade; -- 참조 외래키(foreign key) 제약조건도 함께 제거하고 삭제

create table project_spring_db_study.member
(
    member_id varchar(10),
    money     integer not null default 0,
    primary key (member_id)
);
