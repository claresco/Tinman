import LRSRequest
import XapiStatement as s
import TestingUtility as tu

GET = 'get'
PUT = 'put'
POST = 'post'
PATH = 'agents/profile'



'''
    Store agent profile and then retrieve it
'''
def test1():
    id = s.InverseFunctionalID(mbox='mailto:me@abc.com')
    agent = s.Agent('my name', id)
    
    profileID = 'test1'
    document = 'this agent is subject to testing'
    
    headers = {}
    params = {'agent' : agent.json(), 'profileId' : profileID}
    
    tu.printHeadline('1a', 'Put agent profile')
    tu.printResult(tu.runTest(PATH, PUT, headers, params, 204, data=document))
    
    tu.printHeadline('1b', 'Get agent profile')
    tu.printResult(tu.runTest(PATH, GET, headers, params, 200, expectedText=document))
    
    profileID = 'test2'
    document = 'this agent is subject to testing again and again'
    
    params = {'agent' : agent.json(), 'profileId' : profileID}
    
    tu.printHeadline('1c', 'POST agent profile')
    tu.printResult(tu.runTest(PATH, POST, headers, params, 204, data=document))
    
    tu.printHeadline('1d', 'Get agent profile')
    tu.printResult(tu.runTest(PATH, GET, headers, params, 200, expectedText=document))
    
    
'''
    
'''
def test2():
    id = s.InverseFunctionalID('mailto:me@abc.com', None, None, None)
    agent = s.Agent('my name', id)
    


'''

'''
def test3():
    pass    


def run():
    test1();
