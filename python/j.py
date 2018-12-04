"""
Author: Jainey Elsa James
Date: 09/07/2018

This module takes a text file as input, cleans it by removing punctuation,
and then performs frequency count of the words that appear in the text.
This is done to analyze Zipf's Law in texts.
"""

import string
from collections import OrderedDict
from operator import itemgetter
import matplotlib.pyplot as plt
import math


WORD_COUNT = {}
STOP_WORD_LIST = []

def text_file_opening(textfile):
    """
    This function is used to open the text file, modify the contents
    to remove punctuation, and add the words into a list.
    :return: None
    """
    with open(textfile) as file:
        for line in file:
            words = [x.strip(string.punctuation) for x in line.split()]
            for word in words:
                word = word.lower()
                for char in '-.,\nï»¿ã¢©§/âª´¡123456789':
                    word = word.replace(char, '')
                if word not in WORD_COUNT.keys():
                    WORD_COUNT[word] = 1
                elif word in WORD_COUNT.keys():
                    WORD_COUNT[word] += 1


def data_processing():
    """
    This function is used to sort the words on the basis of the count in the dictionary.
    :return: None
    """
    sorted_dictionary = OrderedDict(sorted(WORD_COUNT.items(), key=lambda x: x[1]), reverse='True')
    print (sorted_dictionary)
    return sorted_dictionary


def plotting(sorted_dictionary):
    """
    A function to plot the sorted dictionary using a histogram to
    show the frequency of the words.
    :param sorted_dictionary: A sorted dictionary with the count of the words
    :return: None
    """
    klist = [ x for x in sorted_dictionary.keys()]
    vlist = [ x for x in sorted_dictionary.values()]
    vlist.remove("True")
    vlist = [ math.log(x,10) for x in vlist]
    rank = range(1,len(vlist)+1)
    rank = [math.log(x,10) for x in range(1,len(vlist)+1)]
    vlist.reverse()
    plt.plot(rank,vlist)
    plt.show()


def stop_words_file_creation():
    """
    This function is used to create the list of stop words from the text file.
    :return: None
    """
    with open(r"Stop_words.txt", 'r') as file:
        for word in file:
            for char in '\n':
                word = word.replace(char, '')
            STOP_WORD_LIST.append(word)


def removing_stop_words_from_dict(word_dictionary):
    """
    This function removes the stop words from the dictionary keys if it is present among the keys.
    :param word_dictionary: The dictionary before all the stop words have been removed
    :return: The dictionary with all the stop words removed
    """
    new_dic = {}
    for word,value in word_dictionary.items():
        if word not in STOP_WORD_LIST:
            new_dic.update({word: value})
    print(word_dictionary)
    return new_dic


def main():
    """
    The main function.
    :return:
    """
    filelist = ['17170-0.txt', 'pg17606.txt', 'Vegetius.txt']
    for file in filelist:
        text_file_opening(file)
        dictionary = data_processing()
        plotting(dictionary)
        stop_words_file_creation()
        d = removing_stop_words_from_dict(dictionary)
        plotting(d)


if __name__ == '__main__':
    main()