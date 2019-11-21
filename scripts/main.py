from multiprocessing import Process, Queue
from tester import Tester
import time
import serial
import serial.tools.list_ports

if __name__ == '__main__':
    tester = Tester()
    tester.executeTest(1,2,3)
