# --- !Ups
create table "Accounts" ("Id" serial NOT NULL PRIMARY KEY
                            ,"Login" text NOT NULL
                            ,"Email" text NOT NULL
                            ,"Password" text NOT NULL
                            ,"DisplayName" text NOT NULL
                            ,"RegisteredFromIp" text NOT NULL
                            ,"Role" text NOT NULL);

# --- !Downs

drop table "Accounts";
