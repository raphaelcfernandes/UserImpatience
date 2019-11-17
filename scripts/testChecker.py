import pdb
import os
import sys

for governorFolder in os.listdir(os.path.join(os.getcwd(), 'results')):
    path = os.path.join(os.path.join(os.getcwd(), 'results'), governorFolder)
    for app in os.listdir(path):
        for execution in os.listdir(os.path.join(path, app)):
            flag = False
            folder = os.path.join(path, app)
            file = os.path.join(folder, execution)
            with open(file) as fp:
                line = fp.readline()
                cnt = 1
                while line:
                    if cnt > 1:
                        amperes = line.split(",")[0]
                        if float(amperes) < 0:
                            print('Negative value in {}'.format(file))
                            break
                        if float(amperes) > 1:
                            #To print once
                            if not flag:
                                print('Value greater than 1V in {}'.format(file))
                                flag = True
                    line = fp.readline()
                    cnt += 1
            if cnt == 2:
                print('Empty file {}'.format(file))
