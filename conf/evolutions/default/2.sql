# --- !Ups
create table "Contractors" ("Id" serial NOT NULL PRIMARY KEY
                            ,"Name" text NOT NULL
                            ,"Email" text NOT NULL
                            ,"Phone" text NOT NULL
                            ,"Website" text NULL
                            ,"Postcode" integer NOT NULL
                            ,"PostcodeSuffix" varchar(2) NOT NULL
                            ,"Description" text NULL);

create table "ContractorTrades" ("ContractorId" INTEGER NOT NULL,
                                 "TradeId" INTEGER NOT NULL);

insert into "ContractorTrades"("ContractorId", "TradeId") values(1, 1);
insert into "ContractorTrades"("ContractorId", "TradeId") values(1, 2);

-- TODO: add foreign keys
# --- !Downs

drop table "Contractors";
drop table "ContractorTrades";
