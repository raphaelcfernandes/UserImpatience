import serial
import serial.tools.list_ports
import time
import sys


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
            position += 1

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


class DataQProcess:
    def __init__(self):
        self.command = "WAIT"
        self.decimation_factor = 250
        self.dataQ = DataQ()

    def spawnProcess(self, queue):
        acquiring = True
        self.dataQ.send_cmd("start")
        acum = 0
        cont = 0
        app = ""
        gov = ""
        iteration = ""
        while True:
            # Check if main thread put something to Queue
            # It could be:
            # READ -> to save reading from dataQ
            # WAIT -> to stop saving reading data from dataQ
            # STOP -> to stop dataQ
            if not queue.empty():
                self.command = queue.get()
                if "RUN" in self.command:
                    parser = self.command.split(":")
                    # Retrieve RUN from request
                    self.command = parser[0]
                    # Retrieve app from request
                    app = parser[1]
                    # Retrieve gov from request
                    gov = parser[2]
                    # Retrieve iteration from request
                    iteration = parser[3]
                    f = open(f"results/{gov}/{app}/{iteration}.txt", "w+")
                    f.write('amperes,timestamp\n')
                if self.command == "STOP":
                    acquiring = False
                    break
                if self.command == "WAIT":
                    self.dataQ.ser.reset_input_buffer()
                    continue
            if (self.dataQ.ser.in_waiting >= self.dataQ.packet_size) and self.command == "RUN":
                for _ in range(self.dataQ.measurements_per_packet):
                    # Always two bytes per sample...read them
                    bytes = self.dataQ.ser.read(2)
                    result = int.from_bytes(
                        bytes, byteorder='little', signed=True)
                    result = result >> 2
                    result = result << 2
                    result = (10*(result/32768))/11
                    acum += result
                    cont += 1
                    if(cont == self.decimation_factor):
                        f.write("{},{}\n".format(
                            acum/self.decimation_factor, time.time()))
                        cont = 0
                        acum = 0
            elif self.command != "RUN":
                self.dataQ.ser.reset_input_buffer()
        self.dataQ.send_cmd("stop")
        self.dataQ.ser.reset_input_buffer()
        self.dataQ.ser.close()
        f.close()
