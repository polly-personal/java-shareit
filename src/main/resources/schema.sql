drop table if exists users, requests, items, "comments", bookings CASCADE;
CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(100) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  description VARCHAR(512) NOT NULL,
  created TIMESTAMP NOT NULL,
  requester_id BIGINT NOT NULL,
  CONSTRAINT pk_item_request PRIMARY KEY (id),
  CONSTRAINT item_requests_users_fkey FOREIGN KEY(requester_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(512) NOT NULL,
  is_available boolean NOT NULL,
  owner_id BIGINT NOT NULL,
  request_id BIGINT,
  CONSTRAINT pk_item PRIMARY KEY (id),
  CONSTRAINT items_users_fkey FOREIGN KEY(owner_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT items_requests_fkey FOREIGN KEY(request_id) REFERENCES requests(id)
);
CREATE TABLE IF NOT EXISTS comments (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  text VARCHAR(512) NOT NULL,
  item_id BIGINT NOT NULL,
  author_id BIGINT NOT NULL,
  created TIMESTAMP without time zone NOT NULL,
  CONSTRAINT pk_comments PRIMARY KEY (id),
  CONSTRAINT comments_items_fkey FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE,
  CONSTRAINT comments_users_fkey FOREIGN KEY(author_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS bookings (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  start_date TIMESTAMP NOT NULL,
  end_date TIMESTAMP NOT NULL,
  item_id BIGINT NOT NULL,
  booker_id BIGINT NOT NULL,
  status VARCHAR(20) NOT NULL,
  CONSTRAINT pk_booker_id PRIMARY KEY (id),
  CONSTRAINT bookings_items_fkey FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE,
  CONSTRAINT bookings_users_fkey FOREIGN KEY(booker_id) REFERENCES users(id) ON DELETE CASCADE
);