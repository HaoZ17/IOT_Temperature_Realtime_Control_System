# Python-Java_based_IOT_Temperature_Realtime_Control_System
1. Description:
My final project’s goal is to monitor and manage the data of the complex environments, such as automated factories.I want to set up a temperature alarm system based on the iot system. The equipment determines the environment by monitoring temperature and humidity This system is divided into three parts: device part, gateway part and the cloud ubidots. In the device module, DeviceApp reads the temperature and humidity through I2c senserAdaptor. Through CoAP on the local network, deviceApp passes sensordata to the gateway App. When Gateway App gets sensordata, Gateway App connects Internet cloud such as ubidots through HTTP Protocol. In the cloud ubidots, according to the data analytic server,ubidot monitors the data and controls the temperature via the Actuator. When device received
the actuator data, the led will display the actuator data.

2. Listing of all modules:
- CoAPConnector.py
- DeviceAPP.py
- I2CSenseHatAdaptor.py
- SenseHatLedActivator.py
- SmtpClientConnector.py
- TempSensorEmulator.py
- CloudSubscriber.java
- CoapServerConnector.java
- GatewayAPP.java
- MqttClientConnector.java
- MqttGatewayToDevice.java
- TempResourceHandler.java
- TempSensorPublisherApp.java

3. System Design Block Diagram:

<img src = "pic/pic1.png" width="800" align: center>

4. Output:

<img src = "pic/1.png" width="800" align: center>

<img src = "pic/2.png" width="800" align: center>

<img src = "pic/3.png" width="800" align: center>

<img src = "pic/4.png" width="800" align: center>

<img src = "pic/5.png" width="800" align: center>

<img src = "pic/6.png" width="800" align: center>

<img src = "pic/7.png" width="800" align: center>

<img src = "pic/8.png" width="800" align: center>