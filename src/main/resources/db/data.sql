insert into user_tb(username, password, email, created_at) values('ssar', '$2a$10$uXAk9sIYsX8laN4YslGc2uH2rIXNcz.LHoYw5SesYd1UpD8OltYde', 'ssar@nate.com', now());
insert into user_tb(username, password, email, created_at) values('cos', '$2a$10$uXAk9sIYsX8laN4YslGc2uH2rIXNcz.LHoYw5SesYd1UpD8OltYde', 'cos@nate.com', now());

insert into board_tb(title, content, user_id, created_at) values('제목1', '내용1', 1, now());
insert into board_tb(title, content, user_id, created_at) values('제목2', '내용2', 1, now());
insert into board_tb(title, content, user_id, created_at) values('제목3', '내용3', 1, now());
insert into board_tb(title, content, user_id, created_at) values('제목4', '내용4', 2, now());