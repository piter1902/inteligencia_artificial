# -*- coding: utf-8 -*-
"""
Created on Mon Dec 30 09:25:06 2019

@author: Pedro Tamargo Allué
"""

import numpy as np
import json
import glob
import matplotlib.pyplot as plt
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.naive_bayes import MultinomialNB
from sklearn.naive_bayes import BernoulliNB
from sklearn.utils import shuffle
from sklearn import metrics
from sklearn.model_selection import KFold

# Realizar la particion de un array
def partition(array, fold, partitions):
    # fold = nº de pliegue sobre el que dividir. Tiene que estar entre 1,partitions.
    # partitions = nº total de particiones
    # array = vector sobre el que realizar la particion
#    long_total = len(array)
    long_total = array.shape[0]
    limite_inferior = int(fold*(long_total/float(partitions))) - 1
    limite_superior = int((fold+1)*(long_total/float(partitions))) -1
    especial = [array[i] for i in range(0, long_total) if i >= limite_inferior and i < limite_superior]
#    normal   = [el for el in array if el not in especial]
    normal   = [array[i] for i in range(0, long_total) if not(i >= limite_inferior and i < limite_superior) ]
    return normal, especial

# KFold Cross Validation generico
def kfold(learner, k, examples, examples_solution):
    if str(learner).lower() == 'multinomial':
        return kfoldMultinomial(k, examples, examples_solution)
    elif str(learner).lower() == 'bernoulli':
        return kfoldBernoulli(k, examples, examples_solution)

# KFold con distribucion Multinomial
def kfoldMultinomial(k, examples, examples_solution):
    # Conversion a NumPy Array
#    array = np.array(examples)
    labels = np.array(examples_solution)
    # Variables iniciales
    best_size = 0
    best_accuracy = 0
    best_f1 = 0
    # Variamos el valor del hyper-parametro del suavizado de Laplace
    size = 0
    while size < float(5):
#        print("Size: %s" % str(size))
        f1, accuracy = 0, 0
        kfold = KFold(n_splits=k, shuffle=True, random_state=None)
        # Devuelve los indices de entrenamiento y de validacion
        for train, validation in kfold.split(examples):
#            print("TRAIN ", train, " VALIDATION: ", validation)
#            [training_set, validation_set] = partition(examples, fold, k)
#            [training_set_solutions, validation_set_solutions] = partition(examples_solution, fold, k)
            training_set = examples[train]
            training_set_solutions = labels[train]
            validation_set = examples[validation]
            validation_set_solutions = labels[validation]
            # Alpha = 0 -> Sin suavizado de Laplace
            hnb = MultinomialNB(alpha = size, fit_prior=True, class_prior=None)
            h = hnb.fit(training_set, training_set_solutions)
#            training_predicted = h.predict(training_set)
            validation_set_predicted = h.predict(validation_set)
            f1 = f1 + metrics.f1_score(validation_set_solutions, validation_set_predicted)
            accuracy = accuracy + metrics.accuracy_score(validation_set_solutions, validation_set_predicted)
        f1 = f1/k
        accuracy = accuracy/k
        if accuracy > best_accuracy:
            best_size = size
            best_accuracy = accuracy
        if f1 > best_f1:
            best_f1 = f1
        size = size + 0.1
    print("-------------------------------------")
    print("Multinomial Naive Bayes")
    print("BEST SIZE (Laplace): %s" % str(best_size))
    print("BEST ACCURACY: %s" % str(best_accuracy))
    print("BEST F1 SCORE: %s" % str(best_f1))
    print("-------------------------------------")
    nb = MultinomialNB(alpha = best_size, fit_prior=True, class_prior=None)
    return nb.fit(examples, examples_solution)

# KFold con distribucion Bernoulli
def kfoldBernoulli(k, examples, examples_solution):
    # Conversion a NumPy Array
#    array = np.array(examples)
    labels = np.array(examples_solution)
    # Variables iniciales
    best_size = 0
    best_accuracy = 0
    best_f1 = 0
    # Variamos el valor del hyper-parametro del suavizado de Laplace
    size = 0
    while size < float(5):
#        print("Size: %s" % str(size))
        f1, accuracy = 0, 0
        kfold = KFold(n_splits=k, shuffle=True, random_state=None)
        # Devuelve los indices de entrenamiento y de validacion
        for train, validation in kfold.split(examples):
#            print("TRAIN ", train, " VALIDATION: ", validation)
#            [training_set, validation_set] = partition(examples, fold, k)
#            [training_set_solutions, validation_set_solutions] = partition(examples_solution, fold, k)
            training_set = examples[train]
            training_set_solutions = labels[train]
            validation_set = examples[validation]
            validation_set_solutions = labels[validation]
            # Alpha = 0 -> Sin suavizado de Laplace
            hnb = BernoulliNB(alpha = size, fit_prior=True, class_prior=None)
            h = hnb.fit(training_set, training_set_solutions)
#            training_predicted = h.predict(training_set)
            validation_set_predicted = h.predict(validation_set)
            f1 = f1 + metrics.f1_score(validation_set_solutions, validation_set_predicted)
            accuracy = accuracy + metrics.accuracy_score(validation_set_solutions, validation_set_predicted)
        f1 = f1/k
        accuracy = accuracy/k
        if accuracy > best_accuracy:
            best_size = size
            best_accuracy = accuracy
        if f1 > best_f1:
            best_f1 = f1
        size = size + 0.1
    print("-------------------------------------")
    print("Bernoulli Naive Bayes")
    print("BEST SIZE (Laplace): %s" % str(best_size))
    print("BEST ACCURACY: %s" % str(best_accuracy))
    print("BEST F1 SCORE: %s" % str(best_f1))
    print("-------------------------------------")
    nb = BernoulliNB(alpha = best_size, fit_prior=True, class_prior=None)
    return nb.fit(examples, examples_solution)
            
"""
    Para la realizacion de las pruebas con el suavizado de Laplace.
"""
def kfold_laplace(learner, k, examples, examples_solution, laplace_value):
    if str(learner).lower() == 'multinomial':
        return kfoldMultinomial_laplace(laplace_value, k, examples, examples_solution)
    elif str(learner).lower() == 'bernoulli':
        return kfoldBernoulli_laplace(laplace_value, k, examples, examples_solution)

# KFold con distribucion Multinomial
def kfoldMultinomial_laplace(laplace, k, examples, examples_solution):
    # Conversion a NumPy Array
#    array = np.array(examples)
    labels = np.array(examples_solution)
    # Variables iniciales
    f1, accuracy = 0, 0
    kfold = KFold(n_splits=k, shuffle=True, random_state=None)
    # Devuelve los indices de entrenamiento y de validacion
    for train, validation in kfold.split(examples):
#            print("TRAIN ", train, " VALIDATION: ", validation)
#            [training_set, validation_set] = partition(examples, fold, k)
#            [training_set_solutions, validation_set_solutions] = partition(examples_solution, fold, k)
        training_set = examples[train]
        training_set_solutions = labels[train]
        validation_set = examples[validation]
        validation_set_solutions = labels[validation]
        # Alpha = 0 -> Sin suavizado de Laplace
        hnb = MultinomialNB(alpha = laplace, fit_prior=True, class_prior=None)
        h = hnb.fit(training_set, training_set_solutions)
#            training_predicted = h.predict(training_set)
        validation_set_predicted = h.predict(validation_set)
        f1 = f1 + metrics.f1_score(validation_set_solutions, validation_set_predicted)
        accuracy = accuracy + metrics.accuracy_score(validation_set_solutions, validation_set_predicted)
    f1 = f1/k
    accuracy = accuracy/k
    print("-------------------------------------")
    print("Multinomial Naive Bayes")
    print("Laplace value: %s" % str(laplace))
    print("ACCURACY: %s" % str(accuracy))
    print("F1 SCORE: %s" % str(f1))
    print("-------------------------------------")
    nb = MultinomialNB(alpha = laplace, fit_prior=True, class_prior=None)
    return nb.fit(examples, examples_solution)

# KFold con distribucion Bernoulli
def kfoldBernoulli_laplace(laplace, k, examples, examples_solution):
    # Conversion a NumPy Array
#    array = np.array(examples)
    labels = np.array(examples_solution)
    # Variables iniciales
    f1, accuracy = 0, 0
    kfold = KFold(n_splits=k, shuffle=True, random_state=None)
    # Devuelve los indices de entrenamiento y de validacion
    for train, validation in kfold.split(examples):
#            print("TRAIN ", train, " VALIDATION: ", validation)
#            [training_set, validation_set] = partition(examples, fold, k)
#            [training_set_solutions, validation_set_solutions] = partition(examples_solution, fold, k)
        training_set = examples[train]
        training_set_solutions = labels[train]
        validation_set = examples[validation]
        validation_set_solutions = labels[validation]
        # Alpha = 0 -> Sin suavizado de Laplace
        hnb = BernoulliNB(alpha = laplace, fit_prior=True, class_prior=None)
        h = hnb.fit(training_set, training_set_solutions)
#            training_predicted = h.predict(training_set)
        validation_set_predicted = h.predict(validation_set)
        f1 = f1 + metrics.f1_score(validation_set_solutions, validation_set_predicted)
        accuracy = accuracy + metrics.accuracy_score(validation_set_solutions, validation_set_predicted)
    f1 = f1/k
    accuracy = accuracy/k
    print("-------------------------------------")
    print("Bernoulli Naive Bayes")
    print("Laplace value: %s" % str(laplace))
    print("ACCURACY: %s" % str(accuracy))
    print("F1 SCORE: %s" % str(f1))
    print("-------------------------------------")
    nb = BernoulliNB(alpha = laplace, fit_prior=True, class_prior=None)
    return nb.fit(examples, examples_solution)
        