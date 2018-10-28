#Users schema

# --- !Ups

CREATE TABLE User (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    firstName varchar(55) NOT NULL,
    lastName varchar(55) NOT NULL,
    email varchar(255) NOT NULL,
    phoneNumber varchar(55) NOT NULL,
    country varchar(55) NOT NULL,
    city varchar(55) NOT NULL,
    password varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO User ( firstName, lastName, email, phoneNumber, country, city, password)
VALUES ('Mirza', 'Alispahić','malispahic6@gmail.com','123456789','Bosnia and Herzegovina','Zenica','1234');
# --- !Downs

DROP TABLE User;