import LRSRequest
import TestingUtility as tu
import XapiStatement as s
import json


GET = 'get'
PUT = 'put'
POST = 'post'
PATH = 'activities/state'



'''
    Storing and retrieving state API
'''
def test1():
    activityID = 'theActivityID'
    stateID = 'stateId'
    
    id = s.InverseFunctionalID(mbox='email@domain.com');
    agent = s.Agent('name', id);
    
    document = 'page 9'
    
    headers={}
    
    params={'activityId' : activityID, 'stateId' : stateID, 'agent' : agent.json()}
    
    tu.printHeadline('1a', 'Put state')
    tu.printResult(tu.runTest(PATH, PUT, headers, params, 204, data=document))
    
    tu.printHeadline('1b', 'Retrieve state')
    tu.printResult(tu.runTest(PATH, GET, headers, params, 200, expectedText=document))
    
    stateID = 'stateId2'
    document = 'this agent is subject to testing again and again'
    
    params={'activityId' : activityID, 'stateId' : stateID, 'agent' : agent.json()}
    
    tu.printHeadline('1c', 'POST state')
    tu.printResult(tu.runTest(PATH, POST, headers, params, 204, data=document))
    
    tu.printHeadline('1d', 'Retrieve state')
    tu.printResult(tu.runTest(PATH, GET, headers, params, 200, expectedText=document))

    
    
def test2():
    activityID = 'theActivityID'
    stateID = 'stateId'
    
    id = s.InverseFunctionalID(mbox='email@domain.com');
    agent = s.Agent('name', id);
    
    document = 'page 11'
    
    headers={}
    
    params={'activityId' : activityID, 'stateId' : stateID, 'agent' : agent.json()}

    tu.printHeadline('2a', 'Put state to update document')
    tu.printResult(tu.runTest(PATH, PUT, headers, params, 204, data=document))
    
    tu.printHeadline('1b', 'Retrieve state')
    tu.printResult(tu.runTest(PATH, GET, headers, params, 200, expectedText=document))
    
    
    
def test21():
    id = s.InverseFunctionalID(mbox='email@domain.com');
    agent = s.Agent('name', id);
    
    document = 'page 9'
    
    headers={}
    
    params={'agent' : agent.json()}
    
    tu.printHeadline('21a', 'Put state without the required params')
    tu.printResult(tu.runTest(PATH, PUT, headers, params, 400, data=document))
    
    tu.printHeadline('21b', 'POST state without the required params')
    tu.printResult(tu.runTest(PATH, POST, headers, params, 400, data=document))

    
    
def run():
    test1()
    test2()
    
    test21()