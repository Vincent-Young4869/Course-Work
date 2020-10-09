import numpy as np
import math as mt
class LogisticRegression(object):
	def __init__(self,updateRate,dataSet):
		self.weights = np.ones(dataSet.shape[1]-1)
		self.updateRate = updateRate
	def fit(self,inputData,resultData):

		def featureSum(weights,inputData,resultData):
		
			def alpha(result):
				if result >= 0:
					return 1/(1+mt.exp(-result))
				else:
					return 1-1/(1+mt.exp(result))    
		
			zeros = np.zeros(inputData.shape[1])
		
			for i in range(len(resultData)):
				weights_t = weights.reshape(weights.shape[0],1)
				zeros = zeros + inputData[i]*(resultData[i] - alpha(inputData[i].dot(weights_t)))
			return zeros
		i=0
		updateRateN = self.updateRate
		oldWeights =  self.weights
		newWeights = oldWeights + self.updateRate * (featureSum(oldWeights,inputData,resultData))
		#while np.amax(np.abs(np.subtract(oldWeights,newWeights))) > 0.0001 :
		for i in range(5000):
			updateRateN = self.updateRate/((i+1))
			oldWeights = newWeights
			newWeights = oldWeights + updateRateN * (featureSum(oldWeights,inputData,resultData))
		
		self.weights = newWeights
	
	def predict(self,inputData,trueLabel):		# split multi test sets into a few 1*m sets?
		def alpha(result):
			return 1/(1+mt.exp(-result))
		output = 1 if (alpha(inputData.dot(self.weights.T)) > 0.5) else 0
		return output == trueLabel

class LinearDiscriminantAnalysis(object):
	
	def __init__(self,input):
		
		m = input.shape[1]
		#print("m:",m)

		self.mean0, self.mean1 = np.zeros(m), np.zeros(m)
		self.n0, self.n1 = 0,0
		self.P0,self.P1 = 0,0
		self.cov_mat,self.inv_cov = np.zeros((m,m)), np.zeros((m,m))
		#self.cov_mat,self.inv_cov = 0,0

	def fit(self,inputData,resultData):		# this function takes complete data set. i.e. contain input & output in one set
		#print(resultData)
		input = np.concatenate((inputData,np.transpose(np.array([resultData]))),axis = 1)

		#print("input:\n",input)
		
		set0 = input[input[:,-1] == 0][:,:-1]
		set1 = input[input[:,-1] == 1][:,:-1]

		#print("set0:\n",set0)


		self.n0 = len(set0)
		self.n1 = len(set1)

		'''
		if (n0+n1) != len(input):
			#raise exception
		'''

		self.P0 = self.n0 / (self.n0 + self.n1)
		self.P1 = self.n1 / (self.n0 + self.n1)

	
		self.mean0 = 1/self.n0 * np.sum(set0,axis=0)  #average all columns of set0 & set1
		self.mean1 = 1/self.n1 * np.sum(set1,axis=0)  #average all columns of set0 & set1
		
		
		#cov_mat = np.cov((input[:,:-1]).T)
		
		def helper(input,mean):

			m = input.shape[1]

			sum = np.zeros((m,m))

			total = self.n0 + self.n1

			for i in range(len(input)):
				#print(sum,"\n")
				x = input[i]			# x = the i-th row of input matrix
				#print((x-mean),"\n")
				mat = x - mean
				mat_t = mat.reshape(mat.shape[0],1)
				#print(mat,"\n",mat_t)

				sum = sum + mat_t*mat/(total-2)
				#sum = sum + (np.transpose(x-mean))*(x-mean)/(total-2)
			
			return sum
		
		#print("set0,set1:",set0.shape[1], set1.shape[1])
		#print("cov_mat:",self.cov_mat.shape[1])

		self.cov_mat = self.cov_mat + helper(set0,self.mean0)
		self.cov_mat = self.cov_mat + helper(set1,self.mean1)
		
		#print("cov_mat:\n",self.cov_mat)	
		#print("det of cov_mat :",np.linalg.det(self.cov_mat))
		self.inv_cov = np.linalg.inv(self.cov_mat)

		'''
		###############################################################
		# initial method where cov_mat is scalar #

		def helper(input,mean):
			sum = 0
				#mean = comput_mean(input,output)
				#n0,n1,total = comput_num(input,output)
			total = self.n0 + self.n1
			for i in range(len(input)):
				x = input[i]			# x = the i-th row of input matrix
				sum = sum + (x-mean)*((x-mean).T)/(total-2)
			return sum
			
		self.cov_mat = self.cov_mat + helper(set0,self.mean0)
		self.cov_mat = self.cov_mat + helper(set1,self.mean1)
			
		self.inv_cov = 1/self.cov_mat
		###############################################################
		'''


	def predict(self,input,output):
		
		
		bound = (np.log(self.P1/self.P0) 
				-0.5 * np.dot(self.mean1, np.dot(self.inv_cov,np.transpose(self.mean1))) 
				+ 0.5*np.dot(self.mean0,np.dot(self.inv_cov,np.transpose(self.mean0))) 
				+ np.dot((self.mean1-self.mean0),np.dot(self.inv_cov,np.transpose(input))) )

		'''
		bound = np.log(self.P1/self.P0) - 0.5 * np.dot((self.mean1).T, self.inv_cov * self.mean1) + 0.5*np.dot((self.mean0).T,self.inv_cov * self.mean0) + np.dot(input.T,self.inv_cov * (self.mean0-self.mean1)) 
		'''

		if bound > 0 :		# assume 2 represents y=0 branch. 4 represents y=1 branch
			result = 1
		else:
			result = 0
		

		return result==output
			



