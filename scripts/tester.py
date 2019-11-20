from dataq import DataQ
from uimpatienceThread import MyThread
import time
import os


class Tester:
    governor = ["interactive"]
    apps = ["spotify", "gmail", "chrome"]
    dataq = DataQ()

    def __init__(self):
        self.setGovernor = True
        self.runApp = True
        self.filename = ""

    def testBoard(self):
        acum = 0
        cont = 0
        decimation_factor = 100
        for _ in range(0, 300):
            start = time.time()
            if not self.dataq.ser.is_open:
                # self.dataq.firstConfig()
                self.dataq.ser.open()
            self.dataq.acquiring = True
            print('---------------------------- {}'.format(_))
            self.dataq.send_cmd("start")
            while True:
                # if self.dataq.ser.in_waiting == 0:
                    # self.dataq.ser.
                if(self.dataq.ser.in_waiting >= self.dataq.packet_size):
                    for _ in range(self.dataq.measurements_per_packet):
                        # Always two bytes per sample...read them
                        bytes = self.dataq.ser.read(2)
                        result = int.from_bytes(
                            bytes, byteorder='little', signed=True)
                        result = result >> 2
                        result = result << 2
                        result = (10*(result/32768))/11
                        acum += result
                        cont += 1
                        if(cont == decimation_factor):
                            print(acum/decimation_factor)
                            cont = 0
                            acum = 0
                if time.time() - start > 2:
                    break
            self.dataq.send_cmd("stop")
            time.sleep(1)
            self.dataq.ser.reset_input_buffer()
            self.dataq.acquiring = False

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
        finally:
            self.dataq.ser.close()

    def executeTest(self, app, gov, iteration):
        f = open(self.filename, "w+")
        f.write('amperes,timestamp\n')
        acum = 0
        cont = 0
        decimation_factor = 500
        if os.system("./cpu search {}".format(app)) == 0:
            mythread = MyThread("run", app, gov, iteration)
            mythread.setName("C++ execution")
            mythread.start()
            self.dataq.acquiring = True
            self.dataq.send_cmd("start 0")
            while True:
                if(self.dataq.ser.inWaiting() >= self.dataq.packet_size):
                    for _ in range(self.dataq.measurements_per_packet):
                        # Always two bytes per sample...read them
                        bytes = self.dataq.ser.read(2)
                        result = int.from_bytes(
                            bytes, byteorder='little', signed=True)
                        result = result >> 2
                        result = result << 2
                        result = (10*(result/32768))/11
                        acum += result
                        cont += 1
                        # datetime.now().strftime("%H:%M:%S.%f"))
                        if(cont == decimation_factor):
                            f.write("{},{}\n".format(
                                acum/decimation_factor, time.time()))
                            cont = 0
                            acum = 0
                if not mythread.is_alive():
                    f.close()
                    if not f.close:
                        Exception("PAU")
                    break
            self.dataq.send_cmd("stop")
            time.sleep(1)
            self.dataq.ser.reset_input_buffer()
            self.dataq.acquiring = False
