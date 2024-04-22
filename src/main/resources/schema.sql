CREATE TABLE IF NOT EXISTS t_user(
    id varchar(255) PRIMARY KEY,
    login varchar(255) UNIQUE,
    password varchar(255) NOT NULL,
    balance decimal(19, 2) NOT NULL
);