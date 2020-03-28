import sys
sys.path.append('/home/pi/workspace/iot-devices/apps')
from time import sleep
from labs.final import TempSensorEmulator
#activate the thread
"""
    setup TempSensorEmulator configuration
        
"""
tempSensorEmulator=TempSensorEmulator.TempSensorEmulator()
tempSensorEmulator.daemon=True
tempSensorEmulator.enableEmulator=True

print("Final project test start")
print("Gat Data instance.")
print("Starting transfor data by Coap")
print("Starting system Final project Device app daemon thread...")

"""
    Start Emulator
        
"""
tempSensorEmulator.start()
while (True):
    sleep(5)
pass