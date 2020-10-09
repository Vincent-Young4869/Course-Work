import csv
import numpy as np
from model import LogisticRegression
from model import LinearDiscriminantAnalysis



def evaluate_acc(trainModel,validData):
	testResults = []
	for i in range(len(validData)):
		testResults.append(trainModel.predict(validData[i,0:-1],validData[i,-1]))
	n = 0
	for i in testResults:
		if i:
			n = n + 1
	print ('the accuracy is ',n/len(testResults) * 100,' %')
	return n/len(testResults) * 100
	
def crossValidation(dataSet,trainModel):
	numOfData = len(dataSet)
	numOfDataPerSub = numOfData // 5 + 1
	setNum = ((0,numOfDataPerSub - 1),(numOfDataPerSub, 2 * numOfDataPerSub - 1) ,(2 * numOfDataPerSub , 3 * numOfDataPerSub - 1),(3 * numOfDataPerSub, 4 * numOfDataPerSub - 1),(4 * numOfDataPerSub, len(dataSet) - 1))
	sumAccuracy = 0
	for i in setNum:
		print('fold ',setNum.index(i),' start training:')
		trainModel.fit(np.concatenate((dataSet[0:i[0],0:-1],dataSet[i[1]+1:len(dataSet),0:-1]),axis = 0),np.concatenate((dataSet[0:i[0],-1],dataSet[i[1]+1:len(dataSet),-1]),axis = 0))
		sumAccuracy = sumAccuracy + evaluate_acc(trainModel,dataSet[i[0]:i[1] + 1,:])
	sumAccuracy = sumAccuracy / 5
	print('the total accuracy is ',sumAccuracy,' %')
	return sumAccuracy
	
wines = []
with open('winequality-red.csv','r') as f:
	wines = list(csv.reader(f,delimiter=";"))
wines = np.array(wines[1:],dtype=np.float)
winesOnes = np.ones((len(wines),1))
wines = np.concatenate((winesOnes,wines),axis = 1)
for i in range(wines.shape[0]):
	#wines[i:5] = wines[i:5]**(1/3)
	if wines[i,-1] > 5:
		wines[i,-1] = 1
	else:
		wines[i,-1] = 0
breastCancerList = []
with open('breast-cancer-wisconsin.data','r') as f:
	for line in f.readlines():
		if not '?' in line:
			l = list(map(int,line.strip().split(',')[1:]))
			breastCancerList.append(l)
breastCancers = np.array(breastCancerList,dtype=int)
breastOnes = np.ones((len(breastCancers),1))
breastCancers = np.concatenate((breastOnes,breastCancers),axis = 1)
for i in range(len(breastCancers)):
	breastCancers[i,-1] = (breastCancers[i,-1]-2)/2

LDA = LinearDiscriminantAnalysis(breastCancers[:,1:-1])
print('Tumors:LDA')
crossValidation(breastCancers[:,1:],LDA)

#wines1 = np.delete(wines,4,axis=1)
#wines1 = np.delete(wines1,4,axis=1)

LDA2 = LinearDiscriminantAnalysis(wines[:,1:-1])
print('Wines:LDA')
crossValidation(wines[:,1:],LDA2)


RegressionWines = LogisticRegression(0.02,wines)
print('Wines:LogisticRegression')
crossValidation(wines,RegressionWines)

RegressionBreasts = LogisticRegression(0.02,breastCancers)
print('Tumors:LogisticRegression')
crossValidation(breastCancers,RegressionBreasts)

