from os import listdir
from os.path import isfile, join
from subprocess import check_output
import re

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

all_files = for_files_in_folder("/home/cliffton/workspace/EmailClassification/data/enron_mail/maildir/allen-p")
# all_files = for_files_in_folder("/home/cliffton/workspace/EmailClassification/data/enron_mail")
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
