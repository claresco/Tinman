import LRSRequest
import TestingUtility as tu
import json

GET = 'get'
PUT = 'put'
POST = 'post'

PATH = 'basic/request'

directory = '/Users/rheza/Documents/workspace/TinmanPythonTestSuite/json/BasicAuthorization/'


def getAuthorization(body):
    params = {}
    headers = {}
    
    response = LRSRequest.makeRequest(PATH, POST, headers, params, body)
    keySecret = json.loads(response.text)
    
    login = keySecret['login']
    password = keySecret['password']     
    return login,password





'''
    Test Authorization given a correct input
'''

def test1():
    fileName = 'BareMinimum.json'
    body = tu.readFile(directory + fileName)
    login, password = getAuthorization(body)
    
    

'''
    Test Authorization given bad input 
'''





def run():
    test1()
    
    
run()