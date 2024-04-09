CREATE TABLE IF NOT EXISTS customer (
                          id BIGSERIAL PRIMARY KEY,
                          firstname TEXT NOT NULL,
                          lastname TEXT NOT NULL,
                          email TEXT NOT NULL,
                          password TEXT NOT NULL,
                          age INT NOT NULL
);