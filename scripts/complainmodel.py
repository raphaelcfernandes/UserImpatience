import tensorflow as tf
from tensorflow import lite
from tensorflow import keras
from keras.models import Sequential
from keras.layers import Dense, Dropout, Flatten
from keras.layers import Activation
from tensorflow.keras import layers
from keras.optimizers import SGD
import numpy as np
#import sklearn
#from sklearn.model_selection import train_test_split
import os
import math



class complainpredict:

    def getPrediction(filename):

        path = ''

        k = 1
        X, Y = [], []
        col1, col2 = [], []
        j = 0

        if (os.path.getsize(path + filename) == 0): return 2
        with open(path + filename) as csvfile:
            for line in csvfile.readlines():
                array = line.split(',')
                col1.append(float(array[0]))
                col2.append([float(np.mean(col1)), np.std(col1)])


        num_samples = 0
        if(len(col1) < 26): num_samples = len(col1)
        else: num_samples = 26
        for i in range(len(col1) - num_samples, len(col1) - k):
            if(col1[i] != None):
                X.append(col2[i])
                Y.append(col1[i])
            else: break
        X = np.asarray(X)
        Y = np.asarray(Y)

        #x_train, x_test,y_train, y_test = train_test_split(X, Y, test_size=0.2)

        input_shape = (k+1,)

        model = Sequential()
        model.add(Dense(16, input_shape=input_shape, kernel_initializer='he_uniform'))
        model.add(Activation('tanh'))
        model.add(Dense(8))
        model.add(Activation('softmax'))
        model.add(Dense(1, activation='linear'))
        model.compile(loss='mean_squared_error', optimizer=SGD(lr=0.01, momentum=0.7),
                      metrics=['accuracy'])


        epochs = 20
        batch_size = 8
        # Fit the model weights.
        history = model.fit(X, Y,
                            batch_size=batch_size,
                            epochs=epochs)
        #,
        #verbose=1,
        #validation_data=(x_test, y_test)

        #converter = lite.TFLiteConverter.from_keras_model(model)
        #tflite_model = converter.convert()
        #f = open('litemodel.tflite', 'w')
        #f.write(tflite_model)


        #model.summary()
        #plt.plot(history.history['accuracy'])
        #plt.plot(history.history['val_accuracy'])
        #plt.title('model accuracy')
        #plt.ylabel('accuracy')
        #plt.xlabel('epoch')
        #plt.legend(['train', 'test'], loc='upper left')
        #plt.show()

        #plt.plot(history.history['loss'])
        #plt.plot(history.history['val_loss'])
        #plt.title('model loss')
        #plt.ylabel('loss')
        #plt.xlabel('epoch')
        #plt.legend(['train', 'test'], loc='upper left')
        #plt.show()

        predictiondata = np.asarray([[(np.mean(Y)), np.std(Y)]])

        print(model.predict(predictiondata))
        prediction = model.predict(predictiondata)
        return math.floor(prediction)

    getPrediction('chrome2.txt')
