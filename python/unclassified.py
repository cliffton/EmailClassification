from os import listdir
from os.path import isfile, join
from subprocess import check_output
import re
from multiprocessing.dummy import Pool as ThreadPool


def separate_attrs2(email_file_path, count):
    email_file = open(email_file_path, "r")
    email_lines = [line for line in email_file.readlines()]
    msg = ""
    content_found = False
    for line in email_lines:

        # msg += line.strip() + " "
        

        if line == "\n":
            content_found = True
            continue

        if content_found:
            msg += line.strip() + " "

        tmp = ""
        for e in msg:
            if e == ",":
                tmp += " "

            elif e == " ":
                tmp += " "

            elif e.isalnum():
                tmp += e

            elif not e.isalnum():
                tmp += " "


    msg = str(count) + ",0, " + tmp.lower() + "\n" 
    email_file.close()
    return msg


def for_files_in_folder2(folder):
    all_files = []

    for f in listdir(folder):
        # if isfile(join(folder, f)) and (f[-3:] == "txt" or f[-3:] == "eml") and (f != "Summary.txt") :
        if isfile(join(folder, f)):
            all_files.append(join(folder, f))
        elif not isfile(join(folder, f)):
            extra_files = for_files_in_folder(join(folder, f))
            all_files.extend(extra_files)
    return all_files


def mt_csv2(folder):
    all_files = for_files_in_folder2(folder)
    count = 0
    output = open("/home/cliffton/workspace/EmailData/output/ham4.csv", "w+")
    for x in all_files:
        if x.find("ham/") > -1:
            print(x)
            try:
                # print(str(x))
                count+=1
                msg = separate_attrs2(x, count)
                output.write(msg)
            except Exception as e:
                print(x)
                print(str(e))
    print(count)
    # print(o)
    output.close()



def mt2():
    all_files = all_folders_in_folder("/home/cliffton/workspace/EmailData/data/assasin/spam/spam")
    # pool = ThreadPool(8)
    # results = pool.map(mt_csv2, all_files)
    for folder in all_files:
        mt_csv2(folder)


def mt3():
    mt_csv2("/home/cliffton/workspace/EmailData/data/assasin/ham")



# users()
# mt3()


def read_csv():
    email_file = open("/home/cliffton/workspace/EmailData/data/enron/emails.csv/emails.csv", "r")
    count = 0
    for line in email_file.readlines():
        print(line)
        print("############################################")
        count+=1
        if count == 100:
            break


# read_csv()



import numpy as np # linear algebra
import pandas as pd # data processing, CSV file I/O (e.g. pd.read_csv)
import re

# Input data files are available in the "../input/" directory.
# For example, running this (by clicking run or pressing Shift+Enter) will list the files in the input directory

from subprocess import check_output



def remove_dirty(email, count):
    email_lines = [ e for e in email.split("\n")]
    content_found = False
    msg = ""
    for line in email_lines:

        # msg += line.strip() + " "
        

        if line == "":
            content_found = True
            continue

        if content_found:
            msg += line.strip() + " "

        tmp = ""
        for e in msg:
            if e == ",":
                tmp += " "

            elif e == " ":
                tmp += " "

            elif e.isalnum():
                tmp += e

            elif not e.isalnum():
                tmp += " "


    msg = str(count) + ",2, " + tmp.lower() + "\n"
    return msg


def something():
    emails = pd.read_csv("/home/cliffton/workspace/EmailData/data/enron/emails.csv/emails.csv")
    output = open("/home/cliffton/workspace/EmailData/output/tardis/unclassified200.csv", "w+")
    count = 0
    all_emails = emails['message']
    all_emails.pop(0)
    for email in all_emails:
        line = remove_dirty(email, count)
        output.write(line)
        count+=1
        if count == 200:
            break

    output.close()

something()
