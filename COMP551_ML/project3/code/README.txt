* packages used in the code files:

cv2
numpy
pandas
gzip
pickle
csv
matplotlib.pyplot
from sklearn.model_selection import train_test_split
from scipy import ndimage
heapq 
from keras.optimizers import SGD, Adam, RMSprop
from keras.utils import np_utils
from keras.preprocessing.image import ImageDataGenerator
from sklearn.model_selection import KFold
argparse
random
from keras.models import Sequential
from keras.layers import Dense, Dropout, Activation, BatchNormalization
from keras.layers import Conv2D
from keras.layers.pooling import MaxPooling2D
from keras.layers import Flatten
from keras import regularizers
keras

* Instructions:

Lenet file: 
implemented two lenet models, one is the original and one is the improved. Also preprocessing steps are included: cv2.threshold, image_division, normalization. If want to test model on "modified image set" refered in our report, use "crop_img_set" variable, otherwise use "train_threshold" variable.

VGG file:
implementation of vgg model. Basically we use "train_images" as input, if one wants to test on preprocessed images, use "train_threshold" as input.

Alexnet file:
Simply read in the csv file and run, we did not contain "modified image set" preprocessing step in this file, as is said in the report, since the model performance is not very well and we did not use this model for further evaluation.