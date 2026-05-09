CREATE TABLE IF NOT EXISTS payments_outbox (
    id                  UUID PRIMARY KEY,
    topic               VARCHAR(255) NOT NULL,
    payload             BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS orders_outbox (
    id                  UUID PRIMARY KEY,
    topic               VARCHAR(255) NOT NULL,
    payload             BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS debezium_heartbeat (
    id INT PRIMARY KEY,
    last_heartbeat TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO debezium_heartbeat (id, last_heartbeat) VALUES (1, CURRENT_TIMESTAMP) ON CONFLICT (id) DO NOTHING;

DROP PUBLICATION IF EXISTS dbz_publication;

CREATE PUBLICATION dbz_publication FOR TABLE
       public.payments_outbox,
       public.orders_outbox,
       public.debezium_heartbeat
       WITH (publish='insert,update');