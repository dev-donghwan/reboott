DROP DATABASE IF EXISTS team_reboott_db;
CREATE DATABASE team_reboott_db;
USE team_reboott_db;

-- === [DDL: Table Definitions] ===

CREATE TABLE ai_feature
(
    cost_per_request INTEGER,
    limit_per_unit   INTEGER,
    id               BIGINT NOT NULL AUTO_INCREMENT,
    name             VARCHAR(255),
    limit_unit       ENUM ('PER_MONTH', 'PER_REQUEST'),
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE ai_feature_bundle
(
    _id  BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    PRIMARY KEY (_id)
) ENGINE = InnoDB;

CREATE TABLE ai_feature_bundle_mapping
(
    _id           BIGINT NOT NULL AUTO_INCREMENT,
    ai_feature_id BIGINT,
    bundle_id     BIGINT,
    PRIMARY KEY (_id),
    FOREIGN KEY (ai_feature_id) REFERENCES ai_feature (id),
    FOREIGN KEY (bundle_id) REFERENCES ai_feature_bundle (_id)
) ENGINE = InnoDB;

CREATE TABLE company_credit
(
    _id    BIGINT NOT NULL AUTO_INCREMENT,
    amount BIGINT,
    PRIMARY KEY (_id)
) ENGINE = InnoDB;

CREATE TABLE company
(
    _id       BIGINT NOT NULL AUTO_INCREMENT,
    name      VARCHAR(255),
    bundle_id BIGINT,
    credit_id BIGINT UNIQUE,
    PRIMARY KEY (_id),
    FOREIGN KEY (bundle_id) REFERENCES ai_feature_bundle (_id),
    FOREIGN KEY (credit_id) REFERENCES company_credit (_id)
) ENGINE = InnoDB;

CREATE TABLE ai_feature_usage
(
    _id          BIGINT  NOT NULL AUTO_INCREMENT,
    input_length INTEGER NOT NULL,
    company__id  BIGINT,
    feature_id   BIGINT,
    used_at      DATETIME(6),
    used_credit  BIGINT  NOT NULL,
    PRIMARY KEY (_id),
    FOREIGN KEY (company__id) REFERENCES company (_id),
    FOREIGN KEY (feature_id) REFERENCES ai_feature (id)
) ENGINE = InnoDB;

-- === [DML: Initial Data Inserts] ===

-- Step 1: Insert features
INSERT INTO ai_feature (id, name, cost_per_request, limit_per_unit, limit_unit)
VALUES (1, 'AI 번역', 10, 2000, 'PER_REQUEST'),
       (2, 'AI 교정', 10, 1000, 'PER_REQUEST'),
       (3, 'AI 뉘앙스 조절', 20, 1500, 'PER_REQUEST'),
       (4, 'AI 초안 작성', 50, 200, 'PER_MONTH');

-- Step 2: Insert bundles
INSERT INTO ai_feature_bundle (_id, name)
VALUES (1, 'A 번들'),
       (2, 'B 번들'),
       (3, 'C 번들');

-- Step 3: Map features to bundles
INSERT INTO ai_feature_bundle_mapping (bundle_id, ai_feature_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (3, 3),
       (3, 4);

-- Step 4: Insert credits
INSERT INTO company_credit (_id, amount)
VALUES (1, 10000),
       (2, 10000),
       (3, 10000);

-- Step 5: Insert companies (linking bundle and credit)
INSERT INTO company (_id, name, bundle_id, credit_id)
VALUES (1, 'A사', 1, 1),
       (2, 'B사', 2, 2),
       (3, 'C사', 3, 3);