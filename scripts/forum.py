import serial
import serial.tools.list_ports
import time


def send_cmd(command):
    ser.write((command+'\r').encode())
    time.sleep(.1)
    if not(acquiring):
        # Echo commands if not acquiring
        while True:
            if(ser.in_waiting > 0):
                while True:
                    try:
                        s = ser.readline().decode()
                        s = s.strip('\n')
                        s = s.strip('\r')
                        s = s.strip(chr(0))
                        break
                    except:
                        continue
                if s != "":
                    print(s)
                    break


def config_scn_lst():
    # Scan list position must start with 0 and increment sequentially
    position = 0
    for item in slist:
        send_cmd("slist " + str(position) + " " + str(item))
        # Add the channel to the logical list.
        position += 1


def discovery():
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
        ser.timeout = 0
        ser.port = hooked_port
        ser.baudrate = '115200'
        ser.open()
        return(True)
    else:
        # Get here if no DATAQ Instruments devices are detected
        print("Please connect a DATAQ Instruments device")
        input("Press ENTER to try again...")
        return(False)


slist = [0x0000]
ser = serial.Serial()
# Define flag to indicate if acquiring is active
acquiring = False
# Configs:
ps = 0
dividend = 60000000
srate = 6000
packet_size = 16
configured = False
measurements_per_packet = int(packet_size/2)
discovery()
ser.reset_input_buffer()
# Stop in case DI-1100 is already scanning
send_cmd("stop")
# Define binary output mode
send_cmd("encode 0")
# Set ps 0 means wait 16bytes to be ready from dataQ to read it
# DataQ will send bursts of 16bytes, or 8 packages of 2 bytes
send_cmd("ps {}".format(ps))
config_scn_lst()
send_cmd(f"srate {srate}")

acum = 0
cont = 0
decimation_factor = 100
for i in range(0, 300):
    start = time.time()
    acquiring = True
    print('---------------------------- {}'.format(i))
    send_cmd("start")
    while True:
        if(ser.in_waiting >= packet_size):
            for _ in range(measurements_per_packet):
                # Always two bytes per sample...read them
                bytes = ser.read(2)
                result = int.from_bytes(
                    bytes, byteorder='little', signed=True)
                result = result >> 2
                result = result << 2
                result = (10*(result/32768))
                acum += result
                cont += 1
                if(cont == decimation_factor):
                    print(acum/decimation_factor)
                    cont = 0
                    acum = 0
        if time.time() - start > 2:
            break
    acquiring = False
    send_cmd("stop")
    time.sleep(1)
    ser.reset_input_buffer()
