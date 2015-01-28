import LRSRequest

def printHeadline(counter, message):
    print 'Test ' + str(counter) + ' -- ' + message


def printResult(boolResult):
    if boolResult:
        print 'Result : success\n\n'
    else:
        print 'Result : failure\n\n'

        
def isTestSuccessful(response, expectedStatusCode, expectedText=None):
    print 'get : ' + str(response.status_code) + ', expected: ' + str(expectedStatusCode)
    
    result = response.status_code == expectedStatusCode
    
    if(expectedText is not None):
        print 'get : ' + response.text + ', expected: ' + expectedText
        result = result and response.text.strip() == expectedText.strip() 
    
    return result


def printDivider():
    print '==============================================================='
    
'''    
def runTest(path, method, headers, params, expectedCode):
    result = True
    
    response = LRSRequest.makeRequest(path, method, headers, params)
    result = result and isTestSuccessful(response, expectedCode)
    response = LRSRequest.makeIE8Request(path, method, headers, params)
    result = result and isTestSuccessful(response, expectedCode)
    
    return result


def runTest(path, method, headers, params, expectedCode, expectedText):
    result = True
    
    response = LRSRequest.makeRequest(path, method, headers, params)
    result = result and isTestSuccessful(response, expectedCode, expectedText)
    response = LRSRequest.makeIE8Request(path, method, headers, params)
    result = result and isTestSuccessful(response, expectedCode, expectedText)
    
    return result

def runTest(path, method, headers, params, data, expectedCode):
    result = True
    
    response = LRSRequest.makeRequest(path, method, headers, params, data)
    result = result and isTestSuccessful(response, expectedCode)
    response = LRSRequest.makeIE8Request(path, method, headers, params, data)
    result = result and isTestSuccessful(response, expectedCode)
    
    return result
'''

def runTest(path, method, headers, params, expectedCode, data=None, expectedText=None):
    result = True
    
    response = LRSRequest.makeRequest(path, method, headers, params, data)
    result = result and isTestSuccessful(response, expectedCode, expectedText)
    response = LRSRequest.makeIE8Request(path, method, headers, params, data)
    result = result and isTestSuccessful(response, expectedCode, expectedText)
    
    return result


def readFile(fileName, mode='r'):
    f = open(fileName, mode)
    print f.read()