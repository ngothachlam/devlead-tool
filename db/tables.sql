-- ============================

-- This file was created using Derby's dblook utility.
-- Timestamp: 2009-04-18 16:05:06.64
-- Source database is: db/data/test
-- Connection URL is: jdbc:derby:db/data/test;create=true
-- appendLogs: false

-- ----------------------------------------------
-- DDL Statements for tables
-- ----------------------------------------------

CREATE TABLE "APP"."TEST" ("ID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "NAME" CHAR(10));

