# --- !Ups
create table "Accounts" ("Id" serial NOT NULL PRIMARY KEY
                            ,"Email" text NOT NULL
                            ,"Password" text NOT NULL
                            ,"Role" text NOT NULL);

# --- !Downs

drop table "Accounts";
