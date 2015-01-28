'''
 This class is to make request to LRS
'''

import LRS
import base64
import requests
import urllib

myEndpoint = LRS.endpoint
myUsername = LRS.username
myPassword = LRS.password


def isLRSUp():
    try:
        response = requests.get(LRS.baseURL)
        return response.status_code is 200
    except requests.exceptions.ConnectionError:
        return False


'''
 Make request to LRS
'''
def makeRequest(path, method, headers, params, data=None):
    # Get the default header
    theHeaders = createDefaultHeader()
    
    # merge the two header dictionary 
    theHeaders = dict(theHeaders.items() + headers.items())
    
    # add authorization header to the headers list
    theHeaders['Authorization'] = createAuthHeader(myUsername, myPassword)
    
    print 'the headers are ' + str(theHeaders)
    print 'the params are ' + str(params)
    
    method = method.lower()
    
    if method == 'get':
        theLRSResponse = requests.get(myEndpoint + path, params=params, headers=theHeaders, data=data)
    elif method == 'put':
        theLRSResponse = requests.put(myEndpoint + path, params=params, headers=theHeaders, data=data)
    elif method == 'post':
        theLRSResponse = requests.post(myEndpoint + path, params=params, headers=theHeaders, data=data)
    else:
        raise LRSRequestError('request\'s method is not supported')
    
    return theLRSResponse
   
   
'''
 This function takes username and password and then
 encode it to base 64 separated by a comma and preceded by the word 'Basic'
'''
def createAuthHeader(username, password):
    if(username is not None and password is not None):
        return 'Basic ' + base64.b64encode(username) + ":" + base64.b64encode(password)
    else:
        raise LRSRequestError('username or password of LRS is null')
    
    
    
def makeIE8Request(path, method, headers, params, data=None):
    # param is the method
    theParam = {'method' : method.upper()}
    
    if data is not None:
        params['content'] = data
    
    # default headers
    theDefaultHeaders = createDefaultHeader()
    headers = dict(theDefaultHeaders.items() + headers.items())
    
    theBody = dict(headers.items() + params.items())
    
    theRequestBody = urllib.urlencode(theBody)

    theLRSResponse = requests.post(myEndpoint + path, params=theParam, data=theRequestBody)
    
    return theLRSResponse


def makeBothTypeRequest(path, method, headers, params):
    makeRequest(path, method, headers, params)
    makeIE8Request(path, method, headers, params)
    

def createDefaultHeader():
    myVersion = '1.0.1'

    myContentType = 'application/json'

    myHeaders = {'X-Experience-API-Version' : myVersion, 'content-type' : myContentType}
    return myHeaders



class LRSRequestError(Exception):
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return repr(self.value)
    


#response = requests.put('http://lrs:8080/xapi/' + 'activities/profile', params={}, headers={}, data=None)





'''
try:
    theResponse = makeRequest('statements', 'get', {'a' : 'b'}, {})
    print theResponse.encoding
    print theResponse.content
    print theResponse.status_code
    
    theResponse = makeIE8Request('statements', 'get', {'a' : 'b'}, {})
    print theResponse.encoding
    print theResponse.content
    print theResponse.status_code
    
except requests.exceptions.ConnectionError:
    print 'problem with connection'
except LRSRequestError as e:
    print e.value
'''