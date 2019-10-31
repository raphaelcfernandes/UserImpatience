import serial
import serial.tools.list_ports
import keyboard
import time

""" 
Example slist for model DI-1100
0x0000 = Analog channel 0, ±10 V range
0x0001 = Analog channel 1, ±10 V range
0x0002 = Analog channel 2, ±10 V range
0x0003 = Analog channel 3, ±10 V range
"""
slist = [0x0000, 0x0001]

ser = serial.Serial()

"""
Since model DI-1100 cannot scan slower that 915 Hz at the protocol level, 
and that rate or higher is not practical for this program, define a decimation 
factor to slow scan rate to a practical level. It defines the number of analog readings to average
before displaying them. By design, digital input values display instantaneously
without averaging at the same rate as decimated analog values.
Averaging n values on each analog channel is more difficult than simply using
every nth value, but is recommended since it reduces noise by a factor of n^0.5 
'decimation_factor' must be an integer value greater than zero. 
'decimation_factor' = 1 disables decimation and attemps to output all values.
"""
# Define a decimation factor variable
decimation_factor = 1000

# Contains accumulated values for each analog channel used for the average calculation
achan_accumulation_table = list(())

# Define flag to indicate if acquiring is active
acquiring = False

""" Discover DATAQ Instruments devices and models.  Note that if multiple devices are connected, only the 
device discovered first is used. We leave it to you to ensure that it's a DI-1100."""


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

# Sends a passed command string after appending <cr>


def send_cmd(command):
    ser.write((command+'\r').encode())
    time.sleep(.1)
    if not(acquiring):
        # Echo commands if not acquiring
        while True:
            if(ser.inWaiting() > 0):
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

# Configure the instrment's scan list


def config_scn_lst():
    # Scan list position must start with 0 and increment sequentially
    position = 0
    for item in slist:
        send_cmd("slist " + str(position) + " " + str(item))
        # Add the channel to the logical list.
        achan_accumulation_table.append(0)
        position += 1


while discovery() == False:
    discovery()
# Stop in case DI-1100 is already scanning
send_cmd("stop")
# Define binary output mode
send_cmd("encode 0")
# Keep the packet size small for responsiveness
send_cmd("ps 0")
# Configure the instrument's scan list
config_scn_lst()

# Define sample rate = 1 Hz, where decimation_factor = 1000:
# 60,000,000/(srate) = 60,000,000 / 60000 / decimation_factor = 1 Hz
send_cmd("srate 60000")
send_cmd("info 9")
ser.close()
SystemExit
