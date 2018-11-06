from os import listdir
from os.path import isfile, join
from subprocess import check_output
import re
from multiprocessing.dummy import Pool as ThreadPool


done = ["sturm-f",
"ybarbo-p",
"gilbertsmith-d",
"wolfe-j",
"mccarty-d",
"neal-s",
"scholtes-d",
"may-l",
"perlingiere-d",
"williams-j",
"hendrickson-s",
"king-j",
"benson-r",
"tholt-j",
"mclaughlin-e",
"blair-l",
"donoho-l",
"staab-t",
"parks-j",
"swerzbin-m",
"hernandez-j",
"kuykendall-t",
"giron-d",
"hyvl-d",
"grigsby-m",
"arnold-j",
"white-s",
"beck-s",
"schwieger-j",
"bass-e",
"farmer-d",
"gay-r",
"lokay-m",
"ruscitti-k",
"lavorato-j",
"geaccone-t"]

def for_files_in_folder(folder):
    all_files = []
    for f in listdir(folder):
        # if isfile(join(folder, f)) and (f[-3:] == "txt" or f[-3:] == "eml"):
        if isfile(join(folder, f)):
            all_files.append(join(folder, f))
        elif not isfile(join(folder, f)):
            extra_files = for_files_in_folder(join(folder, f))
            all_files.extend(extra_files)
    return all_files


def all_folders_in_folder(folder):
    all_files = []
    for f in listdir(folder):
        # if isfile(join(folder, f)) and (f[-3:] == "txt" or f[-3:] == "eml"):
        if not isfile(join(folder, f)):
            all_files.append(join(folder, f))
    return all_files


# def separate_attrs(email_file_path, count):
#     email_file = open(email_file_path, "r")
#     email_lines = [line for line in email_file.readlines()]
#     msg = ""
#     for line in email_lines:
#         msg += line.strip()
#         # msg = re.sub('\W+','', msg)
#         # allowed = []
#         # for c in msg:
#         #     if c.isalnum() or c == " ":
#         #         allowed.append(c)
#         #     else:
#         #         allowed.append(" ")
#         # msg = ''.join(e for e in msg if e.isalnum() or e == " ")
#         # msg = ''.join(allowed)
#     msg = str(count) + "," + msg + "\n" 
#     email_file.close()
#     return msg

def separate_attrs(email_file_path, count):
    email_file = open(email_file_path, "r")
    email_lines = [line for line in email_file.readlines()]
    msg = ""
    content_found = False
    for line in email_lines:

        if line == "\n":
            content_found = True
            continue

        if content_found:
            msg += line.strip() + " "

        # msg = re.sub('\W+','', msg)
        # msg = ''.join(e for e in msg if e.isalnum() or e == " ")
        msg = ''.join(e for e in msg if e != ",")
    msg = str(count) + "," + msg + "\n" 
    email_file.close()
    return msg




def csv():
    # all_files = for_files_in_folder("/home/cliffton/workspace/EmailClassification/data/enron_mail/maildir/allen-p")
    all_files = for_files_in_folder("/home/cliffton/workspace/EmailClassification/data/enron_mail")
    # all_files = for_files_in_folder("/home/cliffton/workspace/EmailClassification/data/enron")
    # print(all_files)
    count = 0
    output = open("/home/cliffton/workspace/EmailClassification/data/output.csv", "w")
    for x in all_files:
        try:
            count+=1
            msg = separate_attrs(x, count)
            output.write(msg)
        except Exception as e:
            pass
    print(count)
    output.close()


def mt_csv(folder):
    all_files = for_files_in_folder(folder)
    count = 0
    o = folder.split("/home/cliffton/workspace/EmailClassification/data/enron_mail/maildir/")[1]
    output = open("/home/cliffton/workspace/EmailClassification/data/output/"+ o + ".csv", "w+")
    for x in all_files:
        try:
            count+=1
            msg = separate_attrs(x, count)
            output.write(msg)
        except Exception as e:
            print(str(e))
    # print(count)
    print(o)
    output.close()



def users():
    all_files = all_folders_in_folder("/home/cliffton/workspace/EmailClassification/data/enron_mail/maildir")
    print(all_files)
    print(len(all_files))



def mt():
    all_files = all_folders_in_folder("/home/cliffton/workspace/EmailClassification/data/enron_mail/maildir")
    pool = ThreadPool(8) 
    bleh = set(all_files) - set(done)
    results = pool.map(mt_csv, bleh)


def separate_attrs2(email_file_path, count):
    email_file = open(email_file_path, "r")
    email_lines = [line for line in email_file.readlines()]
    msg = ""
    tmp = ""
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


    msg = str(count) + ",1, " + tmp.lower() + "\n" 
    email_file.close()
    return msg


def for_files_in_folder2(folder):
    all_files = []

    for f in listdir(folder):
        if isfile(join(folder, f)) and (f[-3:] == "txt" or f[-3:] == "eml") and (f != "Summary.txt") :
        # if isfile(join(folder, f)):
            all_files.append(join(folder, f))
        elif not isfile(join(folder, f)):
            extra_files = for_files_in_folder(join(folder, f))
            all_files.extend(extra_files)
    return all_files


def mt_csv2(folder):
    all_files = for_files_in_folder2(folder)
    count = 0
    output = open("/home/cliffton/workspace/EmailData/output/morespam.csv", "w+")
    for x in all_files:
        if x.find("spam/") > -1:
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
    all_files = all_folders_in_folder("/home/cliffton/workspace/EmailData/data/enron/spam")
    # pool = ThreadPool(8)
    # results = pool.map(mt_csv2, all_files)
    for folder in all_files:
        mt_csv2(folder)


# def mt3():
#     mt_csv2("/home/cliffton/workspace/EmailData/data/assasin/ham")



# users()
mt2()









