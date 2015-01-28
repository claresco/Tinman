import LRSRequest
import TestingUtility as tu
import json

# Full Activity Object GET
# Base case

# input activityID you want to look up

GET = 'get'
PUT = 'put'
POST = 'post'
GET_PATH = 'activities'
PATH = 'activities/profile'

'''
    Test getting a non-existent activity
'''
def test1():
    print 'Full Activity Object GET'
    activityID = 'non-existent-activity-id'

    headers = {}
    params = {'activityId' : activityID}

    tu.printHeadline(1, 'Non existing activity')

    tu.printResult(tu.runTest(GET_PATH, GET, headers, params, 404))
    
    
    
'''
    getting a existing activity
'''
def test2():
    # Activity ID has to be in the database
    activityID = 'http://moodledemo.claresco.com/moodle/question/preview.php?id=1361'

    headers = {}
    params = {'activityId' : activityID}

    tu.printHeadline(2, 'Existing activity')
    tu.printResult(tu.runTest(GET_PATH, GET, headers, params, 200))
    
    
    
'''
    empty activity id 
'''
def test3():
    headers = {}
    params = {}

    tu.printHeadline(3, 'empty activityId param')
    tu.printResult(tu.runTest(GET_PATH, GET, headers, params, 400))
    
    
    
'''
    method other than get
'''
def test4():
    activityID = 'http://moodledemo.claresco.com/moodle/question/preview.php?id=1361'

    headers = {}
    params = {'activityId' : activityID}

    # returns 404
    tu.printHeadline('4a', 'PUT method')
    tu.printResult(tu.runTest(GET_PATH, PUT, headers, params, 404))
    
    tu.printHeadline('4b', 'POST method')
    tu.printResult(tu.runTest(GET_PATH, POST, headers, params, 404))
    
    
    
'''
    put activity profile and then retrieve it
'''
def test5():
    activityID = 'http://lrs:8080/tincan/activities/p300'
    profileID = 'test1'
    document = 'this activity is defined under !@#$%^&'
    
    headers = {}
    params = {'activityId' : activityID, 'profileId' : profileID}
    
    tu.printHeadline('5a', 'Put activity profile')
    tu.printResult(tu.runTest(PATH, PUT, headers, params, 204, data=document))
    
    tu.printHeadline('5b', 'Get activity profile')
    tu.printResult(tu.runTest(PATH, GET, headers, params, 200, expectedText=document))
    
    
'''
    POST activity profile and then retrieve it
'''
def test6():
    activityID = 'http://lrs:8080/tincan/activities/p301'
    profileID = 'test2'
    document = 'this activity is for testing purposes only'
    
    headers = {}
    params = {'activityId' : activityID, 'profileId' : profileID}
    
    tu.printHeadline('6a', 'Post activity profile')
    tu.printResult(tu.runTest(PATH, POST, headers, params, 204, data=document))
    
    tu.printHeadline('6b', 'Get activity profile')
    tu.printResult(tu.runTest(PATH, GET, headers, params, 200, expectedText=document))


'''
    Updating activity profile from previous profile and then retrieve it
'''
def test7():
    activityID = 'http://lrs:8080/tincan/activities/p301'
    profileID = 'test2'
    document = 'an updated activity profile'
    
    headers = {}
    params = {'activityId' : activityID, 'profileId' : profileID}
    
    tu.printHeadline('7a', 'Updating activity profile')
    tu.printResult(tu.runTest(PATH, PUT, headers, params, 204, data=document))
    
    tu.printHeadline('7b', 'Get activity profile')
    tu.printResult(tu.runTest(PATH, GET, headers, params, 200, expectedText=document))
    

'''
    Making a PUT or POST request which missing a parameter
'''
def test8():
    activityID = 'http://lrs:8080/tincan/activities/p301'
    profileID = 'test2'
    document = 'an updated activity profile'
    
    headers = {}
    params = {'activityId' : activityID}
    
    tu.printHeadline('8a', 'Missing profileId -- PUT')
    tu.printResult(tu.runTest(PATH, PUT, headers, params, 400, data=document))
    
    tu.printHeadline('8b', 'Missing profileId -- POST')
    tu.printResult(tu.runTest(PATH, POST, headers, params, 400, data=document))
    
    params = {'profileId' : profileID}
    
    tu.printHeadline('8c', 'Missing activityId -- PUT')
    tu.printResult(tu.runTest(PATH, PUT, headers, params, 400, data=document))
    
    tu.printHeadline('8d', 'Missing activityId -- POST')
    tu.printResult(tu.runTest(PATH, POST, headers, params, 400, data=document))
    
    params = {}
    
    tu.printHeadline('8e', 'Missing both -- PUT')
    tu.printResult(tu.runTest(PATH, PUT, headers, params, 400, data=document))
    
    tu.printHeadline('8f', 'Missing both -- POST')
    tu.printResult(tu.runTest(PATH, POST, headers, params, 400, data=document))
    
    
'''
    Retrieving non-existing profile
'''
def test9():
    activityID = 'http://lrs:8080/tincan/activities/p399'
    profileID = 'test2567'
    
    headers = {}
    params = {'activityId' : activityID, 'profileId' : profileID}
    
    tu.printHeadline('9', 'Retrieving non-existing activity profile')
    tu.printResult(tu.runTest(PATH, GET, headers, params, 404))
    
    
'''
    Testing multiple get request
'''
def test10():
    activityID = 'http://lrs:8080/tincan/activities/p301'
    profileID = 'test2'
    
    headers = {}
    params = {'activityId' : activityID}
    
    result = [profileID]
    
    tu.printHeadline('10', 'Retrieving multiple profiles')
    
    tu.printResult(tu.runTest(PATH, GET, headers, params, 200, expectedText=json.dumps(result)))
    

def run():
    print 'Activity Profile API'
    #test1()
    #test2()
    #test3()
    #test4()
    
    print 'Storing and retrieving Activity Profile'
    #test5();
    #test6();
    #test7();
    #test8();
    #test9();
    test10();