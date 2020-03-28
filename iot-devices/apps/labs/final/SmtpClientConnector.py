import threading
import smtplib

from labs.common import ConfigUtil
from labs.common import ConfigConst

from email.mime.text import MIMEText 
from email.mime.multipart import MIMEMultipart

class SmtpClientConnector (threading.Thread):
    
    def __init__(self):
        """
        Initiate smtp function
        """
        self.config = ConfigUtil.ConfigUtil('../../../data/ConnectedDevicesConfig.props')
        self.config.loadConfig()
        print('Configuration data...\n' + str(self.config)) 
        print('============= Setting Done! =============')
        
    #HOST_KEY,PORT_KEY,FROM_ADDRESS_KEY,TO_ADDRESS_KEY,USER_AUTH_TOKEN_KEY
    def publishMessage(self, topic, data):
        
        """
            setup publish message Configuration
        """        
        host = 'smtp.163.com'
        port = 587
        fromAddr = 'csyecd_zdh@163.com'
        toAddr = 'csyecd_zdh@163.com'
        authToken = 'zuodahao17'
        
        print("host:"+host)
        print("port:"+str(port))
        print("port:"+authToken)
        msg = MIMEMultipart()  #mimeMultipart
        msg['From'] = fromAddr
        msg['To'] = toAddr
        msg['Subject'] = topic
        msgBody = str(data)
        msg.attach(MIMEText(msgBody))   #mimeText  
        msgText = msg.as_string()
        smtpServer = smtplib.SMTP_SSL(host, port)
        smtpServer.ehlo()
        smtpServer.login(fromAddr, authToken)
        smtpServer.sendmail(fromAddr, toAddr, msgText)
        smtpServer.close()