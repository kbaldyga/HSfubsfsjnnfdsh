# --- !Ups
create table "Reviews" ("Id" serial NOT NULL PRIMARY KEY
                            ,"ContractorId" INTEGER NOT NULL
                            ,"CreatedBy" INTEGER NOT NULL
                            ,"Rating" SMALLINT NOT NULL
                            ,"ShortDescription" text NOT NULL
                            ,"LongDescription" text NULL
                            );

# --- !Downs

drop table "Reviews";
