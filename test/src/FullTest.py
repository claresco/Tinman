import sys

# Utility modules
import LRSRequest
import TestingUtility as tu

# Module with test cases
import ActivityProfileAPI as acP
import AgentProfileAPI as agP
import StateAPI as st
import StatementAPI as s


def printHeadline(counter, message):
    tu.printHeadline(counter, message)

def printResult(boolResult):
    tu.printResult(boolResult)
        
def isTestSuccessful(response, expectedStatusCode):
    return tu.isTestSuccessful(response, expectedStatusCode)



# Test if LRS is reachable, if not quit
printHeadline(0, 'Testing if LRS is up')
if not LRSRequest.isLRSUp():
    sys.exit('LRS is unreachable, exiting test')

tu.printDivider()


# Run the activity profile API test
#acP.run()
#tu.printDivider()

# Run the agent profile API test
#agP.run()
#tu.printDivider()

# Run the state API test
st.run()
tu.printDivider()

# Run the statement API test
#s.run()
#tu.printDivider()

