import os
from datetime import datetime
import json

class SensorData():
    timeStamp = None
    name = 'Temperature' 
    curValue = 0
    avgValue = 0
    minValue = 30
    maxValue = 0
    totValue = 0
    sampleCount = 0
    fjsonstring= 'data'
    tjsonstring= 'data'

    def __init__(self):
        self.timeStamp = str(datetime.now())
    def addValue(self, newVal):
        self.sampleCount += 1
        self.timeStamp  = str(datetime.now())
        self.curValue   = newVal
        self.totValue  += newVal
        if (self.curValue < self.minValue):
            self.minValue = self.curValue
        if (self.curValue > self.maxValue):
            self.maxValue = self.curValue
        if (self.totValue != 0 and self.sampleCount > 0):
            self.avgValue = self.totValue / self.sampleCount
    def getAvgValue(self):
        return self.avgValue
    def getMaxValue(self):
        return self.maxValue
    def getMinValue(self):
        return self.minValue
    def getValue(self):
        return self.curValue
    def setName(self, name):
        self.name = name
    
    def toJson(self,data):
        self.fjsonstring=data
        with open("data_file.json", "w") as write_file:
            json.dump(self.tjsonstring, write_file)
    
    def fromJason(self):
        with open("data_file.json", "r") as read_file:
            self.fjsonstring = json.load(read_file)
        return self.fjsonstring
        
        
    def __str__(self):
        customStr = \
            str('name = '+ self.name + ',' + 
            os.linesep + 'TimeStamp:    ' + self.timeStamp + 
            os.linesep + ' curValue = ' + str(self.curValue) + 
            os.linesep + ' avgValue = ' + str(self.avgValue) + 
            os.linesep + ' minValue = ' + str(self.minValue) + 
            os.linesep + ' maxValue = ' + str(self.maxValue) +
            os.linesep + ' totValue = ' + str(self.totValue) +
            os.linesep + ' SampleCount = ' + str(self.sampleCount) 
            )
            
        return customStr
