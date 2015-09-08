# --- !Ups
create table "Trades" ("Id" serial NOT NULL PRIMARY KEY, "Name" varchar(128) NOT NULL);

insert into "Trades"("Name") values('Architectural service');
insert into "Trades"("Name") values('Painting and decorating');
insert into "Trades"("Name") values('Electrical');
insert into "Trades"("Name") values('Flooring');
insert into "Trades"("Name") values('Kitchen fitting');
insert into "Trades"("Name") values('Bathroom fitting');
insert into "Trades"("Name") values('Plumbing');
insert into "Trades"("Name") values('Tiling');


# --- !Downs

drop table "Trades";