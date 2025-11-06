CREATE TABLE tb_tag(
    id UUID NOT NULL PRIMARY KEY,
    coordinate VARCHAR(3) NOT NULL UNIQUE,
    color VARCHAR(20),
    is_available BOOLEAN NOT NULL
);