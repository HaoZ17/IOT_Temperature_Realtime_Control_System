from time import sleep
from threading import Thread
from random import uniform
from labs.common import SensorData
from labs.final import SmtpClientConnector
from labs.final import CoAPConnector
from labs.final import I2CSenseHatAdaptor
import paho.mqtt.client as mqttClient
from labs.common.DataUtil import DataUtil
from labs.final import SenseHatLedActivator

'''Instantiate import classes'''

connector = SmtpClientConnector.SmtpClientConnector()
Coap =CoAPConnector.CoapClientConnector()
# Led = SenseHatLedActivator.SenseHatLedActivator()
# Led.setEnableLedFlag(True)
# Led.daemon=True
# Led.start()



# def on_connect(_Connection, _Data, _Token, resultCode):
#     """
#     build a connection to subscribe the topic ActuatorData
#     """
#     print("Client connected to server. Result: " + str(resultCode))
#     _Connection.subscribe("ActuatorData")
#  
# def on_message(clientConn, data, msg):
#     """
#     get the message from payload and transfer data between Json and sensordata
#     """
#     print("Received PUBLISH on topic {0}. Payload: {1}".format(str(msg.topic), str(msg.payload)))
#     Led.setDisplayMessage("Received PUBLISH on topic {0}. Payload: {1}".format(str(msg.topic), str(msg.payload)))


class TempSensorEmulator(Thread):
    #init the temperature range
    TlowVal=15
    ThighVal=30
    HlowVal=0
    HhighVal=100
    
    isPrevTempSet = False
    RATE_IN_SEC=10 
    #set alert information
    alertDiff = 5
    alertDiff1 = 5
    a = 1
    
    #setup a potential threshold
    tempdata = SensorData.SensorData()
    tempdata.setName("temperature")
    humiditydata = SensorData.SensorData()
    humiditydata.setName("humidity")    
#     i2cdata=I2CSenseHatAdaptor.I2CSenseHatAdaptor()
#     connector = SmtpClientConnector.SmtpClientConnector()
#     Led.setEnableLedFlag(True)

    # class init 
    def __init__(self, rateInSec=RATE_IN_SEC):
        super(TempSensorEmulator,self).__init__()
        self.rateInSec = rateInSec
    #emulate the sensor data
        
    #run function
        
    def run(self):
        while True:
            if self.enableEmulator:
                '''pick model to get data source a==0 from sensehat; a==1 from unifom function'''
                if self.a == 0:
                    self.curtempdata = self.i2cdata.displayHumidityData()
                    self.curhumiditydata = self.i2cdata.displayTemperatureData()
                    self.tempdata.addValue(self.curtempdata)
                    self.humiditydata.addValue(self.curhumiditydata)
                    print('\n--------------------')
                    print('New sensor readings:')
                    print('  ' + str(self.tempdata))
                    print('  ' + str(self.humiditydata))
                else:
                    self.curtempdata = uniform(float(self.TlowVal),float(self.ThighVal))
                    self.curhumiditydata = uniform(float(self.HlowVal),float(self.HhighVal))
                    self.tempdata.addValue(self.curtempdata)
                    self.humiditydata.addValue(self.curhumiditydata)
                    print('\n--------------------')
                    print('New sensor readings:')
                    print('  ' + str(self.tempdata))
                    print('  ' + str(self.humiditydata))
#                 

                '''COAP Connection to Gateway'''
                print('Transport data by protocol: CoAP | Sending data')
                Coap.handlePostTest("THdata", str(self.tempdata.curValue))
                Coap.handlePostTest("THdata", str(self.humiditydata.curValue))

                '''SMTP Function'''
                if self.isPrevTempSet == False:
                    self.prevtemp      = self.curtempdata
                    self.prevhumd      = self.curhumiditydata
                    self.isPrevTempSet = True
                else:
                    if ((abs(self.tempdata.curValue - self.tempdata.getAvgValue()) >= self.alertDiff) and 
                        (abs(self.humiditydata.curValue - self.humiditydata.getAvgValue()) >= self.alertDiff1)):
                        print('\n  Current temp&humd exceeds average by > ' + str(self.alertDiff) + '. Triggering alert...')
                        self.connector.publishMessage('Exceptional Temperature data [test]', self.tempdata)
                        self.connector.publishMessage('Exceptional Humidity data [test]', self.humiditydata)
                
                '''MQTT subscribe function'''
#                 mqttclient= mqttClient.Client()
#                 mqttclient.on_connect = on_connect
#                 mqttclient.on_message = on_message
#                 mqttclient.connect("172.20.10.2", 1883, 60)
#                 mqttclient.loop()
                
            sleep(self.rateInSec)
