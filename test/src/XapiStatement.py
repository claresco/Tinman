import json

class Account:
    
    def __init__(self, name, homePage):
        self.name = name
        self.homePage = homePage
        
    def dict(self):
        return self.__dict__
        
    def json(self):
        return json.dumps(self.__dict__, encoding="UTF-8")


class InverseFunctionalID:
    
    def __init__(self, mbox=None, mbox_sha1sum=None, openid=None, account=None):
        self.mbox = mbox
        self.mbox_sha1sum = mbox_sha1sum
        self.openid = openid
        self.account = account
    
    def dict(self):
        theDict = {}
        if self.mbox is not None:
            theDict['mbox'] = self.mbox
        if self.mbox_sha1sum is not None:
            theDict['mbox_sha1sum'] = self.mbox_sha1sum
        if self.openid is not None:
            theDict['openid'] = self.openid
        if self.account is not None:
            theDict['account'] = self.account.dict()    
        return theDict
        
    def json(self):
        theDict = self.dict()
        
        return json.dumps(theDict, encoding="UTF-8")
        
        
class Agent:
    
    def __init__(self, name, inverseFunctionalID):
        self.objectType = "Agent"
        self.name = name
        self.inverseFunctionalID = inverseFunctionalID
    
    def dict(self):
        if self.name is not None:
            theDict['name'] = self.name
        if self.inverseFunctionalID is not None:
            theDict = dict(theDict.items() + self.inverseFunctionalID.dict().items())
        
    def json(self):
        theDict = self.dict()
        
        return json.dumps(theDict, encoding="UTF-8")
                
                
class LanguageMap:
    def __init__(self, mapping):
        self.mapping = mapping
        
    def dict(self):
        return self.mapping
    

class Extension:
    def __init__(self, mapping):    
        self.mapping = mapping
        
    def dict(self):
        return self.mapping


class Verb:
    def __init__(self, id, display):
        self.id = id
        self.display = display
        
    def json(self):
        theDict = {}
        theDict['id'] = self.id
        if(self.display is not None):
            theDict['display'] = self.display.dict()
        
        return json.dumps(theDict, encoding="UTF-8")


class StatementReference:
    def __init__(self, id):
        self.id = id
        self.objectType = 'StatementRef'
        
        
class Activity:
    def __init__(self, id):
        self.objectType = 'Activity'
        self.id = id
        

class Result:
    def __init__(self, score, success, completion, response, duration, extensions):
        self.score = score
        self.success = success
        self.completion = completion
        self.response = response
        self.duration = duration
        self.extensions = extension
        
    def dict(self):
        theDict = {}
        
        if self.success is not None:
            theDict['success'] = self.success
        if self.completion is not None:
            theDict['completion'] = self.completion
        if self.response is not None:
            theDict['response'] = self.response
        if self.duration is not None:
            theDict['duration'] = self.duration
            
        return theDict;
    
    
        
theAccount = Account('robbie', 'robbie.com')
print theAccount.json()


theID = InverseFunctionalID('mailto:one@one.com', None, None, None)
print theID.dict()
print theID.json()

mapping = {'en' : 'english', 'es' : 'espanyol'}
theLM = LanguageMap(mapping)
print theLM.dict()