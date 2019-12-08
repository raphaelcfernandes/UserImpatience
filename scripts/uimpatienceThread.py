import threading
import os

class CommonThread(threading.Thread):
    def __init__(self, app, gov, iteration):
        self.app = app
        self.governor = gov
        self.iteration = iteration
        threading.Thread.__init__(self)

    def run(self):
        # ./cpu run app governor iteration
        os.system('./cpu run {} {} {} '.format(self.app,
                                               self.governor, self.iteration))

class UImpatienceThread(threading.Thread):
    def __init__(self, app, gov, iteration, readTa=None, decreaseCpuI=None, decreaseCpuF=None, increaseCpuI=None, impatienceLevel=None):
        self.app = app
        self.governor = gov
        self.iteration = iteration
        self.readTa = readTa
        self.decreaseCpuI = decreaseCpuI
        self.decreaseCpuF = decreaseCpuF
        self.increaseCpuI = increaseCpuI
        self.impatienceLevel = impatienceLevel
        threading.Thread.__init__(self)

    def run(self):
        os.system(f"./cpu run {self.app} {self.governor} {self.iteration} {self.readTa} {self.decreaseCpuI} {self.decreaseCpuF} {self.increaseCpuI} {self.impatienceLevel}")
