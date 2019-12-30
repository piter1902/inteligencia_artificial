# -*- coding: utf-8 -*-
"""
Created on Mon Dec 30 09:25:06 2019

@author: pedro
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



def partition(array, fold, partitions):
    # fold = nº de pliegue sobre el que dividir. Tiene que estar entre 1,partitions.
    # partitions = nº total de particiones
    # array = vector sobre el que realizar la particion
    long_total = len(array)
    limite_inferior = int(fold*(long_total/float(partitions))) - 1
    limite_superior = int((fold+1)*(long_total/float(partitions))) -1
    especial = [array[i] for i in range(0, long_total) if i >= limite_inferior and i < limite_superior]
#    normal   = [el for el in array if el not in especial]
    normal   = [array[i] for i in range(0, long_total) if not(i >= limite_inferior and i < limite_superior) ]
    return normal, especial

def kfoldMultinomial(k, examples, examples_solution):
    best_size = 0
    best_errV = float('inf')
    # Variamos el valor del hyper-parametro del suavizado de Laplace
    size = float(0)
    while size < float(5):
        err_T, err_V = 0, 0
        for fold in range(1, k+1):
            [training_set, validation_set] = partition(examples, fold, k)
            [training_set_solutions, validation_set_solutions] = partition(examples_solution, fold, k)
            # Alpha = 0 -> Sin suavizado de Laplace
            print("HOLA 1")
            hnb = MultinomialNB(alpha = size, fit_prior=True, class_prior=None)
            print("HOLA 2")
            h = hnb.fit(training_set, training_set_solutions)
            training_predicted = h.predict(training_set)
            validation_set_predicted = h.predict(validation_set)
            err_T = err_T + metrics.accuracy_score(training_predicted, training_set_solutions)
            err_V = err_V + metrics.accuracy_score(validation_set_predicted, validation_set_solutions)
        err_T = err_T/k
        err_V = err_V/k
        if err_V < best_errV:
            best_size = size
        size = size + 0.1
    nb = MultinomialNB(alpha = best_size, fit_prior=True, class_prior=None)
    return nb.fit(examples, examples_solution)
            
        