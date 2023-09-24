CREATE TABLE IF NOT EXISTS loginevents
(
    id               SERIAL PRIMARY KEY,
    uuid             VARCHAR(36)  NOT NULL,
    "timestamp"      TIMESTAMP    NOT NULL,
    "schoolName"     VARCHAR(256) NOT NULL,
    "schoolAddress"  VARCHAR(256) NOT NULL,
    "scraperBaseUrl" VARCHAR(128) NOT NULL,
    symbol           VARCHAR(64)  NOT NULL,
    "loginType"      VARCHAR(32)  NOT NULL
);

ALTER TABLE loginevents ADD CONSTRAINT "Unique event constraint" UNIQUE (uuid);
