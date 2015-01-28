import TestingUtility as tu

import uuid

GET = 'get'
PUT = 'put'
POST = 'post'
PATH = 'statements'
directory = '/Users/rheza/Documents/workspace/TinmanPythonTestSuite/json/statements/'  
fileName = 'BareMinimum.json'
tu.readFile(directory + fileName)


def getID():
    statementID = uuid.uuid4()
    params = {'statementId' : statementID}
    return params


def test0():
    pass

def test1():
    params = getID()
    headers = {}
    
    fileName = 'BareMinimum.json'
    document = tu.readFile(directory + fileName)
    
    tu.printHeadline('1a', 'Put Statement')
    tu.printResult(tu.runTest(PATH, PUT, {}, params, 204, data=document))

    tu.printHeadline('1b', 'Retrieve the statement')
    tu.printResult(tu.runTest(PATH, GET, headers, params, 200, expectedText=document))
    
    params = {}
    fileName = 'ArrayWithBareMinimum.json'
    document = tu.readFile(directory + fileName)
    
    tu.printHeadline('2a', 'POST Statement')
    tu.printResult(tu.runTest(PATH, POST, {}, params, 204, data=document))

    tu.printHeadline('2b', 'Retrieve the statement')
    tu.printResult(tu.runTest(PATH, GET, headers, params, 200, expectedText=document))
    
    
def test2():
    headers = {}
    params = getID()
    fileName = 'WithResult.json'
    document = tu.readFile(directory + fileName)
    
    tu.printHeadline('2a', 'Put Statement with result')
    tu.printResult(tu.runTest(PATH, PUT, {}, params, 204, data=document))

    tu.printHeadline('2b', 'Retrieve the statement')
    tu.printResult(tu.runTest(PATH, GET, headers, params, 200, expectedText=document))
    
    
def test3():
    headers = {}
    params = getID()
    
    fileName = 'MostPropertiesAvailable.json'
    document = tu.readFile(directory + fileName)
    
    tu.printHeadline('3a', 'Put Statement with most properties available')
    tu.printResult(tu.runTest(PATH, PUT, {}, params, 204, data=document))

    tu.printHeadline('3b', 'Retrieve the statement')
    tu.printResult(tu.runTest(PATH, GET, headers, params, 200, expectedText=document))
    
    
def test32():
    params = getID()
    fileName = 'MissingVerb.json'
    document = tu.readFile(directory + fileName)
    
    tu.printHeadline('32', 'Put Statement missing a verb')
    tu.printResult(tu.runTest(PATH, PUT, {}, params, 400, data=document))
    
    
def test33():
    params = getID()
    fileName = 'MissingObject.json'
    document = tu.readFile(directory + fileName)
    
    tu.printHeadline('33', 'Put Statement missing an object')
    tu.printResult(tu.runTest(PATH, PUT, {}, params, 400, data=document))


def test34():
    params = getID()
    fileName = 'EmptyObject.json'
    document = tu.readFile(directory + fileName)
    
    tu.printHeadline('34', 'PUT empty json object')
    tu.printResult(tu.runTest(PATH, PUT, {}, params, 400, data=document))


def test35():
    params = getID()
    fileName = 'EmptyObject.json'
    document = tu.readFile(directory + fileName)
    
    tu.printHeadline('35', 'PUT null')
    tu.printResult(tu.runTest(PATH, PUT, {}, params, 400, data=document))
    
    
def test36():
    params = getID()
    fileName = 'SomeText.json'
    document = tu.readFile(directory + fileName)
    
    tu.printHeadline('36', 'PUT some random text instead of JSON')
    tu.printResult(tu.runTest(PATH, PUT, {}, params, 400, data=document))


    
def run():
    #test0()
    test1()
    test2()
    test3()
    test32()
    test33()
    test34()
    test35()
    test36()