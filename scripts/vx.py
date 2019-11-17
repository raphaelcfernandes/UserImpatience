import pdb
import time
import vxi11
import time

i = vxi11.Instrument("192.168.1.1", "gpib0,1")
assert i.open() == 1, "Could not connect"
print(i.ask("CURV?"))
