import os

#print("Sequential particle count from 2 to 10")
#for i in xrange(2,11):
#	print str(i) + " bodies"
#	os.system("java nBody_seq " + str(i) + " 5 100000 1000 false")
#
#
#print("Sequential particle count from 2 to 10 with graphics")
#for i in xrange(2,11):
#	print str(i) + " bodies"
#	os.system("java nBody_seq "+ str(i) + " 5 100000 1000 true")
#

print("Parallel particle count from 2 to 10")
i=1
while i <=8:
	print(str(i)+ " worker threads")
	for j in xrange(2,11):
		print str(j) + " bodies"
		os.system("java nBody_par "+ str(i) +" " + str(j) +" 5 100000 1000 false")
	i *= 2
	
#
#print("Parallel particle count from 2 to 10 with graphics")
#i=1
#while i <=8:
#	print(str(i)+ " worker threads")
#	for j in xrange(2,11):
#		print str(j) + " bodies"
#		os.system("java nBody_par "+ str(i) +" " + str(j) +" 5 100000 1000 true")
#	i *= 2