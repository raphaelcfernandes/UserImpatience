from dataq import DataQProcess
from multiprocessing import Process, Queue
from uimpatienceThread import CommonThread, UImpatienceThread
import time
import os
import subprocess
from subprocess import check_output, CalledProcessError


class Tester:
    # governor = ['performance','ondemand','conservative']
    governor = ['userspace']
    apps = ['chrome']
    app = 'chrome'
    # Read interval that userspace background thread will read TA from top in s
    # Values in s
    # timeToReadTA = ["1", "2"]
    timeToReadTA = ["1"]
    device = "Nexus 6"
    # Time to decrease cpu frequency in s
    # this is the parameter A
    # Values in s
    # decreaseCPUInterval = ["2", "4", "8"]
    decreaseCPUInterval = ["2"]
    # Amount of frequency to reduce after A.time has passed
    # decreaseCPUFrequency = ["2", "4", "8"]
    decreaseCPUFrequency = ["1"]
    #(Max - current)/C
    # Where C is the following measurements
    # marginToIncreaseCpuFrequency = ["2", "4", "8"]
    marginToIncreaseCpuFrequency = ["2"]
    # 0 is the user that does not complain at all
    # 5 is the user that complains with high frequency
    # userImpatienceLevel = [0, 1, 2]
    userImpatienceLevel = [1]
    dataQ = DataQProcess()
    queue = Queue()

    def __init__(self):
        self.setGovernor = True
        self.runApp = False
        self.p = Process(target=self.dataQ.spawnProcess, args=(self.queue,))
        self.p.start()

    def testUImpatience(self):
        try:
            gov = "userspace"
            # Create a folder for each governor
            if not os.path.exists("adbTouchEvents"):
                os.mkdir("adbTouchEvents")
            if not os.path.exists("adbTouchEvents/{}".format(self.device)):
                os.mkdir("adbTouchEvents/{}".format(self.device))
                os.mkdir("adbTouchEvents/{}/{}".format(self.device, gov))
            if not os.path.exists("results"):
                os.mkdir("results")
            if not os.path.exists("results/{}".format(self.device)):
                os.mkdir("results/{}".format(self.device))
                os.mkdir("results/{}/{}".format(self.device, gov))
            for readTa in self.timeToReadTA:
                for decreaseCPUi in self.decreaseCPUInterval:
                    for decreaseCPUf in self.decreaseCPUFrequency:
                        for increaseCPUf in self.marginToIncreaseCpuFrequency:
                            for impatienceLevel in self.userImpatienceLevel:
                                print(f"./cpu set userspace {readTa} {decreaseCPUi} {decreaseCPUf} {increaseCPUf} {impatienceLevel}")
                                if os.system(f"./cpu set userspace {readTa} {decreaseCPUi} {decreaseCPUf} {increaseCPUf} {impatienceLevel}") == 0:
                                    for app in self.apps:
                                        # Create folder for each app inside each governor
                                        if not os.path.exists("results/{}/{}/{}".format(self.device, gov, app)):
                                            os.mkdir(
                                                "results/{}/{}/{}".format(self.device, gov, app))
                                        if not os.path.exists("adbTouchEvents/{}/{}/{}".format(self.device, gov, app)):
                                            os.mkdir(
                                                "adbTouchEvents/{}/{}/{}".format(self.device, gov, app))
                                        path = "results/{}/{}/{}/readTA{}_decreaseCpuI{}_decreaseCpuF{}_increaseCpuF{}_impatienceLevel{}".format(self.device,
                                                                                                                                                 gov, app,
                                                                                                                                                 readTa, decreaseCPUi, decreaseCPUf, increaseCPUf, impatienceLevel)
                                        if not os.path.exists(path):
                                            os.mkdir(path)
                                            os.mkdir(path.replace(
                                                "results", "adbTouchEvents"))
                                        for i in range(0, 30):
                                            print(
                                                f"./cpu set userspace {readTa} {decreaseCPUi} {decreaseCPUf} {increaseCPUf} {impatienceLevel} {i}")
                                            self.executeTestUImpatience(self.app, "userspace", i, readTa,
                                                                        decreaseCPUi, decreaseCPUf, increaseCPUf, impatienceLevel)

        except Exception as e:
            print(e)
        finally:
            self.queue.put("STOP")
            self.p.join()
            self.queue.close()

    def commonGovernorsTest(self):
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
                if self.setGovernor:
                    # Wait the c++ code to set the governor and then start executing apps
                    if os.system("./cpu set {}".format(gov)) == 0:
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
                        for i in range(0, 30):
                            print("running {} {} iteration: {}".format(gov, app, i))
                            # Save file to its respective governor and app
                            self.executeCommonGovernorsTest(app, gov, i)
                    self.setGovernor = True
                    self.runApp = False
        except Exception as e:
            print(e)
        finally:
            self.queue.put("STOP")
            self.p.join()
            self.queue.close()

    def executeCommonGovernorsTest(self, app, gov, iteration):
        mythread = CommonThread(app, gov, iteration)
        if os.system("./cpu search {} {}".format(app, gov)) == 0:
            self.queue.put(f"RUN:{app}:{gov}:{iteration}:{self.device}")
            mythread.setName("C++ execution")
            mythread.start()
        while mythread.is_alive():
            continue

    def executeTestUImpatience(self, app, gov, iteration, readTa, decreaseCpuI, decreaseCpuF, increaseCpuF, impatienceLevel):
        mythread = UImpatienceThread(
            app, gov, iteration, readTa, decreaseCpuI, decreaseCpuF, increaseCpuF, impatienceLevel)
        if os.system("./cpu search {} {}".format(app, gov)) == 0:
            self.queue.put(
                f"RUN:{app}:{gov}:{iteration}:{self.device}:{readTa}:{decreaseCpuI}:{decreaseCpuF}:{increaseCpuF}:{impatienceLevel}")
            mythread.setName("C++ execution")
            mythread.start()
        while mythread.is_alive():
            continue
