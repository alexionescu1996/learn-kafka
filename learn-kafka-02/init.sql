CREATE TABLE invoices(
  id INT PRIMARY KEY NOT NULL,
  title TEXT NOT NULL,
  details CHAR(50),
  billedamt REAL,
  modified TIMESTAMP DEFAULT (STRFTIME('%s', 'now')) NOT NULL
);

INSERT INTO invoices (id,title,details,billedamt)
VALUES (1, 'book', 'Franz Kafka', 500.00 );

