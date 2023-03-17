-- :name- db-create :<!
INSERT INTO Users (username, email, password) VALUES (:username, :email, :password);

-- :name- db-read-all :? :*
SELECT * FROM users;

-- :name- db-read-one-by-username :? :1
SELECT * FROM users WHERE username = :username;
