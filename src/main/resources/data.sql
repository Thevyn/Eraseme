

CREATE TABLE Custom (
  creditCard varchar(16),
  customerNo integer NOT NULL,
  email varchar(40),
  name varchar(40),
  password varchar(40),
  phone varchar(20),
  LOCK_FLAG integer,
  PRIMARY KEY (customerNo)
);



INSERT INTO CUSTOM (CREDITCARD,CUSTOMERNO,EMAIL,NAME,PASSWORD,PHONE,LOCK_FLAG) VALUES
(NULL,1000234,'robert.gibson@gmail.com','Robert Gibson',NULL,'+47 11 12 13 14',NULL),
(NULL,1000235,'toby.baxter@inmeta.no','Toby Baxter',NULL,'+47 11 12 13 14',NULL),
(NULL,1000236,'harley.doyle@yahoo.com','Harley Doyle',NULL,'98765432',NULL),
(NULL,1000237,'jordan.anderson@crayon.com','Jordan Anderson',NULL,'+47 11 12 13 14',NULL),
(NULL,1000238,'esito@crayon.com','Esito',NULL,'+47 11 12 13 14',NULL),
(NULL,1000239,'louie.andrews@crayon.com','Louie Andrews',NULL,'+47 11 12 13 14',NULL),
(NULL,1000240,'brecken.mercer@crayon.com','Brecken Mercer',NULL,'+47 11 12 13 14',NULL),
(NULL,1000241,'jens.barth@esito.no','Jens Barth',NULL,'+47 11 12 13 14',NULL),
(NULL,1000242,'hudson.gould@esito.no','Hudson Gould',NULL,'+47 11 12 13 14',NULL),
(NULL,1000243,NULL,NULL,NULL,NULL,NULL),
(NULL,1000244,'zaiden.mcconnell@crayon.com','Zaiden Mcconnell',NULL,'+47 11 12 13 14',NULL),
(NULL,1000245,'elian.harvey@crayon.com','Elian Harvey',NULL,'',NULL),
(NULL,1000246,'pernille.groth@crayon.com','Pernille Groth',NULL,'+47 11 12 13 14',NULL),
(NULL,1000247,'rory.mckinney@inmeta.no','Rory Mckinney',NULL,'+47 11 12 13 14',NULL),
(NULL,1000248,'franky.riggs@crayon.com','Franky Riggs',NULL,'1234',NULL),
(NULL,1000303,'chris.richard@msn.com','Chris Richard',NULL,'34535',NULL),
(NULL,1000304,'carmen.williams@inmeta.no','Carmen Williams',NULL,'5432871',NULL),
(NULL,1000305,'kris.william@esito.no','Kris William',NULL,'42389',NULL),
(NULL,1000306,'justice.levine@crayon.com','Justice Levine',NULL,'543289',NULL),
(NULL,1000307,'denny.riley@msn.com','Denny Riley',NULL,'43729',NULL),
(NULL,1000308,'raylee.hart@yahoo.com','Raylee Hart',NULL,'89776',NULL),
(NULL,1000309,'casey.stewart@gmail.com','Casey Stewart',NULL,'63815',NULL),
(NULL,1000310,'nicky.powell@msn.com','Nicky Powell',NULL,'678',NULL),
(NULL,1000311,'jo.foster@crayon.com','Jo Foster',NULL,'123',NULL),
(NULL,1000312,'jess.pearce@msn.com','Jess Pearce',NULL,'1234',NULL),
(NULL,1000313,'eli.jones@inmeta.no','Eli Jones',NULL,'123456',NULL),
(NULL,1000314,'brook.wilson@esito.no','Brook Wilson',NULL,'123',NULL),
(NULL,1000315,'danny.palmer@inmeta.no','Danny Palmer',NULL,'123',NULL),
(NULL,1000316,'erin.king@gmail.com','Erin King',NULL,'931212121212',NULL),
(NULL,1000317,'erin.miranda@crayon.com','Erin Miranda',NULL,'988793178293',NULL),
(NULL,1000318,'aaren.copeland@crayon.com','Aaren Copeland',NULL,'121212121212',NULL),
(NULL,1000319,'drew.mays@crayon.com','Drew Mays',NULL,'847916',NULL),
(NULL,1000321,'gene.morin@crayon.com','Gene Morin',NULL,'nei',NULL),
(NULL,1000322,'terry.stark@crayon.com','Terry Stark',NULL,'123456',NULL),
(NULL,1000323,'jess.richardson@msn.com','Jess Richardson',NULL,'free',NULL);

