DELIMITER //

CREATE PROCEDURE create_users(
    IN id bigint,
    IN `age` int,
    IN username varchar(30),
    IN `password` varchar(255),
    IN sur_name varchar(30),
    IN roles varchar(30)
)
BEGIN
    REPLACE INTO users VALUES (id, `age`, username, `password`, sur_name);
    DELETE FROM user_role where id = 1 LIMIT 1000;
    DELETE FROM user_role where id = 1 LIMIT 1000;
    INSERT INTO user_role VALUES (id, roles);

END;
DELIMITER ;