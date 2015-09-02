# --- !Ups
create table "Contractors" ("Id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY
                            ,"Name" NVARCHAR NOT NULL
                            ,"Email" NVARCHAR NOT NULL
                            ,"Phone" NVARCHAR NOT NULL
                            ,"Website" NVARCHAR NULL
                            ,"Postcode" NVARCHAR NOT NULL
                            ,"Description" NVARCHAR NULL);



insert into Contractors(Name, Email, Phone, Website, Postcode, Description) values('test name', 'test email@email.cc', 'test website', '1055ab', 'long description');

create table "ContractorTrades" ("ContractorId" BIGINT NOT NULL,
                                 "TradeId" BIG INT NOT NULL);

insert into ContractorTrades(ContractorId, TradeId) values(1, 1);
insert into ContractorTrades(ContractorId, TradeId) values(1, 2);

-- TODO: add foreign keys
# --- !Downs

drop table "Contractors";
drop table "ContractorTrades";
