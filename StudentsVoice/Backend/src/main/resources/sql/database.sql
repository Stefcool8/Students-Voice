-- Create roles table
DROP TABLE IF EXISTS roles CASCADE;
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Create users table
DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create users_roles table
DROP TABLE IF EXISTS users_roles CASCADE;
CREATE TABLE users_roles (
    username VARCHAR(100) NOT NULL, -- Reference to users.username
    role_code VARCHAR(50) NOT NULL, -- Reference to roles.code
    PRIMARY KEY (username, role_code),
    FOREIGN KEY (username) REFERENCES users (username) ON DELETE CASCADE,
    FOREIGN KEY (role_code) REFERENCES roles (code) ON DELETE CASCADE
);


-- Create evaluations table
DROP TABLE IF EXISTS evaluations CASCADE;
CREATE TABLE evaluations (
    id SERIAL PRIMARY KEY,
    registration_number UUID NOT NULL UNIQUE,
    teacher_id INT NOT NULL,
    activity_name VARCHAR(255) NOT NULL,
    activity_type VARCHAR(50) NOT NULL CHECK (activity_type IN ('COURSE', 'SEMINAR')),
    grade INT NOT NULL CHECK (grade BETWEEN 1 AND 10),
    comment TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    student_id INT NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Create evaluation_time_range table
DROP TABLE IF EXISTS time_range CASCADE;
CREATE TABLE time_range (
    id SERIAL PRIMARY KEY,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    CHECK (start_time < end_time)
);


-- Insert default roles
INSERT INTO roles (code, name) VALUES
    ('ADMIN', 'Administrator'),
    ('STUDENT', 'Student'),
    ('TEACHER', 'Teacher');

-- Insert default users
INSERT INTO users (username, email, password, name) VALUES
    ('admin', 'admin@example.com', 'admin123', 'Admin'),
    ('teacher1', 'teacher1@example.com', 'teach123', 'Teacher 1'),
    ('teacher2', 'teacher2@example.com', 'teach123', 'Teacher 2'),
    ('teacher3', 'teacher3@example.com', 'teach123', 'Teacher 3'),
    ('student1', 'student1@example.com', 'stud123', 'Student 1'),
    ('student2', 'student2@example.com', 'stud123', 'Student 2'),
    ('student3', 'student3@example.com', 'stud123', 'Student 3');

-- Insert default users_roles
INSERT INTO users_roles (username, role_code)
VALUES ('admin', 'ADMIN'),
       ('teacher1', 'TEACHER'),
       ('teacher2', 'TEACHER'),
       ('teacher3', 'TEACHER'),
       ('student1', 'STUDENT'),
       ('student2', 'STUDENT'),
       ('student3', 'STUDENT');

-- Insert default evaluation time range
INSERT INTO time_range (start_time, end_time)
VALUES ('2025-01-15 00:00:00', '2025-02-15 00:00:00');

-- Insert default evaluations
INSERT INTO evaluations (registration_number, teacher_id, activity_name, activity_type, grade, comment, student_id) VALUES
    ('fda9f785-5c69-4159-9d61-5b186bdd0569', 2, 'Java', 'SEMINAR', 10, 'Teacher is helping the students with their homework. That''s nice :)', 5),
    ('50e6886c-a99d-467d-8a37-e077dda4d4a0', 3, 'FMSE', 'COURSE', 10, 'Nice explanations!', 6),
    ('48f8975f-069e-48e1-b1b4-3be7366b4081', 4, 'TAIP', 'SEMINAR', 8, 'Eh... :))', 7),
    ('df005287-eed9-40bf-a6ab-a649fe87a476', 2, 'Java', 'COURSE', 10, 'Very nice course!', 5),
    ('3aef9e0d-1ee0-42dc-a40c-6e405252e31e', 3, 'FMSE', 'SEMINAR', 10, 'Perfection!', 6),
    ('11ed48dc-f137-48d8-821a-f3ceeb2afe80', 4, 'TAIP', 'COURSE', 9, 'Nice!', 7);


SELECT * FROM roles;
SELECT * FROM users;
SELECT * FROM users_roles;
SELECT * FROM evaluations;
SELECT * FROM time_range;


-- Update the evaluations time range
UPDATE time_range SET start_time = '2025-01-15 00:00:00', end_time = '2025-02-15 00:00:00' WHERE id = 1;


-- CREATE EXTENSION pgcrypto;
--
-- -- Hash the passwords with SHA-256
UPDATE users SET password = '$2a$10$GD5fHzyCHW.Ea8GWCeGoQuSlj8g7hRovAsfYrUOhaXn1e2d7PsLmG' WHERE username = 'admin';
UPDATE users SET password = '$2a$10$lxixIGIweWRzfEVotieNkuBRIy6s7p2DEubPfbMzSMtjwNwc.SkIy' WHERE username = 'teacher1';
UPDATE users SET password = '$2a$10$lxixIGIweWRzfEVotieNkuBRIy6s7p2DEubPfbMzSMtjwNwc.SkIy' WHERE username = 'teacher2';
UPDATE users SET password = '$2a$10$lxixIGIweWRzfEVotieNkuBRIy6s7p2DEubPfbMzSMtjwNwc.SkIy' WHERE username = 'teacher3';
UPDATE users SET password = '$2a$10$nu7xugfPRqKqn8rds0Nsb.eVq7zB29Q0H2sEuMVpvNmxXEv7NWIj2' WHERE username = 'student1';
UPDATE users SET password = '$2a$10$nu7xugfPRqKqn8rds0Nsb.eVq7zB29Q0H2sEuMVpvNmxXEv7NWIj2' WHERE username = 'student2';
UPDATE users SET password = '$2a$10$nu7xugfPRqKqn8rds0Nsb.eVq7zB29Q0H2sEuMVpvNmxXEv7NWIj2' WHERE username = 'student3';
