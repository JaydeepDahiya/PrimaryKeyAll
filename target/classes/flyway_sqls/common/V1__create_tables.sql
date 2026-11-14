-- Author: Jaydeep Dahiya
-- Create contacts_sequence table
CREATE TABLE contacts_sequence (
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'NEW' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by VARCHAR(20) DEFAULT NULL
    );

-- Create common_seq table - for table Primary key
create table common_seq (
    seq_name VARCHAR(255) NOT NULL,
    seq_value BIGINT
);

-- Create contacts_table table - for table Primary key
CREATE TABLE contacts_table (
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'NEW' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by VARCHAR(20) DEFAULT NULL
    );

-- Create books_table table - for table Primary key
create table books_table (
      id BIGINT NOT NULL PRIMARY KEY,
      isbn VARCHAR(255),
      publisher VARCHAR(255),
      title VARCHAR(255)
);

-- Create authors_table table - for table Primary key
create table authors_table
(
    id bigint NOT NULL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

-- Create contacts_identity table
CREATE TABLE contacts_identity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'NEW' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by VARCHAR(20) DEFAULT NULL
    );

-- Create contacts_uuid table
CREATE TABLE contacts_uuid (
    id VARCHAR(36) not null PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'NEW' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by VARCHAR(20) DEFAULT NULL
    );

-- Create contacts_uuid_binary table
CREATE TABLE contacts_uuid_binary (
    id binary(16) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'NEW' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by VARCHAR(20) DEFAULT NULL
    );

-- Create contacts_natural table
CREATE TABLE contacts_natural (
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL PRIMARY KEY,
    status VARCHAR(20) DEFAULT 'NEW' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by VARCHAR(20) DEFAULT NULL
    );

-- Create contacts_composite table
CREATE TABLE IF NOT EXISTS contacts_composite (
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'NEW' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by VARCHAR(20) DEFAULT NULL,
    PRIMARY KEY(firstname, lastname)
    );

-- Create contacts_composite_embedded table
CREATE TABLE IF NOT EXISTS contacts_composite_embedded (
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'NEW' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by VARCHAR(20) DEFAULT NULL,
    PRIMARY KEY(firstname, lastname)
    );