USE crypto_wallet;

CREATE TABLE asset_history (
    id             BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
    asset_name     VARCHAR(1024),
    exchange_name  VARCHAR(1024),
    holding_amount FLOAT,
    jpy            FLOAT,
    date           DATE
);

CREATE TABLE job_completed (
    id            BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
    exchange_name VARCHAR(1024),
    date          DATE
);