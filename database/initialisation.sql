CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE statuses
(
    status_id   serial PRIMARY KEY,
    status_name varchar(15) NOT NULL
);

INSERT INTO statuses (status_name)
VALUES ('Lead'),
       ('In work'),
       ('Consultaion'),
       ('Save for later'),
       ('Student'),
       ('Archive');

CREATE TABLE branches
(
    branch_id      serial PRIMARY KEY,
    branch_name    varchar(256) NOT NULL,
    branch_address varchar(256) NOT NULL
);

INSERT INTO branches (branch_name, branch_address)
VALUES ('Rehovot', 'Rehovot,Herzl st.1-25'),
       ('Haifa', 'Haifa,Herzl st.1-25'),
       ('Tel Aviv', 'Tel Aviv,Herzl st.1-25');

CREATE TABLE courses
(
    course_id           uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    course_name         varchar(256) NOT NULL,
    course_abbreviation varchar(8)   NOT NULL
);

INSERT INTO courses (course_name, course_abbreviation)
VALUES ('Full stack development', 'FSD'),
       ('Quality assurance', 'QA'),
       ('Base Programming', 'BP');

CREATE TABLE contacts
(
    contact_id       uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    contact_name     varchar(256) NOT NULL,
    phone            int          NOT NULL,
    email            varchar(256) NOT NULL,
    status_id        int          NOT NULL,
    FOREIGN KEY (status_id) REFERENCES statuses (status_id),
    branch_id        int          NOT NULL,
    FOREIGN KEY (branch_id) REFERENCES branches (branch_id),
    target_course_id int          NOT NULL,
    FOREIGN KEY (target_course_id) REFERENCES courses (course_id),
    comment          varchar(256)
);

CREATE TABLE students_information
(
    contact_id     uuid PRIMARY KEY,
    FOREIGN KEY (contact_id) REFERENCES contacts (contact_id) ON DELETE CASCADE,
    full_payment   int     NOT NULL,
    documents_done boolean NOT NULL
);

CREATE TABLE payment_types
(
    payment_type_id   serial PRIMARY KEY,
    payment_type_name varchar(256) NOT NULL
);

INSERT INTO payment_types (payment_type_name)
VALUES ('Credit card'),
       ('Cash'),
       ('Cheque'),
       ('Online transfer');

CREATE TABLE payment_information
(
    payment_id      uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    contact_id      uuid,
    FOREIGN KEY (contact_id) REFERENCES students_information (contact_id) ON DELETE CASCADE,
    payment_date    date NOT NULL,
    payment_type_id int  NOT NULL,
    FOREIGN KEY (payment_type_id) REFERENCES payment_types (payment_type_id),
    payment_amount  int  NOT NULL,
    payment_details varchar(256)
);

CREATE TABLE groups
(
    group_id         uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    group_name       varchar(25)  NOT NULL,
    start_date       date         NOT NULL,
    finish_date      date         NOT NULL,
    isActive         boolean      NOT NULL,
    course_id        int          NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses (course_id),
    slack_link       varchar(256) NOT NULL,
    whats_app_link   varchar(256) NOT NULL,
    skype_link       varchar(256) NOT NULL,
    deactivate_after boolean      NOT NULL
);

CREATE TABLE students_by_group
(
    contact_id uuid NOT NULL,
    FOREIGN KEY (contact_id) REFERENCES students_information (contact_id),
    group_id   uuid NOT NULL,
    FOREIGN KEY (group_id) REFERENCES groups (group_id),
    PRIMARY KEY (contact_id, group_id)
);

CREATE TABLE lecturers
(
    lecturer_id   uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    lecturer_name varchar(256) NOT NULL,
    phone         int          NOT NULL,
    email         varchar(256) NOT NULL,
    branch_id     int          NOT NULL,
    FOREIGN KEY (branch_id) REFERENCES branches (branch_id),
    comment       varchar(256)
);

CREATE TABLE lecturers_by_group
(
    lecturer_id uuid NOT NULL,
    FOREIGN KEY (lecturer_id) REFERENCES lecturers (lecturer_id),
    group_id    uuid NOT NULL,
    FOREIGN KEY (group_id) REFERENCES groups (group_id),
    PRIMARY KEY (lecturer_id, group_id)
);

CREATE TABLE weekdays
(
    weekday_id   serial PRIMARY KEY,
    weekday_name varchar(10) NOT NULL
);

INSERT INTO weekdays (weekday_name)
VALUES ('Monday'),
       ('Tuesday'),
       ('Wednesday'),
       ('Thursday'),
       ('Friday'),
       ('Saturday'),
       ('Sunday');

CREATE TABLE webinars_by_weekday
(
    group_id   uuid NOT NULL,
    FOREIGN KEY (group_id) REFERENCES groups (group_id),
    weekday_id int  NOT NULL,
    FOREIGN KEY (weekday_id) REFERENCES weekdays (weekday_id),
    PRIMARY KEY (group_id, weekday_id)
);

CREATE TABLE lessons_by_weekday
(
    group_id   uuid NOT NULL,
    FOREIGN KEY (group_id) REFERENCES groups (group_id),
    weekday_id int  NOT NULL,
    FOREIGN KEY (weekday_id) REFERENCES weekdays (weekday_id),
    PRIMARY KEY (group_id, weekday_id)
);


