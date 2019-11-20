import pdb
import sys
import serial
import serial.tools.list_ports
import keyboard
import time
from datetime import datetime
from datetime import timedelta
import threading
import os
import vxi11


class DataQ:
    """
        Example slist for model DI-1100
        0x0000 = Analog channel 0, ±10 V range
        0x0001 = Analog channel 1, ±10 V range
        0x0002 = Analog channel 2, ±10 V range
        0x0003 = Analog channel 3, ±10 V range
        """
    slist = [0x0000]
    ser = serial.Serial()
    # Contains accumulated values for each analog channel used for the average calculation
    achan_accumulation_table = list(())
    # Define flag to indicate if acquiring is active
    acquiring = False
    # Configs:
    ps = 0
    dividend = 60000000  # DO NOT CHANGE
    srate = 6000
    packet_size = 16
    configured = False
    measurement_rate = 0
    measurements_per_packet = 0

    def __init__(self):
        self.acquiring = False
        self.firstConfig()
        self.measurement_rate = self.dividend/self.srate

    def firstConfig(self):
        # Keep the packet size small for responsiveness
        # ps 0 Make packet size 16 bytes (DEFAULT)
        # ps 1 Make packet size 32 bytes
        # ps 2 Make packet size 64 bytes
        # ps 3 Make packet size 128 bytes
        # ps 4 Make packet size 256 bytes
        # ps 5 Make packet size 512 bytes
        # ps 6 Make packet size 1024 bytes
        # ps 7 Make packet size 2048 bytes
        if(self.ps == 0):
            self.packet_size = 16
        elif(self.ps == 1):
            self.packet_size = 32
        elif(self.ps == 2):
            self.packet_size = 64
        elif(self.ps == 3):
            self.packet_size = 128
        elif(self.ps == 4):
            self.packet_size = 256
        elif(self.ps == 5):
            self.packet_size = 512
        elif(self.ps == 6):
            self.packet_size = 1024
        elif(self.ps == 7):
            self.packet_size = 2048
        else:
            sys.exit(-1)
        while self.discovery() == False:
            self.discovery()
        self.measurements_per_packet = int(self.packet_size/2)
        # Stop in case DI-1100 is already scanning
        self.send_cmd("stop")
        # Define binary output mode
        self.send_cmd("encode 0")
        # Set ps 0 means wait 16bytes to be ready from dataQ to read it
        # DataQ will send bursts of 16bytes, or 8 packages of 2 bytes
        self.send_cmd("ps {}".format(self.ps))
        self.config_scn_lst()
        self.send_cmd(f"srate {self.srate}")

    def send_cmd(self, command):
        self.ser.write((command+'\r').encode())
        time.sleep(.1)
        if not(self.acquiring):
            # Echo commands if not acquiring
            while True:
                if(self.ser.in_waiting > 0):
                    while True:
                        try:
                            s = self.ser.readline().decode()
                            s = s.strip('\n')
                            s = s.strip('\r')
                            s = s.strip(chr(0))
                            break
                        except:
                            continue
                    if s != "":
                        print(s)
                        break

    def config_scn_lst(self):
        # Scan list position must start with 0 and increment sequentially
        position = 0
        for item in self.slist:
            self.send_cmd("slist " + str(position) + " " + str(item))
            # Add the channel to the logical list.
            self.achan_accumulation_table.append(0)
            position += 1

    def reset(self):
        self.ser.reset_input_buffer()
        self.ser.reset_output_buffer()
        self.send_cmd("encode 0")
        # Set ps 0 means wait 16bytes to be ready from dataQ to read it
        # DataQ will send bursts of 16bytes, or 8 packages of 2 bytes
        self.send_cmd("ps {}".format(self.ps))
        self.config_scn_lst()
        self.send_cmd(f"srate {self.srate}")

    def discovery(self):
        # Get a list of active com ports to scan for possible DATAQ Instruments devices
        available_ports = list(serial.tools.list_ports.comports())
        # Will eventually hold the com port of the detected device, if any
        hooked_port = ""
        for p in available_ports:
            # Do we have a DATAQ Instruments device?
            if ("VID:PID=0683" in p.hwid):
                # Yes!  Dectect and assign the hooked com port
                hooked_port = p.device
                break

        if hooked_port:
            print("Found a DATAQ Instruments device on", hooked_port)
            self.ser.timeout = 0
            self.ser.port = hooked_port
            self.ser.baudrate = '115200'
            self.ser.open()
            return(True)
        else:
            # Get here if no DATAQ Instruments devices are detected
            print("Please connect a DATAQ Instruments device")
            input("Press ENTER to try again...")
            return(False)


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
                        # datetime.now().strftime("%H:%M:%S.%f"))
                        if(cont == decimation_factor):
                            print(acum/decimation_factor)
                            # f.write("{},{}\n".format(
                            #     acum/decimation_factor, time.time()))
                            cont = 0
                            acum = 0
                if time.time() - start > 2:
                    break
            self.dataq.send_cmd("stop")
            time.sleep(1)
            self.dataq.ser.reset_input_buffer()
            self.dataq.ser.reset_output_buffer()
            self.dataq.ser.flush()
            self.dataq.ser.close()
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
            self.dataq.ser.reset_output_buffer()
            self.dataq.acquiring = False


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


if __name__ == '__main__':
    tester = Tester()
    tester.testBoard()
    sys.exit(0)
