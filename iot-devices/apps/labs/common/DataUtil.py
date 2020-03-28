'''
Created on Feb 18, 2019
toJsonFromSensorData and  toSensorDataFromJson
@author: alex
'''
from labs.common import ActuatorData
from labs.common import SensorData
import json


class DataUtil:
    
    def jsonToActuatorData(self, jsonData):
        
        with open(jsonData, encoding='utf-8') as jsonData:
                adDict = json.loads(jsonData.read())
                
        adDict = json.loads(jsonData)
        print(" decode [pre]  --> " + str(adDict))
        ad = ActuatorData.ActuatorData()
        ad.name = adDict['name']
        ad.timeStamp = adDict['timeStamp']
        ad.hasError = adDict['hasError']
        ad.command = adDict['command']
        ad.errCode = adDict['errCode']
        ad.statusCode = adDict['statusCode']
        ad.stateData = adDict['stateData']
        ad.curValue = adDict['curValue']
        #print(" decode [post] --> " + str(ad))
        return ad
    
    def jsonToSensorData(self, jsonData):
        
        with open(jsonData, encoding='utf-8') as jsonData:
                sdDict = json.loads(jsonData.read())
        
        sdDict = json.loads(jsonData)
        #print(" decode [pre]  --> " + str(sdDict))
        sd = SensorData.SensorData()
        sd.name = sdDict['name']
        sd.timeStamp = sdDict['timeStamp']
        sd.avgValue = sdDict['avgValue']
        sd.minValue = sdDict['minValue']
        sd.maxValue = sdDict['maxValue']
        sd.curValue = sdDict['curValue']
        sd.totValue = sdDict['totValue']
        sd.sampleCount = sdDict['sampleCount']
        
        #print(" decode [post] --> " + str(sd))
        return sd
    
    
    def toJson(self, name,timeStamp,avgValue,minValue,maxValue,curValue,totValue,sampleCount):
            
            json_info = {}
            json_info['name'] = name
            json_info['timeStamp'] = timeStamp
            json_info['avgValue'] = avgValue
            json_info['minValue'] = minValue
            json_info['maxValue'] = maxValue
            json_info['curValue'] = curValue
            json_info['totValue'] = totValue
            json_info['sampleCount'] = sampleCount
            
            with open("/Users/alex/git/iot-devices/data/data.json","w+") as j:
                
                print("Dictionary:  ")
                print(json_info)
                json.dump(json_info,j)
                
                
                
                jsonData = json.dumps(json_info)
                # write data into json file
    
                print("JSon:  ")
                print(jsonData)
            
            return jsonData