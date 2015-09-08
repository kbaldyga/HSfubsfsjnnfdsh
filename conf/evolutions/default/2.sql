# --- !Ups
create table "Contractors" ("Id" serial NOT NULL PRIMARY KEY
                            ,"Name" text NOT NULL
                            ,"Email" text NOT NULL
                            ,"Phone" text NOT NULL
                            ,"Website" text NULL
                            ,"Postcode" text NOT NULL
                            ,"Description" text NULL);



insert into "Contractors"("Name", "Email", "Phone", "Website", "Postcode", "Description") values('test name', 'test email@email.cc', '067678777', 'test website', '1055ab', 'long description');
insert into "Contractors"("Name", "Email", "Phone", "Website", "Postcode", "Description") values('test name2', 'test email2@email.cc', '22222', 'test website2', '1055ab2', 'long description2');

create table "ContractorTrades" ("ContractorId" INTEGER NOT NULL,
                                 "TradeId" INTEGER NOT NULL);

insert into "ContractorTrades"("ContractorId", "TradeId") values(1, 1);
insert into "ContractorTrades"("ContractorId", "TradeId") values(1, 2);

-- TODO: add foreign keys
# --- !Downs

drop table "Contractors";
drop table "ContractorTrades";
