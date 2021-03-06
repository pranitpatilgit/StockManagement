DROP TABLE IF EXISTS Stock;

CREATE TABLE Stock (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  price DOUBLE default 0.00,
  CREATED_AT TIMESTAMP,
  LAST_MODIFIED_AT TIMESTAMP,
  VERSION INT
);

INSERT INTO Stock (id, name, price, CREATED_AT, LAST_MODIFIED_AT, VERSION) VALUES
  (1, 'Google', 10.00 , CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
  (2, 'Facebook', 20.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
  (3, 'Confluent', 30.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
  (4, 'Apple', 12.34, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
  (5, 'TATA', 44.67, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
