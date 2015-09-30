# --- !Ups
create table "Portfolios" ("Id" serial NOT NULL PRIMARY KEY
                            ,"ContractorId" INTEGER NOT NULL
                            ,"TradeId" INTEGER NOT NULL
                            ,"ShortDescription" text NOT NULL
                            ,"LongDescription" text NULL
                            );

# --- !Downs

drop table "Portfolios";
