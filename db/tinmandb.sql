/*
Script generated Jan-07-2015 12:14:12 PM
Database: hedb
Schema: tinman
*/

CREATE TABLE tinman.account  ( 
	accountid   	numeric(12,0) NOT NULL,
	accthomepage	varchar(250) NULL,
	acctname    	varchar(250) NULL,
	PRIMARY KEY(accountid)
);
COMMENT ON TABLE tinman.account IS 'User account on an existing system';
COMMENT ON COLUMN tinman.account.accountid IS 'PK';
COMMENT ON COLUMN tinman.account.accthomepage IS 'IRL';
COMMENT ON COLUMN tinman.account.acctname IS 'The unique account id or name on remote system';

CREATE TABLE tinman.activity  ( 
	activityid         	numeric(12,0) NOT NULL,
	actviri            	varchar(250) NULL,
	namelanguagemapid  	numeric(12,0) NULL,
	desclanguagemapid  	numeric(12,0) NULL,
	activitytypecode   	varchar(10) NULL,
	interactiontypecode	varchar(10) NULL,
	xextensionid       	numeric(12,0) NULL,
	PRIMARY KEY(activityid)
);
COMMENT ON COLUMN tinman.activity.activityid IS 'synthetic PK';
COMMENT ON COLUMN tinman.activity.actviri IS 'An IRI that uniquely identifies this activity.  This is known as the "Activity ID" in the spec';
COMMENT ON COLUMN tinman.activity.namelanguagemapid IS 'Pointer to languagemap of this activity name';
COMMENT ON COLUMN tinman.activity.desclanguagemapid IS 'Pointer to languagemap of this activity description';
COMMENT ON COLUMN tinman.activity.activitytypecode IS 'FK into ActivityType';

CREATE TABLE tinman.activityprofile  ( 
	activityprofileid	numeric(12,0) NOT NULL,
	activityid       	numeric(12,0) NOT NULL,
	profilekey       	varchar(255) NOT NULL,
	documentid       	numeric(12,0) NOT NULL,
	stored           	timestamp NOT NULL,
	PRIMARY KEY(activityprofileid)
);

CREATE TABLE tinman.activitytype  ( 
	activitytypecode	varchar(10) NOT NULL,
	atypiri         	varchar(80) NOT NULL,
	atypname        	varchar(40) NULL,
	atypdesc        	text NULL,
	PRIMARY KEY(activitytypecode)
);

CREATE TABLE tinman.actor  ( 
	actorid      	numeric(12,0) NOT NULL,
	actortypecode	varchar(12) NOT NULL,
	actname      	varchar(125) NULL,
	actmbox      	varchar(250) NULL,
	atcmbox_sha1 	varchar(50) NULL,
	openid       	varchar(250) NULL,
	accountid    	numeric(12,0) NULL,
	PRIMARY KEY(actorid)
);
COMMENT ON COLUMN tinman.actor.actortypecode IS '"GROUP" in the case of a group actor.  == objectType';
COMMENT ON COLUMN tinman.actor.actname IS 'Full name';
COMMENT ON COLUMN tinman.actor.actmbox IS 'mailto IRI';
COMMENT ON COLUMN tinman.actor.atcmbox_sha1 IS 'sha1 of a mbox IRI';
COMMENT ON COLUMN tinman.actor.openid IS 'URI that uniquely identifies an actor';
COMMENT ON COLUMN tinman.actor.accountid IS 'PK to the account table';

CREATE TABLE tinman.actortype  ( 
	actortypecode	char(12) NOT NULL,
	atcname      	char(50) NULL,
	PRIMARY KEY(actortypecode)
);

CREATE TABLE tinman.agentprofile  ( 
	agentprofileid	numeric(12,0) NOT NULL,
	actorid       	numeric(12,0) NOT NULL,
	profilekey    	varchar(255) NOT NULL,
	documentid    	numeric(12,0) NOT NULL,
	stored        	timestamp NOT NULL,
	PRIMARY KEY(agentprofileid)
);

CREATE TABLE tinman.context  ( 
	contextid       	numeric(12,0) NOT NULL,
	statementid     	numeric(12,0) NULL,
	ctxtregistration	varchar(36) NULL,
	ctxtinstructor  	numeric(12,0) NULL,
	ctxtteam        	numeric(12,0) NULL,
	ctxtrevision    	varchar(255) NULL,
	ctxtplatform    	varchar(512) NULL,
	languagecode    	varchar(12) NULL,
	PRIMARY KEY(contextid)
);
COMMENT ON COLUMN tinman.context.statementid IS 'Statement which is the context.';
COMMENT ON COLUMN tinman.context.ctxtinstructor IS 'Agent ID';
COMMENT ON COLUMN tinman.context.ctxtteam IS 'Group actorID';

CREATE TABLE tinman.contextactivity  ( 
	contextactivityid      	numeric(12,0) NOT NULL,
	contextactivitytypecode	varchar(10) NOT NULL,
	contextid              	numeric(12,0) NOT NULL,
	activityid             	numeric(12,0) NOT NULL,
	PRIMARY KEY(contextactivityid)
);

CREATE TABLE tinman.contextactivitytype  ( 
	contextactivitytypecode	varchar(10) NOT NULL,
	catypdesc              	varchar(255) NULL,
	PRIMARY KEY(contextactivitytypecode)
);

CREATE TABLE tinman.correctresponse  ( 
	correctresponseid	numeric(12,0) NOT NULL,
	activityid       	numeric(12,0) NOT NULL,
	crpattern        	varchar(1024) NOT NULL,
	PRIMARY KEY(correctresponseid)
);

CREATE TABLE tinman.document  ( 
	documentid	numeric(12,0) NOT NULL,
	docdata   	bytea NULL,
	PRIMARY KEY(documentid)
);
COMMENT ON COLUMN tinman.document.docdata IS 'Binary data - contents of document';

CREATE TABLE tinman.groupactors  ( 
	groupid	numeric(12,0) NOT NULL,
	agentid	numeric(12,0) NOT NULL,
	PRIMARY KEY(groupid,agentid)
);
COMMENT ON TABLE tinman.groupactors IS 'List of agent actors linked to a group actor';
COMMENT ON COLUMN tinman.groupactors.groupid IS 'an actor ID -- actor objectType must be "GROUP"';
COMMENT ON COLUMN tinman.groupactors.agentid IS 'actorID of an Agent actor -- objectType must be "AGENT"';

CREATE TABLE tinman.interactioncomponent  ( 
	interactioncomponentid      	numeric(12,0) NOT NULL,
	activityid                  	numeric(12,0) NOT NULL,
	icompkey                    	varchar(255) NOT NULL,
	languagemapid               	numeric(12,0) NULL,
	interactioncomponenttypecode	varchar(10) NOT NULL,
	PRIMARY KEY(interactioncomponentid)
);

CREATE TABLE tinman.interactioncomponenttype  ( 
	interactioncomponenttypecode	varchar(10) NOT NULL,
	icomptypname                	varchar(80) NOT NULL,
	icomptypdesc                	varchar(255) NULL,
	PRIMARY KEY(interactioncomponenttypecode)
);

CREATE TABLE tinman.interactiontype  ( 
	interactiontypecode	varchar(10) NOT NULL,
	itypname           	varchar(80) NOT NULL,
	itypdesc           	varchar(255) NULL,
	PRIMARY KEY(interactiontypecode)
);

CREATE TABLE tinman.language  ( 
	languagecode	varchar(12) NOT NULL,
	lname       	varchar(40) NULL,
	lenglishname	varchar(40) NULL,
	ldescription	varchar(255) NULL,
	PRIMARY KEY(languagecode)
);
COMMENT ON COLUMN tinman.language.languagecode IS 'RFC 5646 Language Tag';
COMMENT ON COLUMN tinman.language.lname IS 'Language name in native script';
COMMENT ON COLUMN tinman.language.lenglishname IS 'English representation of language ( e.g. Russian )';
COMMENT ON COLUMN tinman.language.ldescription IS 'Full description of language tag ( e.g. Russian in Cyrillic script, as used in Russia )';

CREATE TABLE tinman.languagemap  ( 
	languagemapid	numeric(12,0) NOT NULL,
	lmdesc       	varchar(80) NOT NULL,
	PRIMARY KEY(languagemapid)
);
COMMENT ON TABLE tinman.languagemap IS 'http://tools.ietf.org/html/rfc5646';
COMMENT ON COLUMN tinman.languagemap.lmdesc IS 'Description of this language map ( e.g. Language map for verb "launched" )';

CREATE TABLE tinman.languagemaplanguage  ( 
	languagemapid	numeric(12,0) NOT NULL,
	languagecode 	varchar(12) NOT NULL,
	lmldisplay   	text NOT NULL,
	PRIMARY KEY(languagemapid,languagecode)
);

CREATE TABLE tinman.object  ( 
	objectid      	numeric(12,0) NOT NULL,
	objecttypecode	varchar(12) NULL,
	statementid   	numeric(12,0) NULL,
	activityid    	numeric(12,0) NULL,
	actorid       	numeric(12,0) NULL,
	PRIMARY KEY(objectid)
);
COMMENT ON COLUMN tinman.object.objecttypecode IS 'Can be "Activity", "StatementRef", or "SubStatement"';
COMMENT ON COLUMN tinman.object.statementid IS 'Reference to a new statement. Used with objectType "SubStatement"';
COMMENT ON COLUMN tinman.object.activityid IS 'FK to the Activity table. Used with objectType"Activity"';

CREATE TABLE tinman.objecttype  ( 
	objecttypecode	char(12) NOT NULL,
	otcname       	char(50) NULL,
	PRIMARY KEY(objecttypecode)
);

CREATE TABLE tinman.result  ( 
	resultid    	numeric(12,0) NOT NULL,
	rsltrawscore	numeric(10,5) NULL,
	rsltminscore	numeric(10,5) NULL,
	rsltmaxscore	numeric(10,5) NULL,
	rsltscaled  	numeric(6,5) NULL,
	rsltsuccess 	numeric(1,0) NULL,
	rsltcomplete	numeric(1,0) NULL,
	rsltresponse	varchar(500) NULL,
	rsltduration	int8 NULL,
	PRIMARY KEY(resultid)
);
COMMENT ON COLUMN tinman.result.rsltrawscore IS 'Raw score';
COMMENT ON COLUMN tinman.result.rsltminscore IS 'min score';
COMMENT ON COLUMN tinman.result.rsltscaled IS 'scale factor for scoring';
COMMENT ON COLUMN tinman.result.rsltsuccess IS 'boolean -- was the activity successful';
COMMENT ON COLUMN tinman.result.rsltcomplete IS 'Boolean -- was the activity completed';
COMMENT ON COLUMN tinman.result.rsltresponse IS 'String response ';

CREATE TABLE tinman.state  ( 
	stateid         	numeric(12,0) NOT NULL,
	activityid      	numeric(12,0) NOT NULL,
	actorid         	numeric(12,0) NOT NULL,
	statekey        	varchar(250) NOT NULL,
	registrationuuid	varchar(36) NULL,
	documentid      	numeric(12,0) NOT NULL,
	stored          	timestamp NOT NULL,
	PRIMARY KEY(stateid)
);

CREATE TABLE tinman.statement  ( 
	statementid  	numeric(12,0) NOT NULL,
	actorid      	numeric(12,0) NOT NULL,
	verbid       	numeric(12,0) NOT NULL,
	objectid     	numeric(12,0) NOT NULL,
	resultid     	numeric(12,0) NULL,
	contextid    	numeric(12,0) NULL,
	sttime       	timestamp NULL,
	authorityid  	numeric(12,0) NULL,
	stversion    	varchar(10) NULL,
	statementuuid	varchar(36) NULL,
	ststored     	timestamp NULL,
	isvoided     	numeric(1,0) NOT NULL DEFAULT 0,
	PRIMARY KEY(statementid)
);
COMMENT ON COLUMN tinman.statement.statementid IS 'PK for the statement table. Internal';
COMMENT ON COLUMN tinman.statement.contextid IS 'Context that gives the Statement more meaning. Examples: a team the Actor is working with, altitude at which a scenario was attempted in a flight simulator';
COMMENT ON COLUMN tinman.statement.statementuuid IS 'UUID for this statement -- for global identification';

CREATE TABLE tinman.verb  ( 
	verbid       	numeric(12,0) NOT NULL,
	verbiri      	varchar(250) NULL,
	languagemapid	numeric(12,0) NULL,
	PRIMARY KEY(verbid)
);
COMMENT ON COLUMN tinman.verb.verbid IS 'PK';
COMMENT ON COLUMN tinman.verb.verbiri IS 'IRI uniquely identifying this verb and implying its meaning';

CREATE TABLE tinman.xextension  ( 
	xextensionid	numeric(12,0) NOT NULL,
	PRIMARY KEY(xextensionid)
);

CREATE TABLE tinman.xextensionmap  ( 
	xextensionmapid	numeric(12,0) NOT NULL,
	xextensionid   	numeric(12,0) NOT NULL,
	xekeyiri       	varchar(250) NOT NULL,
	xevalue        	varchar(4096) NULL,
	PRIMARY KEY(xextensionmapid)
);

ALTER TABLE tinman.activity
	ADD CONSTRAINT activity_idx
	UNIQUE (actviri);

ALTER TABLE tinman.activityprofile
	ADD CONSTRAINT activityprofile_idx
	UNIQUE (activityid, profilekey);

ALTER TABLE tinman.actor
	ADD CONSTRAINT fk_actor_rf_account
	FOREIGN KEY(accountid)
	REFERENCES tinman.account(accountid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.activityprofile
	ADD CONSTRAINT fk_activityprofile_activity
	FOREIGN KEY(activityid)
	REFERENCES tinman.activity(activityid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.contextactivity
	ADD CONSTRAINT contextactivity_fk2
	FOREIGN KEY(activityid)
	REFERENCES tinman.activity(activityid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.correctresponse
	ADD CONSTRAINT fk_correctresponse_activity
	FOREIGN KEY(activityid)
	REFERENCES tinman.activity(activityid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.interactioncomponent
	ADD CONSTRAINT fk_interactioncomponent_activity
	FOREIGN KEY(activityid)
	REFERENCES tinman.activity(activityid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.object
	ADD CONSTRAINT fk_object_rf_activity
	FOREIGN KEY(activityid)
	REFERENCES tinman.activity(activityid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.state
	ADD CONSTRAINT fk_state_activity
	FOREIGN KEY(activityid)
	REFERENCES tinman.activity(activityid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.activity
	ADD CONSTRAINT fk_activity_rf_activitytype
	FOREIGN KEY(activitytypecode)
	REFERENCES tinman.activitytype(activitytypecode)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.agentprofile
	ADD CONSTRAINT fk_agentprofile_actor
	FOREIGN KEY(actorid)
	REFERENCES tinman.actor(actorid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.groupactors
	ADD CONSTRAINT fk_groupactors_rf_actor_group
	FOREIGN KEY(groupid)
	REFERENCES tinman.actor(actorid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.groupactors
	ADD CONSTRAINT fk_groupactors_rf_actor_agent
	FOREIGN KEY(agentid)
	REFERENCES tinman.actor(actorid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.object
	ADD CONSTRAINT fk_object_rf_actor
	FOREIGN KEY(actorid)
	REFERENCES tinman.actor(actorid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.state
	ADD CONSTRAINT fk_state_actor
	FOREIGN KEY(actorid)
	REFERENCES tinman.actor(actorid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.statement
	ADD CONSTRAINT fk_statement_rf_actor
	FOREIGN KEY(actorid)
	REFERENCES tinman.actor(actorid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.actor
	ADD CONSTRAINT fk_actor_rf_actortype
	FOREIGN KEY(actortypecode)
	REFERENCES tinman.actortype(actortypecode)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.contextactivity
	ADD CONSTRAINT contextactivity_fk1
	FOREIGN KEY(contextid)
	REFERENCES tinman.context(contextid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.statement
	ADD CONSTRAINT fk_statement_rf_context
	FOREIGN KEY(contextid)
	REFERENCES tinman.context(contextid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.contextactivity
	ADD CONSTRAINT contextactivity_fk
	FOREIGN KEY(contextactivitytypecode)
	REFERENCES tinman.contextactivitytype(contextactivitytypecode)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.activityprofile
	ADD CONSTRAINT fk_activityprofile_document
	FOREIGN KEY(documentid)
	REFERENCES tinman.document(documentid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.agentprofile
	ADD CONSTRAINT fk_agentprofile_document
	FOREIGN KEY(documentid)
	REFERENCES tinman.document(documentid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.interactioncomponent
	ADD CONSTRAINT fk_interactioncomponent_interactioncomponenttypecode
	FOREIGN KEY(interactioncomponenttypecode)
	REFERENCES tinman.interactioncomponenttype(interactioncomponenttypecode)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.activity
	ADD CONSTRAINT fk_activity_rf_interactiontypecode
	FOREIGN KEY(interactiontypecode)
	REFERENCES tinman.interactiontype(interactiontypecode)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.context
	ADD CONSTRAINT fk_context_rf_language
	FOREIGN KEY(languagecode)
	REFERENCES tinman.language(languagecode)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.languagemaplanguage
	ADD CONSTRAINT fk_languagemaplanguage_rf_language
	FOREIGN KEY(languagecode)
	REFERENCES tinman.language(languagecode)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.activity
	ADD CONSTRAINT fk_activity_rf_languagemap_name
	FOREIGN KEY(namelanguagemapid)
	REFERENCES tinman.languagemap(languagemapid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.activity
	ADD CONSTRAINT fk_activity_rf_languagemap_desc
	FOREIGN KEY(desclanguagemapid)
	REFERENCES tinman.languagemap(languagemapid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.interactioncomponent
	ADD CONSTRAINT fk_interactioncomponent_languagemap
	FOREIGN KEY(languagemapid)
	REFERENCES tinman.languagemap(languagemapid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.languagemaplanguage
	ADD CONSTRAINT fk_languagemaplanguage_rf_languagemap
	FOREIGN KEY(languagemapid)
	REFERENCES tinman.languagemap(languagemapid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.verb
	ADD CONSTRAINT fk_verb_rf_languagemap
	FOREIGN KEY(languagemapid)
	REFERENCES tinman.languagemap(languagemapid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.statement
	ADD CONSTRAINT fk_statement_rf_object
	FOREIGN KEY(objectid)
	REFERENCES tinman.object(objectid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.object
	ADD CONSTRAINT fk_object_rf_objecttype
	FOREIGN KEY(objecttypecode)
	REFERENCES tinman.objecttype(objecttypecode)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.statement
	ADD CONSTRAINT fk_statement_rf_result
	FOREIGN KEY(resultid)
	REFERENCES tinman.result(resultid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.context
	ADD CONSTRAINT fk_context_rf_statement
	FOREIGN KEY(statementid)
	REFERENCES tinman.statement(statementid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.object
	ADD CONSTRAINT fk_object_rf_statement
	FOREIGN KEY(statementid)
	REFERENCES tinman.statement(statementid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.statement
	ADD CONSTRAINT fk_statement_rf_verb
	FOREIGN KEY(verbid)
	REFERENCES tinman.verb(verbid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.activity
	ADD CONSTRAINT fk_activity_xextension
	FOREIGN KEY(xextensionid)
	REFERENCES tinman.xextension(xextensionid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

ALTER TABLE tinman.xextensionmap
	ADD CONSTRAINT fk_xextensionmap_xextension
	FOREIGN KEY(xextensionid)
	REFERENCES tinman.xextension(xextensionid)
	MATCH SIMPLE
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION ;

CREATE INDEX idx_actactivityiri
	ON tinman.activity(actviri);

CREATE UNIQUE INDEX activity_idx
	ON tinman.activity(actviri);

CREATE UNIQUE INDEX activityprofile_idx
	ON tinman.activityprofile(activityid, profilekey);

CREATE INDEX idx_actor_accountid
	ON tinman.actor(accountid);

CREATE INDEX idx_cactcontextid
	ON tinman.contextactivity(contextid);

CREATE INDEX idx_objactivityid
	ON tinman.object(activityid);

CREATE UNIQUE INDEX uk_state_actvityactorstatereguuid
	ON tinman.state(activityid, actorid, statekey, registrationuuid);

CREATE UNIQUE INDEX idx_stuniquuid
	ON tinman.statement(statementid);

CREATE INDEX idx_stisvoided
	ON tinman.statement(isvoided);

CREATE INDEX idx_stmtactoridandisvoided
	ON tinman.statement(actorid, isvoided);

CREATE UNIQUE INDEX idx_statement_statementuuid
	ON tinman.statement(statementuuid);

CREATE INDEX idx_stmtverbid
	ON tinman.statement(verbid);

CREATE INDEX idx_vverbiri
	ON tinman.verb(verbiri);

