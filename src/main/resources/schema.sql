CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       first_name VARCHAR(50),
                       father_name VARCHAR(50),
                       username VARCHAR(50) UNIQUE,
                       mobile_number VARCHAR(15),
                       email VARCHAR(50),
                       school_name VARCHAR(50),
                       user_class VARCHAR(20),
                       admission_number VARCHAR(20),
                       password VARCHAR(100)
);

CREATE TABLE quizzes (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(50),
                         questions JSON
);

CREATE TABLE schools (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(50),
                         area VARCHAR(50),
                         type VARCHAR(50)
);

CREATE TABLE results (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(50),
                         score INT
);
