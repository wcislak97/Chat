create database ChatApp;

USE ChatApp;

create table User(
username varchar(255) primary key,
password varchar(100)
);

create table Message(
message_id integer primary key auto_increment,
username varchar(255) not null,
message_body varchar(255),
sent_time datetime,
foreign key (username) references User(username)
);

create table Chat(
chat_id integer primary key auto_increment,
chat_title varchar(100) not null,
last_message_id datetime
);

create table Bad_Words(
word_id integer primary key auto_increment,
bad_word varchar(100) not null,
censor_word varchar(100) not null
);

create table Message_Chat(
message_id integer,
chat_id integer,
foreign key(message_id) references Message(message_id),
foreign key(chat_id) references Chat(chat_id),
primary key(message_id, chat_id)
);

create table Chat_User(
chat_id integer,
username varchar(255),
foreign key(chat_id) references Chat(chat_id),
foreign key(username) references User(username),
primary key(chat_id, username)
);







