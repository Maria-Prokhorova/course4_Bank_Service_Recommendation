-- liquibase formatted sql

-- changeset Krsasnovvg:1
CREATE TABLE IF NOT EXISTS dynamic_rules
(
    id           UUID PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL
);

-- changeset Krsasnovvg:2
CREATE TABLE IF NOT EXISTS rule_conditions
(
    id       UUID PRIMARY KEY,
    query    TEXT NOT NULL,
    negate   BOOLEAN NOT NULL,
    rule_id  UUID REFERENCES dynamic_rules (id) ON DELETE CASCADE
);

-- changeset Krsasnovvg:3
CREATE TABLE IF NOT EXISTS rule_condition_arguments
(
    rule_condition_id UUID NOT NULL REFERENCES rule_conditions (id) ON DELETE CASCADE,
    arguments TEXT NOT NULL
);