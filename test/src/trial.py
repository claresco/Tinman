a = {'number' : 3, 'boolean' : True, 'String' : 'woohaaa'}

print a['number']
print a['boolean']
print a['String']

def something():
    return (1, False, 'something else entirely')

#a, b, c = something()
a, b = something()

print a 
#print b
#print c