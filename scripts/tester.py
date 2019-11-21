from dataq import DataQProcess
from multiprocessing import Process, Queue
from uimpatienceThread import MyThread
import time
import os


class Tester:
    governor = ["interactive"]
    apps = ["spotify", "gmail", "chrome"]
    dataQ = DataQProcess()
    queue = Queue()

    def __init__(self):
        self.setGovernor = True
        self.runApp = True
        self.filename = ""
        self.p = Process(target=self.dataQ.spawnProcess, args=(self.queue,))
        self.p.start()

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
                        print("Python running {} with {}".format(app, gov))
                        for i in range(0, 1):
                            print("running {} {} iteration: {}".format(gov, app, i))
                            # Save file to its respective governor and app
                            self.filename = "results/{}/{}/{}.txt".format(
                                gov, app, i)
                            self.executeTest(app, gov, i)
                    self.setGovernor = True
                    self.runApp = False
        except Exception as e:
            print(e)

    def executeTest(self, app, gov, iteration):
        k = ["READ", "WAIT", "STOP"]
        for i in k:
            self.queue.put(i)
            time.sleep(5)
        self.p.join()
        self.queue.close()
        # f = open(self.filename, "w+")
        # f.write('amperes,timestamp\n')
        # if os.system("./cpu search {}".format(app)) == 0:
        #     mythread = MyThread("run", app, gov, iteration)
        #     mythread.setName("C++ execution")
        #     mythread.start()
