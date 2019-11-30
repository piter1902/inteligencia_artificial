# Example of loading emails from Enron
# Juan D. Tardos
# 28-Nov-2018

######################################################
# Imports
######################################################

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

######################################################
# Functions for loading mails
######################################################

def read_folder(folder):
    mails = []
    file_list = glob.glob(folder)  # List mails in folder
    num_files = len(file_list)
    for i in range(0, num_files):
        i_path = file_list[i]
        # print(i_path)
        i_file = open(i_path, 'rb')
        i_str = i_file.read()
        i_text = i_str.decode('utf-8', errors='ignore')  # Convert to Unicode
        mails.append(i_text)  # Append to the mail structure
        i_file.close()
    return mails


def load_enron_folders(datasets):
    path = 'D:\Mingo\DOCENCIA\IA\TP6\datasets\\'
    ham = []
    spam = []
    for j in datasets:
        ham  = ham  + read_folder(path + 'enron' + str(j) + '\ham\*.txt')
        spam = spam + read_folder(path + 'enron' + str(j) + '\spam\*.txt')
    num_ham  = len(ham)
    num_spam = len(spam)
    print("mails:", num_ham+num_spam)
    print("ham  :", num_ham)
    print("spam :", num_spam)

    mails = ham + spam
    labels = [0]*num_ham + [1]*num_spam
    mails, labels = shuffle(mails, labels, random_state=0)
    return mails, labels


######################################################
# Main
######################################################

print("Loading files...")

print("------Loading train and validation data--------")
mails, y = load_enron_folders([1,2,3,4,5])

print("--------------Loading Test data----------------")
mails_test, y_test = load_enron_folders([6])

print("-----Example of obtaining BOWs from emails-----")
vectorizer  = CountVectorizer(ngram_range=(1, 1))  # Initialize BOW structure
X = vectorizer.fit_transform(mails)                # BOW with word counts
X_test = vectorizer.transform(mails_test)          # BOW with word counts
print("A Bag of Words is represented as a sparse matrix:" )
print(X)
