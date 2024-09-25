CREATE
    EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE times
(
    time_id serial PRIMARY KEY,
    minutes INT NOT NULL
);

INSERT INTO times (minutes)
VALUES (15),
       (30),
       (60),
       (120),
       (180),
       (600);

CREATE TABLE types
(
    type_id     serial PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(100) NOT NULL
);

INSERT INTO types (name, description)
VALUES ('Детский', 'До 12 лет'),
       ('Взрослый', 'От 12 лет'),
       ('Взрослый + Детский', 'До 6 лет');

CREATE TABLE tickets
(
    ticket_id serial PRIMARY KEY,
    time_id   INT NOT NULL,
    FOREIGN KEY (time_id) REFERENCES times (time_id),
    type_id   INT NOT NULL,
    FOREIGN KEY (type_id) REFERENCES types (type_id)
);

INSERT INTO tickets (time_id, type_id)
VALUES (3, 1),
       (4, 1),
       (5, 1),
       (6, 1),
       (3, 2),
       (4, 2),
       (5, 2),
       (6, 2),
       (3, 3),
       (4, 3),
       (5, 3),
       (6, 3);

CREATE TABLE institutions
(
    institution_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    ru_name        varchar(50)  NOT NULL,
    en_name        varchar(50)  NOT NULL,
    address        varchar(100) NOT NULL,
    link           varchar(50)  not null
);

CREATE TABLE coupons
(
    coupon_id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    institution_id  UUID         NOT NULL,
    FOREIGN KEY (institution_id) REFERENCES institutions (institution_id),
    coupon_code     VARCHAR(50)  NOT NULL,
    discount_amount INTEGER      NOT NULL,
    expiration_date DATE,
    description     VARCHAR(100) NOT NULL,
    used            INT              DEFAULT 0
);

CREATE TABLE institution_tickets
(
    institution_tickets_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    institution_id         UUID    NOT NULL,
    FOREIGN KEY (institution_id) REFERENCES institutions (institution_id),
    ticket_id              INTEGER NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets (ticket_id),
    price                  INT,
    is_active              BOOLEAN          DEFAULT true,
);

CREATE TABLE statuses
(
    status_id serial PRIMARY KEY,
    name      varchar(50) UNIQUE NOT NULL
);

INSERT INTO statuses (name)
VALUES ('paid'),
       ('using'),
       ('used');

CREATE TABLE paid_person
(
    person_id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    email     varchar(50)  NOT NULL,
    full_name varchar(100) NOT NULL
);

CREATE TABLE orders
(
    order_id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    paid_person_id UUID,
    FOREIGN KEY (paid_person_id) REFERENCES paid_person (person_id),
    institution_id UUID             DEFAULT NULL,
    status_id      INT NOT NULL,
    FOREIGN KEY (status_id) REFERENCES statuses (status_id),
    date_paid      TIMESTAMP        DEFAULT NOW(),
    date_changed   TIMESTAMP
);

CREATE TABLE order_items
(
    order_item_id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id               UUID NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (order_id),
    institution_tickets_id UUID NOT NULL,
    FOREIGN KEY (institution_tickets_id) REFERENCES institution_tickets (institution_tickets_id),
    tickets_count          INT  NOT NULL
);

CREATE TABLE roles
(
    role_id   serial PRIMARY KEY,
    role_name varchar(25)
);

INSERT INTO roles (role_name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_SITE'),
       ('ROLE_LOCALSERVER');

CREATE TABLE employees
(
    employee_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    role_id     INT                 NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles (role_id),
    name        VARCHAR(50)         NOT NULL,
    password    VARCHAR(255)        NOT NULL,
    email       VARCHAR(100) UNIQUE NOT NULL
);