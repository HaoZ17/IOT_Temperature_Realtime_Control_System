from coapthon.client.helperclient import HelperClient
from labs.common import ConfigUtil
from labs.common import ConfigConst
#import socket


client = None

class CoapClientConnector():


    config = None
    serverAddr = None
    host = "172.20.10.2"
    port = 5683
    
    
    def __init__(self):
    
        """
        Initate the configuration of CoAP Protocal
        
        """
        self.config = ConfigUtil.ConfigUtil('../../../data/ConnectedDevicesConfig.props')
        self.config.loadConfig()
        print('Configuration data...\n' + str(self.config)) 
        print('============= Setting Done! =============')
        self.host = self.config.getProperty(ConfigConst.COAP_GATEWAY_SECTION, ConfigConst.DEFAULT_HOST )
        self.port = int(self.config.getProperty(ConfigConst.COAP_GATEWAY_SECTION, ConfigConst.DEFAULT_COAP_PORT))
        self.serverAddr = (self.host, self.port)
        print('URL(IP): ' + str(self.serverAddr))
        self.url = "coap://" + self.host + ":" + str(self.port) + "/temp"
    
    
    def initClient(self):
        """
        try:
            tmp = socket.gethostbyname(self.host)
            host = tmp
        except socket.gaierror:
            pass
        """
        try:
            self.client = HelperClient(server=(self.host, self.port))
            print("Created CoAP client ref: " + str(self.client))
            print(" coap://" + self.host + ":" + str(self.port))
        except Exception:
            print("Failed to create CoAP helper client reference using host: " + self.host)
            pass
    
    
    def handleGetTest(self,resource):
    
        print("Testing GET for resource: " + str(resource))
        self.initClient()
        response = self.client.get(resource)
        if response:
            print(response.pretty_print())
        else:
            print("No response received for GET using resource: " + resource)
        self.client.stop()
    
    
    def handlePostTest(self, resource, payload):
    
        print("Testing POST for resource: " + resource + ", payload: " + payload)
        self.initClient()
        response = self.client.post(resource, payload)
        if response:
            print(response.pretty_print())
            print("Notice: POST Completed!")
        else:
            print("No response received for POST using resource: " + resource)
        self.client.stop()
        
        
    def handlePutTest(self, resource, payload):
    
        print("Testing PUT for resource: " + resource + ", payload: " + payload)
        self.initClient()
        response = self.client.put(resource, payload)
        if response:
            print(response.pretty_print())
        else:
            print("No response received for put using resource: " + resource)
        self.client.stop()
        
    def handleDeleteTest(self, resource, payload):
    
    
        print("Testing delete for resource: " + resource + ", payload: " + payload)
        self.initClient()
        response = self.client.delete(resource, payload)
        if response:
            print(response.pretty_print())
        else:
            print("No response received for delete using resource: " + resource)
        self.client.stop()