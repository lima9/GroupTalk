drop database if exists grouptalkdb;
create database grouptalkdb;

use grouptalkdb;

CREATE TABLE users (
    id BINARY(16) NOT NULL,
    loginid VARCHAR(15) NOT NULL UNIQUE,
    password BINARY(16) NOT NULL,
	email VARCHAR(255) NOT NULL,
    fullname VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user_roles (
    userid BINARY(16) NOT NULL,
    role ENUM ('registered', 'admin'),
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    PRIMARY KEY (userid, role)
);

CREATE TABLE auth_tokens (
    userid BINARY(16) NOT NULL,
    token BINARY(16) NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    PRIMARY KEY (token)
);

CREATE TABLE groups(
	id BINARY(16) NOT NULL,
	name VARCHAR(256) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE users_groups(
	userid BINARY(16) NOT NULL,
	groupid BINARY(16) NOT NULL,
	FOREIGN KEY (userid) REFERENCES users(id),
	FOREIGN KEY (groupid) REFERENCES groups(id),
	PRIMARY KEY (userid, groupid)
);

CREATE TABLE themes (
    id BINARY(16) NOT NULL,
    userid BINARY(16) NOT NULL,
    groupid BINARY(16) NOT NULL,
    title VARCHAR(100) NOT NULL,
    content VARCHAR(500) NOT NULL,
    last_modified TIMESTAMP NOT NULL,
    creation_timestamp DATETIME not null default current_timestamp,
    FOREIGN KEY (userid) REFERENCES users(id),
    FOREIGN KEY (groupid) REFERENCES groups(id),
    PRIMARY KEY (id)
);

CREATE TABLE response (
    id BINARY(16) NOT NULL,
    themeid BINARY(16) NOT NULL,
    content VARCHAR(500) NOT NULL,
    userid BINARY(16) NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id), 
    FOREIGN KEY (themeid) REFERENCES themes(id),
    PRIMARY KEY (id)
);

insert into users (id, loginid, password, email, fullname) values (UNHEX(REPLACE(UUID(),'-','')), 'admin', UNHEX(MD5('admin')), 'admin@admin.com', 'admin1');
select @userid:=id from users where loginid = 'admin';
insert into user_roles (userid, role) values (@userid, 'admin');
insert into users (id, loginid, password, email, fullname) values (UNHEX(REPLACE(UUID(),'-','')), 'user', UNHEX(MD5('user')), 'user@user.com', 'user1');
select @userid:=id from users where loginid = 'user';
insert into user_roles (userid, role) values (@userid, 'registered');
insert into groups(id, name) values (UNHEX(REPLACE(UUID(),'-','')),'unicos');
select @groupid:=id from groups where name = 'unicos';
insert into themes (id, userid, groupid, title, content) values (UNHEX(REPLACE(UUID(),'-','')),@userid,@groupid,'1r tema','Tema super entretenido');

