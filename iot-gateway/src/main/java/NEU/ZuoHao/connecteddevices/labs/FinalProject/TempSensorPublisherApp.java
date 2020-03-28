package NEU.ZuoHao.connecteddevices.labs.FinalProject;

import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.logging.Level;

import com.google.gson.Gson;
import com.labbenchstudios.edu.connecteddevices.common.ConfigConst;
import NEU.ZuoHao.connecteddevices.labs.FinalProject.MqttClientConnector;
import NEU.ZuoHao.connecteddevices.labs.common.SensorData;

/*
 * author: Hao Zuo
 */

public class TempSensorPublisherApp {
	// static
	private static final Logger _Logger = Logger.getLogger(TempSensorPublisherApp.class.getName());
	private static TempSensorPublisherApp _App; 
	
    private String _protocol = ConfigConst.SECURE_MQTT_PROTOCOL;
    private String _host = ConfigConst.DEFAULT_UBIDOTS_SERVER;
	private String _pemFileName= "/Users/alex/git/iot-gateway/ubidots_cert.pem";
	private String token="A1E-wVPU1XIE23hpsykRJb0wfc7ZQByEvo";
	
	
	//initiate a mqtt-connector
	private MqttClientConnector _mqttClient;

	//constructor
	public TempSensorPublisherApp() {
		super();
	}

	//Main function:
	public static void main(String[] args) {
		
		// new a test app
		_App = new TempSensorPublisherApp();
		try {
			// call start function()
			_App.start();
		} catch (Exception e) {
			// if error, throw a exception.
			_Logger.log(Level.WARNING, "Connection fail.", e);
		}
	}
	
	// public methods
	/**
	 * Connect to the MQTT client and publish a test message to the given topic
	 */
	public void start() {
		//Create a new MqttClient:_Client and connection
		_mqttClient = new MqttClientConnector(_host, token, _pemFileName);
		_mqttClient.connect();
		//setup topic name and payload
		String topic = "/v1.6/devices/assignment8/tempsensor";
		//setup payload
		String payload = "27";
		// publish message
		_mqttClient.publishMessage(topic, 0, payload.getBytes());
		// disconnect
		_mqttClient.disconnect();
	}
}