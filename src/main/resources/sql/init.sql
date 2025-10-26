create database if not exists project_spring_db_study;


show databases;

select * from information_schema.SCHEMA_PRIVILEGES;

GRANT ALL PRIVILEGES ON project_spring_db_study.* TO 'admin'@'%';
flush privileges;