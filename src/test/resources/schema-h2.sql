DROP TABLE IF EXISTS employee CASCADE;
CREATE TABLE employee
(
    employee_id bigint NOT NULL AUTO_INCREMENT,
    `name`      varchar(100) NOT NULL,
    email       varchar(100),
    tel         varchar(100),
    joined      date,
    created_by  varchar(100),
    created_at  timestamp(6),
    modified_by varchar(100),
    modified_at timestamp(6),
    primary key (employee_id)
);