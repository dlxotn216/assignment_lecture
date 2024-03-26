drop table if exists app_lecture;
create table app_lecture
(
  lecture_key          bigint auto_increment not null,
  name                 varchar(255)          not null,
  trainer_name         varchar(255)          not null,
  location             varchar(512)          not null,
  max_trainee_count    int                   not null,
  remain_trainee_count int                   not null,
  released_at          datetime(6)           not null,

  created_at           datetime(6)           not null,
  created_by           bigint                not null,
  modified_at          datetime(6)           not null,
  modified_by          bigint                not null,
  primary key (lecture_key)
);
create index app_lecture_ix_01 on app_lecture (released_at);

drop table if exists app_lecture_content;
create table app_lecture_content
(
  lecture_key bigint   not null,
  content     longtext not null,
  primary key (lecture_key)
);

drop table if exists app_lecture_trainee;
create table app_lecture_trainee
(
  lecture_trainee_key bigint auto_increment not null,
  lecture_key bigint      not null,
  trainee_id  varchar(255)  not null,

  created_at  datetime(6) not null,
  created_by  bigint      not null,
  modified_at datetime(6) not null,
  modified_by bigint      not null,

  primary key (lecture_trainee_key)
);
create index app_lecture_trainee_ux_01 on app_lecture_trainee (lecture_key, trainee_id);
