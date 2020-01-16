'''
Ejemplo de entrenamiento de un percepton para reconocer MNIST
v1.0 14-Dic-2018
Juan D. Tardos
'''

import keras
from keras.datasets import mnist
from keras.models import Sequential
from keras.layers import Dense, Dropout, Flatten
from keras.layers import Conv2D, MaxPooling2D
from keras.optimizers import RMSprop, Adam, SGD
from keras.callbacks import EarlyStopping
from keras import backend as K
import time
#from sklearn.metrics import confusion_matrix
import numpy as np
#import matplotlib.pyplot as plt
import P5_utils

verbose = True

print('Loading MNIST dataset...')
# Problem dimensions
img_rows, img_cols = 28, 28
num_pixels = img_rows * img_cols
num_classes = 10
# The data, split between train and test sets
(x_train, y_train), (x_test, y_test) = mnist.load_data()
x_train = x_train.reshape(60000, num_pixels)
x_test = x_test.reshape(10000, num_pixels)
x_train = x_train.astype('float32') / 255
x_test = x_test.astype('float32') / 255
print(x_train.shape[0], 'train samples')
print(x_test.shape[0], 'test samples')
# convert class vectors to binary class matrices
y_train = keras.utils.to_categorical(y_train, num_classes)
y_test  = keras.utils.to_categorical(y_test,  num_classes)
# Random permutation of training data
np.random.seed(0)
p = np.arange(x_train.shape[0])
np.random.shuffle(p)
x_train = x_train[p]
y_train = y_train[p]

# Función para parar cuando ya no mejora el error de validacion
earlystop=EarlyStopping(monitor='val_loss', patience=5, 
                        verbose=1, mode='auto')

"""
    Red neuronal sin capas ocultas y funcion de activacion softmax en la salida
"""
t_total0 = time.time()
print("Entrenando red neuronal sin capas ocultas, funcion de activacion softmax en la salida y batch de 16 muestras")
model = Sequential()
model.add(Dense(10, activation='softmax', input_shape=(num_pixels,)))
# Con dropout
model.compile(
              loss='categorical_crossentropy',
              optimizer=SGD(),
              metrics=['accuracy'])

model.summary()

#print('Training the NN....')
t0 = time.time()
history = model.fit(x_train, y_train,
                    batch_size=128,      # Batch de tamaño pequeño
                    epochs=20,
                    validation_split=0.1,
                    #callbacks=[earlystop],
                    verbose=verbose)

train_time = time.time() - t0
print('%s %.3f%s' %  ('Training time: ', train_time, 's') )
P5_utils.plot_history(history)

# Evaluar la red
train_score = model.evaluate(x_train, y_train, verbose=0)
test_score = model.evaluate(x_test, y_test, verbose=0)
print('%s %2.2f%s' % ('Accuracy train: ', 100*train_score[1], '%' ))
print('%s %2.2f%s' % ('Accuracy test:  ', 100*test_score[1], '%'))

y_pred = model.predict(x_test)
P5_utils.plot_mnist_confusion_matrix(y_test, y_pred)

t_total1= time.time() - t_total0
print('%s %.3f%s' %  ('Tiempo total: ', t_total1, 's') )



"""
    Red neuronal con 1 capa oculta
"""
t_total0 = time.time()
print("Entrenando red neuronal con 1 capa oculta (750) y batch de 128 muestras")
model = Sequential()
model.add(Dense(750, activation='relu', input_shape=(num_pixels,)))
model.add(Dense(10, activation='softmax'))
# Con dropout
model.compile(
              loss='categorical_crossentropy',
              optimizer=SGD(),
              metrics=['accuracy'])

model.summary()

#print('Training the NN....')
t0 = time.time()
history = model.fit(x_train, y_train,
                    batch_size=128,      # Batch de tamaño pequeño
                    epochs=20,
                    validation_split=0.1,
                    #callbacks=[earlystop],
                    verbose=verbose)

train_time = time.time() - t0
print('%s %.3f%s' %  ('Training time: ', train_time, 's') )
P5_utils.plot_history(history)

# Evaluar la red
train_score = model.evaluate(x_train, y_train, verbose=0)
test_score = model.evaluate(x_test, y_test, verbose=0)
print('%s %2.2f%s' % ('Accuracy train: ', 100*train_score[1], '%' ))
print('%s %2.2f%s' % ('Accuracy test:  ', 100*test_score[1], '%'))

y_pred = model.predict(x_test)
P5_utils.plot_mnist_confusion_matrix(y_test, y_pred)

t_total1= time.time() - t_total0
print('%s %.3f%s' %  ('Tiempo total: ', t_total1, 's') )

"""
    Red neuronal con 2 capas ocultas
"""
t_total0 = time.time()
print("Entrenando red neuronal con 2 capas ocultas (90,100) con dropout en la primera capa oculta y batch de 16 muestras")
model = Sequential()
model.add(Dense(90, activation='relu', input_shape=(num_pixels,)))
model.add(Dropout(0.2))
model.add(Dense(100, activation='relu'))
model.add(Dense(10, activation='softmax'))
# Con dropout
model.compile(
              loss='categorical_crossentropy',
              optimizer=SGD(),
              metrics=['accuracy'])

model.summary()

#print('Training the NN....')
t0 = time.time()
history = model.fit(x_train, y_train,
                    batch_size=16,      # Batch de tamaño pequeño
                    epochs=20,
                    validation_split=0.1,
                    #callbacks=[earlystop],
                    verbose=verbose)

train_time = time.time() - t0
print('%s %.3f%s' %  ('Training time: ', train_time, 's') )
P5_utils.plot_history(history)

# Evaluar la red
train_score = model.evaluate(x_train, y_train, verbose=0)
test_score = model.evaluate(x_test, y_test, verbose=0)
print('%s %2.2f%s' % ('Accuracy train: ', 100*train_score[1], '%' ))
print('%s %2.2f%s' % ('Accuracy test:  ', 100*test_score[1], '%'))

y_pred = model.predict(x_test)
P5_utils.plot_mnist_confusion_matrix(y_test, y_pred)

t_total1= time.time() - t_total0
print('%s %.3f%s' %  ('Tiempo total: ', t_total1, 's') )

"""
    Red convolucional
"""

batch_size = 128
num_classes = 10
epochs = 12

(x_train, y_train), (x_test, y_test) = mnist.load_data()

if K.image_data_format() == 'channels_first':
    x_train = x_train.reshape(x_train.shape[0], 1, img_rows, img_cols)
    x_test = x_test.reshape(x_test.shape[0], 1, img_rows, img_cols)
    input_shape = (1, img_rows, img_cols)
else:
    x_train = x_train.reshape(x_train.shape[0], img_rows, img_cols, 1)
    x_test = x_test.reshape(x_test.shape[0], img_rows, img_cols, 1)
    input_shape = (img_rows, img_cols, 1)

x_train = x_train.astype('float32')
x_test = x_test.astype('float32')
x_train /= 255
x_test /= 255

# convert class vectors to binary class matrices
y_train = keras.utils.to_categorical(y_train, num_classes)
y_test = keras.utils.to_categorical(y_test, num_classes)

model = Sequential()
model.add(Conv2D(32, kernel_size=(3, 3),
                 activation='relu',
                 input_shape=input_shape))
model.add(Conv2D(64, (3, 3), activation='relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))
model.add(Dropout(0.25))
model.add(Flatten())
model.add(Dense(128, activation='relu'))
model.add(Dropout(0.5))
model.add(Dense(num_classes, activation='softmax'))

model.compile(loss=keras.losses.categorical_crossentropy,
              optimizer=keras.optimizers.Adadelta(),
              metrics=['accuracy'])

model.fit(x_train, y_train,
          batch_size=batch_size,
          epochs=epochs,
          verbose=1)
score = model.evaluate(x_test, y_test, verbose=0)
print('Test loss:', score[0])
print('Test accuracy:', score[1])
