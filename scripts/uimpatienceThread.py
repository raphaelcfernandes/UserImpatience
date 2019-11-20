import threading
import os

class MyThread(threading.Thread):
    def __init__(self, cmd, app, gov, iteration):
        self.cmd = cmd
        self.app = app
        self.governor = gov
        self.iteration = iteration
        threading.Thread.__init__(self)

    def run(self):
        # ./cpu run app governor iteration
        os.system('./cpu {} {} {} {} '.format(self.cmd,
                                              self.app, self.governor, self.iteration))
