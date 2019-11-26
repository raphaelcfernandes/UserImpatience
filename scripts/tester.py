from dataq import DataQProcess
from multiprocessing import Process, Queue
from uimpatienceThread import MyThread
import time
import os


class Tester:
    governor = ['powersave']
    apps = ["chrome", "spotify", "youtube", "gmail"]
    # Read interval that userspace background thread will read TA from top in ms
    # Values in ms
    timeToReadTA = ["500", "1000", "2000"]
    # Time to decrease cpu frequency in ms
    # this is the parameter A
    # Values in ms
    decreaseCPUInterval = ["1000", "2000", "4000", "8000"]
    # Amount of frequency to reduce after A.time has passed
    decreaseCPUFrequency = ["2", "4", "8"]
    #(Max - current)/C
    # Where C is the following measurements
    marginToIncreaseCpuFrequency = ["1/2", "1/4", "1/8"]
    # 0 is the user that does not complain at all
    # 5 is the user that complains with high frequency
    userImpatienceLevel = [0, 1, 2]
    # dataQ = DataQProcess()
    # queue = Queue()

    def __init__(self):
        self.setGovernor = True
        self.runApp = True
        self.filename = ""
        # self.p = Process(target=self.dataQ.spawnProcess, args=(self.queue,))
        # self.p.start()

    def testUImpatience(self):
        cont = 0
        for readTa in self.timeToReadTA:
            for decreaseCPUi in self.decreaseCPUInterval:
                for decreaseCPUf in self.decreaseCPUFrequency:
                    for incraseCPUf in self.marginToIncreaseCpuFrequency:
                        for impatienceLevel in self.userImpatienceLevel:
                            if not os.path.exists("results/uimpatience"):
                                os.mkdir("results/uimpatience")

    def test(self):
        try:
            # Create a folder for each governor
            for gov in self.governor:
                if not os.path.exists("results/{}".format(gov)):
                    os.mkdir("results/{}".format(gov))
                if not os.path.exists("adbTouchEvents"):
                    os.mkdir("adbTouchEvents")
                if not os.path.exists("adbTouchEvents/{}".format(gov)):
                    os.mkdir("adbTouchEvents/{}".format(gov))
                # Tell the c++ code to set the governor before executing the array of apps
                if self.setGovernor:
                    # Wait the c++ code to set the governor and then start executing apps
                    if os.system("./cpu set {}".format(gov)) == 0:
                        self.runApp = True
                        self.setGovernor = False
                if self.runApp:
                    for app in self.apps:
                        # Create folder for each app inside each governor
                        if not os.path.exists("results/{}/{}".format(gov, app)):
                            os.mkdir("results/{}/{}".format(gov, app))
                        if not os.path.exists("adbTouchEvents/{}/{}".format(gov, app)):
                            os.mkdir("adbTouchEvents/{}/{}".format(gov, app))
                        for i in range(17, 30):
                            print("running {} {} iteration: {}".format(gov, app, i))
                            # Save file to its respective governor and app
                            self.filename = "results/{}/{}/{}.txt".format(
                                gov, app, i)
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
            self.queue.put(f"RUN:{app}:{gov}:{iteration}")
            mythread.setName("C++ execution")
            mythread.start()
        while mythread.is_alive():
            continue
