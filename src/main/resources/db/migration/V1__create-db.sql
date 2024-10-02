ALTER TABLE IF EXISTS account_confirmation_token
   DROP CONSTRAINT IF EXISTS FKbj521pdeykjdpppkts3o7d3c2

ALTER TABLE IF EXISTS contact
   DROP CONSTRAINT IF EXISTS FKq3rrwn2eaywsw94b70ha33ibi

ALTER TABLE IF EXISTS contact_user
   DROP CONSTRAINT IF EXISTS FKavb2vc6hc4ojhgs81y5x6fdvq

DROP TABLE IF EXISTS account_confirmation_token CASCADE

DROP TABLE IF EXISTS contact CASCADE

DROP TABLE IF EXISTS contact_user CASCADE

DROP TABLE IF EXISTS login CASCADE

CREATE TABLE account_confirmation_token (
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    id varchar(255) NOT NULL,
    login_id varchar(255) NOT NULL,
    token varchar(255),
    PRIMARY KEY (id)
)

CREATE TABLE contact (
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    contact_user_id varchar(255) NOT NULL,
    first_name varchar(255),
    id varchar(255) NOT NULL,
    last_name varchar(255),
    phone_number varchar(255),
    PRIMARY KEY (id)
)

CREATE TABLE contact_user (
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    first_name varchar(255),
    id varchar(255) NOT NULL,
    last_name varchar(255),
    login_id varchar(255) UNIQUE,
    PRIMARY KEY (id)
)

CREATE TABLE login (
    account_activated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    email varchar(255) UNIQUE,
    id varchar(255) NOT NULL,
    password varchar(255),
    PRIMARY KEY (id)
)

ALTER TABLE IF EXISTS account_confirmation_token
   ADD CONSTRAINT FKbj521pdeykjdpppkts3o7d3c2
   FOREIGN KEY (login_id)
   REFERENCES login

ALTER TABLE IF EXISTS contact
   ADD CONSTRAINT FKq3rrwn2eaywsw94b70ha33ibi
   FOREIGN KEY (contact_user_id)
   REFERENCES contact_user

ALTER TABLE IF EXISTS contact_user
   ADD CONSTRAINT FKavb2vc6hc4ojhgs81y5x6fdvq
   FOREIGN KEY (login_id)
   REFERENCES login