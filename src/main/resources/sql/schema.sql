DROP DATABASE IF EXISTS team_reboott_db;
CREATE DATABASE IF NOT EXISTS team_reboott_db;

-- === [DDL: Table Definitions] ===
USE team_reboott_db;

CREATE TABLE IF NOT EXISTS ai_feature
(
    cost_per_request INTEGER,
    limit_per_unit   INTEGER,
    id               BIGINT NOT NULL AUTO_INCREMENT,
    name             VARCHAR(255),
    limit_unit       ENUM ('PER_MONTH', 'PER_REQUEST'),
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS ai_feature_bundle
(
    _id  BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    PRIMARY KEY (_id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS ai_feature_bundle_mapping
(
    _id           BIGINT NOT NULL AUTO_INCREMENT,
    ai_feature_id BIGINT,
    bundle_id     BIGINT,
    PRIMARY KEY (_id),
    FOREIGN KEY (ai_feature_id) REFERENCES ai_feature (id),
    FOREIGN KEY (bundle_id) REFERENCES ai_feature_bundle (_id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS company_credit
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

CREATE TABLE IF NOT EXISTS ai_feature_usage
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