package NEU.ZuoHao.connecteddevices.labs.FinalProject;
import java.util.logging.Level;
import java.util.logging.Logger;
import NEU.ZuoHao.connecteddevices.labs.FinalProject.MqttClientConnector;

import com.labbenchstudios.edu.connecteddevices.common.ConfigConst;
/*
 * author: Hao Zuo
 */
public class CloudSubscriber {
	
    private String _host = ConfigConst.DEFAULT_UBIDOTS_SERVER;
	private String _pemFileName= "/Users/alex/git/iot-gateway/ubidots_cert.pem";
	private String token="A1E-wVPU1XIE23hpsykRJb0wfc7ZQByEvo";
	private MqttClientConnector _mqttClient;
	
	// static
	private static final Logger _Logger = Logger.getLogger(CloudSubscriber.class.getName());
	private static CloudSubscriber _App;
	
	//Main function:
	public static void main(String[] args) {
		// new a test app
		_App = new CloudSubscriber();
		try {
			// call start function()
			_App.start();
		} catch (Exception e) {
			// if error, throw a exception.
			_Logger.log(Level.WARNING, "Connection fail.", e);
		}
	}

	// params

	//constructor
	public CloudSubscriber() {
		super();
	}

	//subscribe method.
	public void start() {
		//Create a new MqttClient:_Client and connection
		_mqttClient = new MqttClientConnector(_host, token, _pemFileName);
		_mqttClient.connect();
		//setup the topic of subscribe 
		String topic1 = "/v1.6/devices/assignment8/tempactuator";
		//subscribe the topic
		_mqttClient.subscribeToTopic(topic1); 
	}
}