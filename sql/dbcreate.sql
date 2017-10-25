DROP TABLE users;
DROP TABLE groups;
DROP TABLE users_groups;

CREATE TABLE users(
  user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  login VARCHAR(45) NOT NULL UNIQUE
);
CREATE TABLE groups(
  group_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NOT NULL UNIQUE
);
CREATE TABLE users_groups(
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  group_id INT NOT NULL,
  PRIMARY KEY (id, user_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY(group_id) REFERENCES groups(group_id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO users VALUES(DEFAULT, 'ivanov');
INSERT INTO users VALUES(DEFAULT, 'sidorov');
INSERT INTO users VALUES(DEFAULT, 'petrov');
INSERT INTO users VALUES(DEFAULT, 'pupkin');
INSERT INTO users VALUES(DEFAULT, 'санин');
INSERT INTO users VALUES(DEFAULT, 'сусанин');

INSERT INTO groups (group_id, name) VALUES(DEFAULT, 'teamA');
INSERT INTO groups (group_id, name) VALUES(DEFAULT, 'teamB');
INSERT INTO groups (group_id, name) VALUES(DEFAULT, 'teamC');


INSERT INTO users_groups VALUES(DEFAULT, 1, 2);
INSERT INTO users_groups VALUES(DEFAULT, 1, 3);
INSERT INTO users_groups VALUES(DEFAULT, 2, 1);
INSERT INTO users_groups VALUES(DEFAULT, 2, 2);
INSERT INTO users_groups VALUES(DEFAULT, 3, 1);
INSERT INTO users_groups VALUES(DEFAULT, 4, 2);
INSERT INTO users_groups VALUES(DEFAULT, 5, 3);
INSERT INTO users_groups VALUES(DEFAULT, 6, 1);
INSERT INTO users_groups VALUES(DEFAULT, 6, 2);

SELECT * FROM users;
SELECT * FROM groups;
SELECT * FROM users_groups ORDER BY id;

