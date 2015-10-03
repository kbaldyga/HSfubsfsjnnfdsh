# --- !Ups
create table "Uploads" ("Id" serial not null primary key
                            ,"AggregateId" integer not null
                            ,"CreatedBy" integer not null
                            ,"Type" text not null
                            ,"PhysicalLocation" text not null
                            ,"CreatedAt" date not null default CURRENT_DATE
                            );

# --- !Downs

drop table "Uploads";
