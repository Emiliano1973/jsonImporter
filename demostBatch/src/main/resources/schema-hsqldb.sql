create table if not exists events (
  event_id       varchar(255) not null,
  alert          boolean      not null,
  event_duration bigint       not null,
  host           varchar(255),
  type           varchar(255),
  primary key (event_id)
);
