from dataq import DataQProcess
from multiprocessing import Process, Queue
from uimpatienceThread import MyThread
import time
import os
import subprocess
from subprocess import check_output, CalledProcessError


class Tester:
    governor = ['powersave']
    apps = ['photos']
    # Read interval that userspace background thread will read TA from top in s
    # Values in s
    timeToReadTA = ["1", "2"]
    device = "Nexus 5"
    # Time to decrease cpu frequency in s
    # this is the parameter A
    # Values in s
    decreaseCPUInterval = ["1", "2", "4", "8"]
    # Amount of frequency to reduce after A.time has passed
    decreaseCPUFrequency = ["2", "4", "8"]
    #(Max - current)/C
    # Where C is the following measurements
    marginToIncreaseCpuFrequency = ["2", "4", "8"]
    # 0 is the user that does not complain at all
    # 5 is the user that complains with high frequency
    userImpatienceLevel = [0, 1, 2]
    dataQ = DataQProcess()
    queue = Queue()

    def __init__(self):
        self.setGovernor = True
        self.runApp = True
        self.p = Process(target=self.dataQ.spawnProcess, args=(self.queue,))
        self.p.start()

    def testUImpatience(self):
        if not os.path.exists("results/uimpatience"):
            os.mkdir("results/uimpatience")
        if not os.path.exists("adbTouchEvents"):
            os.mkdir("adbTouchEvents")
        if not os.path.exists("adbTouchEvents/uimpatience"):
            os.mkdir("adbTouchEvents/uimpatience")
        if not os.path.exists("results/uimpatience/readTa"):
            os.mkdir("results/uimpatience/readTa")
        for readTa in self.timeToReadTA:
            for decreaseCPUi in self.decreaseCPUInterval:
                for decreaseCPUf in self.decreaseCPUFrequency:
                    for increaseCPUf in self.marginToIncreaseCpuFrequency:
                        for impatienceLevel in self.userImpatienceLevel:
                            path = "results/uimpatience/{}_{}_{}_{}_{}".format(
                                readTa, decreaseCPUi, decreaseCPUf, increaseCPUf.replace("/", "|"), impatienceLevel)
                            if not os.path.exists(path):
                                os.mkdir(path)
                            # for i in range(0, 30):
                            #     f = open(
                            #         f"results/uimpatience/{readTa}_{decreaseCPUi}_{decreaseCPUf}_{increaseCPUf.replace('/','|')}_{impatienceLevel}/{i}.txt", "w+")
                            #     f.write("amperes,timestamp\n")
                            #     f.close()

    def test(self):
        try:
            # Create a folder for each governor
            if not os.path.exists("adbTouchEvents"):
                os.mkdir("adbTouchEvents")
            if not os.path.exists("adbTouchEvents/{}".format(self.device)):
                os.mkdir("adbTouchEvents/{}".format(self.device))
            if not os.path.exists("results"):
                os.mkdir("results")
            if not os.path.exists("results/{}".format(self.device)):
                os.mkdir("results/{}".format(self.device))
            for gov in self.governor:
                if not os.path.exists("results/{}/{}".format(self.device, gov)):
                    os.mkdir("results/{}/{}".format(self.device, gov))
                if not os.path.exists("adbTouchEvents/{}/{}".format(self.device, gov)):
                    os.mkdir("adbTouchEvents/{}/{}".format(self.device, gov))
                # Tell the c++ code to set the governor before executing the array of apps
                # if self.setGovernor:
                    # Wait the c++ code to set the governor and then start executing apps
                # if os.system("./cpu set {}".format(gov)) == 0:
                self.runApp = True
                self.setGovernor = False
                if self.runApp:
                    for app in self.apps:
                        # Create folder for each app inside each governor
                        if not os.path.exists("results/{}/{}/{}".format(self.device, gov, app)):
                            os.mkdir(
                                "results/{}/{}/{}".format(self.device, gov, app))
                        if not os.path.exists("adbTouchEvents/{}/{}/{}".format(self.device, gov, app)):
                            os.mkdir(
                                "adbTouchEvents/{}/{}/{}".format(self.device, gov, app))
                        for i in range(12, 30):
                            res = str(check_output(
                                "adb shell dumpsys battery", shell=True).decode('utf-8'))
                            res = int(res.replace("\r\n", ",").strip().split(
                                ",")[8].split(":")[1].strip())
                            if res < 100:
                                errorF = open("error_file.txt", "a+")
                                errorF.write(
                                    f"BATERIA ABAIXO DE 100. {gov},{app},{i}\n")
                                errorF.close()
                            print("running {} {} iteration: {}".format(gov, app, i))
                            # Save file to its respective governor and app
                            self.executeTest(app, gov, i)
                    self.setGovernor = True
                    self.runApp = False
        except Exception as e:
            print(e)
        finally:
            self.queue.put("STOP")
            self.p.join()
            self.queue.close()

    def executeTest(self, app, gov, iteration):
        mythread = MyThread("run", app, gov, iteration)
        if os.system("./cpu search {}".format(app)) == 0:
            self.queue.put(f"RUN:{app}:{gov}:{iteration}:{self.device}")
            mythread.setName("C++ execution")
            mythread.start()
        while mythread.is_alive():
            continue
