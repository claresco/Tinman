BEGIN TRANSACTION/
--rollback/
INSERT INTO actortype VALUES ( 'AGT', 'Agent' )/
INSERT INTO actortype VALUES ( 'GRP', 'Group' )/
INSERT INTO objecttype VALUES ( 'ACT', 'Activity' )/
INSERT INTO objecttype VALUES ( 'AGT', 'Agent' )/
INSERT INTO objecttype VALUES ( 'STMT', 'Statement' )/
INSERT INTO objecttype VALUES ( 'SREF', 'Statement Ref' )/
INSERT INTO objecttype VALUES ( 'GRP', 'Group' )/
--UPDATE objecttype SET otcname = 'Group' WHERE objecttypecode = 'GRP'/
INSERT INTO language VALUES ( 'en-US', 'US English', 'US English', 'English as used in the US' )/
INSERT INTO language VALUES ( 'es', 'Spanish', 'Spanish', 'Spanish as used in Spain' )/
INSERT INTO language VALUES ( 'en-GB', 'GB English', 'GB English', 'English as used in the United Kingdom' )/
INSERT INTO language VALUES ( 'en', 'English', 'English', 'English' )/
INSERT INTO language VALUES('de', 'German (Standard)', 'German (Standard)', 'German')/
INSERT INTO language VALUES('fr', 'French (Standard)', 'French (Standard)', 'French')/
INSERT INTO language VALUES('it', 'Italian (Standard)', 'Italian (Standard)', 'Italian')/
INSERT INTO language(languagecode, lname, lenglishname, ldescription) VALUES ('ja', 'Japanese', 'Japanese', 'Japanese as used in Japan')/
INSERT INTO language(languagecode, lname, lenglishname, ldescription) VALUES ('pt', 'Portugese', 'Portuguese (Portugal)', 'Portuguese as used in Portugal')/
INSERT INTO language(languagecode, lname, lenglishname, ldescription) VALUES ('tr', 'Turkish', 'Turkish', 'Turkish as used in Turkey')/
INSERT INTO language(languagecode, lname, lenglishname, ldescription) VALUES ('ru', 'Russian', 'Russian', 'Russian as used in Russia')/
INSERT INTO language(languagecode, lname, lenglishname, ldescription) VALUES ('zh-CN', 'Chinese', 'Chinese (PRC)', 'Chinese as used in China')/

ALTER TABLE tinman.activitytype ALTER COLUMN atypdesc TYPE TEXT/
INSERT INTO activitytype VALUES ( 'ASMT', 'http://adlnet.gov/expapi/activities/assessment', 'assessment', 'An assessment is an activity that determines a learner’s mastery of a particular subject area.  An assessment typically has one or more questions.' )/
INSERT INTO activitytype VALUES ( 'CRS', 'http://adlnet.gov/expapi/activities/course', 'course', 'A course represents an entire “content package” worth of material.  The largest level of granularity.  Unless flat, a course consists of multiple modules.  A course is not content.' )/
INSERT INTO activitytype VALUES ( 'FILE', 'http://adlnet.gov/expapi/activities/file', 'file', 'A file is similar to a link, only the resource is more likely to be used at a) a different time, b) can be used offline, and/or c) could be used with a different system.  Only the expectation changes.  Files are not considered learning content or SCOs.  If a file is intended for this purpose, it should be re-categorized.' )/
INSERT INTO activitytype VALUES ( 'INT', 'http://adlnet.gov/expapi/activities/interaction', 'interaction', 'An interaction is typically a part of a larger activity (such as assessment or simulation) and refers to a control to which a learner provides input.  An interaction can be either an asset or function independently.' )/
INSERT INTO activitytype VALUES ( 'LSN', 'http://adlnet.gov/expapi/activities/lesson', 'lesson', 'A lesson is learning content that may or may not take on the form of a SCO (formal, tracked learning).  A lesson is the most generic form.' )/
INSERT INTO activitytype VALUES ( 'LNK', 'http://adlnet.gov/expapi/activities/link', 'link', 'A link is simply a means of expressing a link to another resource within, or external to, an activity.  A link is not synonymous with launching another resource and should be considered external to the current resource.  Links are not learning content, nor SCOs.  If a link is intended for this purpose, it should be re-categorized.' )/
INSERT INTO activitytype VALUES ( 'MEDIA', 'http://adlnet.gov/expapi/activities/media', 'media', 'Media refers to text, audio, or video used to convey information.  Media can be consumed (tracked: completed), but doesn’t have an interactive component that may result in a score, success, or failure.' )/
INSERT INTO activitytype VALUES ( 'MTG', 'http://adlnet.gov/expapi/activities/meeting', 'meeting', 'A meeting is a gathering of multiple people for a common cause' )/
INSERT INTO activitytype VALUES ( 'MOD', 'http://adlnet.gov/expapi/activities/module', 'module', 'A module represents any “content aggregation” at least one level below the course level.  Modules of modules can exist for layering purposes.  Modules are not content.  Modules are one level up from all content.' )/
INSERT INTO activitytype VALUES ( 'OBJ', 'http://adlnet.gov/expapi/activities/objective', 'objective', 'An objective determines whether competency has been achieved in a desired area.  Objectives typically are associated with questions and assessments.  Objectives are not learning content and cannot be SCOs.' )/
INSERT INTO activitytype VALUES ( 'PRF', 'http://adlnet.gov/expapi/activities/performance', 'performance', 'A performance is an attempted task or series of tasks within a particular context.  Tasks would likely take on the form of interactions, or the performance could be self-contained content.' )/
INSERT INTO activitytype VALUES ( 'QSTN', 'http://adlnet.gov/expapi/activities/question', 'question', 'A question is typically part of an assessment and requires a response from the learner, a response that is then evaluated for correctness.' )/
INSERT INTO activitytype VALUES ( 'SIM', 'http://adlnet.gov/expapi/activities/simulation', 'simulation', 'A simulation is an attempted task or series of tasks in an artificial context that mimics reality.  Tasks would likely take on the form of interactions, or the simulation could be self-contained content.' )/

INSERT INTO languagemap VALUES ( 1010, 'Language map for the verb "answered"' )/
INSERT INTO languagemap VALUES ( 1020, 'Language map for the verb "asked"' )/
INSERT INTO languagemap VALUES ( 1030, 'Language map for the verb "attempted"' )/
INSERT INTO languagemap VALUES ( 1040, 'Language map for the verb "attended"' )/
INSERT INTO languagemap VALUES ( 1050, 'Language map for the verb "commented"' )/
INSERT INTO languagemap VALUES ( 1060, 'Language map for the verb "completed"' )/
INSERT INTO languagemap VALUES ( 1070, 'Language map for the verb "exited"' )/
INSERT INTO languagemap VALUES ( 1080, 'Language map for the verb "experienced"' )/
INSERT INTO languagemap VALUES ( 1090, 'Language map for the verb "failed"' )/
INSERT INTO languagemap VALUES ( 1100, 'Language map for the verb "imported"' )/
INSERT INTO languagemap VALUES ( 1110, 'Language map for the verb "initialized"' )/
INSERT INTO languagemap VALUES ( 1120, 'Language map for the verb "interacted"' )/
INSERT INTO languagemap VALUES ( 1130, 'Language map for the verb "launched"' )/
INSERT INTO languagemap VALUES ( 1140, 'Language map for the verb "mastered"' )/
INSERT INTO languagemap VALUES ( 1150, 'Language map for the verb "passed"' )/
INSERT INTO languagemap VALUES ( 1160, 'Language map for the verb "preferred"' )/
INSERT INTO languagemap VALUES ( 1170, 'Language map for the verb "progressed"' )/
INSERT INTO languagemap VALUES ( 1180, 'Language map for the verb "registered"' )/
INSERT INTO languagemap VALUES ( 1190, 'Language map for the verb "responded"' )/
INSERT INTO languagemap VALUES ( 1200, 'Language map for the verb "resumed"' )/
INSERT INTO languagemap VALUES ( 1210, 'Language map for the verb "scored"' )/
INSERT INTO languagemap VALUES ( 1220, 'Language map for the verb "shared"' )/
INSERT INTO languagemap VALUES ( 1225, 'Language map for the verb "started"' )/
INSERT INTO languagemap VALUES ( 1230, 'Language map for the verb "suspended"' )/
INSERT INTO languagemap VALUES ( 1240, 'Language map for the verb "terminated"' )/
INSERT INTO languagemap VALUES ( 1250, 'Language map for the verb "voided"' )/

INSERT INTO verb VALUES ( 1010, 'http://adlnet.gov/expapi/verbs/answered', 1010 )/
INSERT INTO verb VALUES ( 1020, 'http://adlnet.gov/expapi/verbs/asked', 1020 )/
INSERT INTO verb VALUES ( 1030, 'http://adlnet.gov/expapi/verbs/attempted', 1030 )/
INSERT INTO verb VALUES ( 1040, 'http://adlnet.gov/expapi/verbs/attended', 1040 )/
INSERT INTO verb VALUES ( 1050, 'http://adlnet.gov/expapi/verbs/commented', 1050 )/
INSERT INTO verb VALUES ( 1060, 'http://adlnet.gov/expapi/verbs/completed', 1060 )/
INSERT INTO verb VALUES ( 1070, 'http://adlnet.gov/expapi/verbs/exited', 1070 )/
INSERT INTO verb VALUES ( 1080, 'http://adlnet.gov/expapi/verbs/experienced', 1080 )/
INSERT INTO verb VALUES ( 1090, 'http://adlnet.gov/expapi/verbs/failed', 1090 )/
INSERT INTO verb VALUES ( 1100, 'http://adlnet.gov/expapi/verbs/imported', 1100 )/
INSERT INTO verb VALUES ( 1110, 'http://adlnet.gov/expapi/verbs/initialized', 1110 )/
INSERT INTO verb VALUES ( 1120, 'http://adlnet.gov/expapi/verbs/interacted', 1120 )/
INSERT INTO verb VALUES ( 1130, 'http://adlnet.gov/expapi/verbs/launched', 1130 )/
INSERT INTO verb VALUES ( 1140, 'http://adlnet.gov/expapi/verbs/mastered', 1140 )/
INSERT INTO verb VALUES ( 1150, 'http://adlnet.gov/expapi/verbs/passed', 1150 )/
INSERT INTO verb VALUES ( 1160, 'http://adlnet.gov/expapi/verbs/preferred', 1160 )/
INSERT INTO verb VALUES ( 1170, 'http://adlnet.gov/expapi/verbs/progressed', 1170 )/
INSERT INTO verb VALUES ( 1180, 'http://adlnet.gov/expapi/verbs/registered', 1180 )/
INSERT INTO verb VALUES ( 1190, 'http://adlnet.gov/expapi/verbs/responded', 1190 )/
INSERT INTO verb VALUES ( 1200, 'http://adlnet.gov/expapi/verbs/resumed', 1200 )/
INSERT INTO verb VALUES ( 1210, 'http://adlnet.gov/expapi/verbs/scored', 1210 )/
INSERT INTO verb VALUES ( 1220, 'http://adlnet.gov/expapi/verbs/shared', 1220 )/
INSERT INTO verb VALUES ( 1225, 'http://adlnet.gov/expapi/verbs/started', 1225 )/
INSERT INTO verb VALUES ( 1230, 'http://adlnet.gov/expapi/verbs/suspended', 1230 )/
INSERT INTO verb VALUES ( 1240, 'http://adlnet.gov/expapi/verbs/terminated', 1240 )/
INSERT INTO verb VALUES ( 1250, 'http://adlnet.gov/expapi/verbs/voided', 1250 )/

INSERT INTO languagemaplanguage VALUES ( 1010, 'en-US', 'answered' )/
INSERT INTO languagemaplanguage VALUES ( 1020, 'en-US', 'asked' )/
INSERT INTO languagemaplanguage VALUES ( 1030, 'en-US', 'attempted' )/
INSERT INTO languagemaplanguage VALUES ( 1040, 'en-US', 'attended' )/
INSERT INTO languagemaplanguage VALUES ( 1050, 'en-US', 'commented' )/
INSERT INTO languagemaplanguage VALUES ( 1060, 'en-US', 'completed' )/
INSERT INTO languagemaplanguage VALUES ( 1070, 'en-US', 'exited' )/
INSERT INTO languagemaplanguage VALUES ( 1080, 'en-US', 'experienced' )/
INSERT INTO languagemaplanguage VALUES ( 1090, 'en-US', 'failed' )/
INSERT INTO languagemaplanguage VALUES ( 1100, 'en-US', 'imported' )/
INSERT INTO languagemaplanguage VALUES ( 1110, 'en-US', 'initialized' )/
INSERT INTO languagemaplanguage VALUES ( 1120, 'en-US', 'interacted' )/
INSERT INTO languagemaplanguage VALUES ( 1130, 'en-US', 'launched' )/
INSERT INTO languagemaplanguage VALUES ( 1140, 'en-US', 'mastered' )/
INSERT INTO languagemaplanguage VALUES ( 1150, 'en-US', 'passed' )/
INSERT INTO languagemaplanguage VALUES ( 1160, 'en-US', 'preferred' )/
INSERT INTO languagemaplanguage VALUES ( 1170, 'en-US', 'progressed' )/
INSERT INTO languagemaplanguage VALUES ( 1180, 'en-US', 'registered' )/
INSERT INTO languagemaplanguage VALUES ( 1190, 'en-US', 'responded' )/
INSERT INTO languagemaplanguage VALUES ( 1200, 'en-US', 'resumed' )/
INSERT INTO languagemaplanguage VALUES ( 1210, 'en-US', 'scored' )/
INSERT INTO languagemaplanguage VALUES ( 1220, 'en-US', 'shared' )/
INSERT INTO languagemaplanguage VALUES ( 1225, 'en-US', 'started' )/
INSERT INTO languagemaplanguage VALUES ( 1230, 'en-US', 'suspended' )/
INSERT INTO languagemaplanguage VALUES ( 1240, 'en-US', 'terminated' )/
INSERT INTO languagemaplanguage VALUES ( 1250, 'en-US', 'voided' )/

INSERT INTO tinman.contextactivitytype (contextactivitytypecode, catypdesc) VALUES('CATEGORY', NULL)/
INSERT INTO tinman.contextactivitytype (contextactivitytypecode, catypdesc) VALUES('GROUPING', NULL)/
INSERT INTO tinman.contextactivitytype (contextactivitytypecode, catypdesc) VALUES('OTHER', NULL)/
INSERT INTO tinman.contextactivitytype (contextactivitytypecode, catypdesc) VALUES('PARENT', NULL)/

INSERT INTO tinman.interactioncomponenttype (interactioncomponenttypecode, icomptypname, icomptypdesc) VALUES('CHOICES', 'CHOICES', NULL)/
INSERT INTO tinman.interactioncomponenttype (interactioncomponenttypecode, icomptypname, icomptypdesc) VALUES('SCALE', 'SCALE', NULL)/
INSERT INTO tinman.interactioncomponenttype (interactioncomponenttypecode, icomptypname, icomptypdesc) VALUES('SOURCE', 'SOURCE', NULL)/
INSERT INTO tinman.interactioncomponenttype (interactioncomponenttypecode, icomptypname, icomptypdesc) VALUES('STEPS', 'STEPS', NULL)/
INSERT INTO tinman.interactioncomponenttype (interactioncomponenttypecode, icomptypname, icomptypdesc) VALUES('TARGET', 'TARGET', NULL)/

INSERT INTO tinman.interactiontype (interactiontypecode, itypname, itypdesc) VALUES('CHO', 'CHOICE', NULL)/
INSERT INTO tinman.interactiontype (interactiontypecode, itypname, itypdesc) VALUES('FILL', 'FILL-IN', NULL)/
INSERT INTO tinman.interactiontype (interactiontypecode, itypname, itypdesc) VALUES('LIKE', 'LIKERT', NULL)/
INSERT INTO tinman.interactiontype (interactiontypecode, itypname, itypdesc) VALUES('MAT', 'MATCHING', NULL)/
INSERT INTO tinman.interactiontype (interactiontypecode, itypname, itypdesc) VALUES('NUM', 'NUMERIC', NULL)/
INSERT INTO tinman.interactiontype (interactiontypecode, itypname, itypdesc) VALUES('OTH', 'OTHER', NULL)/
INSERT INTO tinman.interactiontype (interactiontypecode, itypname, itypdesc) VALUES('PERF', 'PERFORMANCE', NULL)/
INSERT INTO tinman.interactiontype (interactiontypecode, itypname, itypdesc) VALUES('SEQ', 'SEQUENCING', NULL)/
INSERT INTO tinman.interactiontype (interactiontypecode, itypname, itypdesc) VALUES('TF', 'TRUE-FALSE', NULL)/
--rollback/
--commit/