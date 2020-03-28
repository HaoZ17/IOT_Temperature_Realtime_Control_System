'''
Created on Feb 9, 2019
@author: Hao Zuo
'''
'''import system file location'''
import sys
sys.path.append('/home/pi/workspace/iot-devices/apps') #import system file location
# import smbus #import smbus
import threading #import thread function
from time import sleep #import sleep function from
from labs.common import ConfigUtil # import Configutil from common file
from labs.common import ConfigConst #get ConfigConst from common file 
from sense_hat import SenseHat


'''initiate i2cbus & get address from sensor'''
sense= SenseHat()
sense.clear()
# i2cBus = smbus.SMBus(1) # Use I2C bus No.1 on Raspberry Pi3 +
enableControl = 0x2D # control command
enableMeasure = 0x08 # enable measure command
accelAddr = 0x1C # address for IMU (accelerometer)
magAddr = 0x6A # address for IMU (magnetometer)
pressAddr = 0x5C # address for pressure sensor
humidAddr = 0x5F # address for humidity sensor
begAddr = 0x28 # setup starting reading address
totBytes = 1 # setup number of bytes
DEFAULT_RATE_IN_SEC = 5
Humiditydata = 0
Temperaturedata = 0


class I2CSenseHatAdaptor(threading.Thread):
    rateInSec = DEFAULT_RATE_IN_SEC
    
    '''constructor'''
    def __init__(self):
        super(I2CSenseHatAdaptor, self).__init__()
        self.config = ConfigUtil.ConfigUtil(ConfigConst.DEFAULT_CONFIG_FILE_NAME)
        self.config.loadConfig() #loading config file
        print('Configuration data...\n' + str(self.config)) #print 
        self.initI2CBus() #initiate the I2Cbus
        self.displayHumidityData()
        self.displayTemperatureData()
        
    '''define I2CBus function'''
#     def initI2CBus(self):
#         print("Initializing I2C bus and enabling I2C addresses...")
#         i2cBus.write_quick(humidAddr)#write humidAddr address into i2cbus
#     '''display humidity data via i2c'''
#     def displayHumidityData(self):
#         Humiditydata = i2cBus.read_i2c_block_data(humidAddr,begAddr,totBytes) #extract data from sensor by i2c and save into data
#         return Humiditydata[0]
#     '''display temperature data via sensehat'''
#     def displayTemperatureData(self):
#         Temperaturedata = sense.get_temperature() #extract data from sensor by i2c and save into data
#         return Temperaturedata


