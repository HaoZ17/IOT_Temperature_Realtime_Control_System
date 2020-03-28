package NEU.ZuoHao.connecteddevices.labs.FinalProject;

import java.util.logging.Logger;
import com.ubidots.ApiClient;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.labbenchstudios.edu.connecteddevices.common.ConfigConst;
import com.labbenchstudios.edu.connecteddevices.common.ConfigUtil;
import com.ubidots.ApiClient;
import com.ubidots.DataSource;
import com.ubidots.Variable;

import NEU.ZuoHao.connecteddevices.labs.FinalProject.MqttClientConnector;
import NEU.ZuoHao.connecteddevices.labs.FinalProject.MqttGatewayToDevice;

public class TempResourceHandler extends CoapResource{
public String humddata,tempdata;
private Boolean type = true;
private String _apikey="A1E-54db8b9c93ce4f750cd61df9ed173febbc3e";
private Variable _cloudSensorVar;
private String _protocol = ConfigConst.SECURE_MQTT_PROTOCOL;
private MqttClientConnector _mqttFromCloud;
private String _host = ConfigConst.DEFAULT_UBIDOTS_SERVER;
private String _pemFileName= "/Users/alex/git/iot-gateway/ubidots_cert.pem";
private String token="A1E-wVPU1XIE23hpsykRJb0wfc7ZQByEvo";

private float t,h;

	//constructor
	public TempResourceHandler() {
		super("THdata",true);
	}
	// get logername
	private static final Logger _Logger = Logger.getLogger(TempResourceHandler.class.getName());
	
	//handle all of received options such as get put post delete 
	public void handleGET(CoapExchange ce) {
		ce.respond(ResponseCode.VALID,"GET worked!");
		_Logger.info("Received GET request from client.");
	}
	
	public void handlePUT(CoapExchange ce) {
		ce.respond(ResponseCode.VALID,"GET worked!");
		_Logger.info("Received GET request from client.");
	}
	
	/**
	 * Handle a POST from the client. This will ...
	 * 
	 * @param ce The CoapExchange container from the client.
	 */
	public void handlePOST(CoapExchange ce) {
		String responseMsg = "Here's the reponse to Temp&Humd data request::" + super.getName();
//		System.out.println(ce.getRequestText());
		ce.respond(ResponseCode.VALID, responseMsg);
		
		//Publish Sensordata to Ubidot Clouds By HTTP protocol
		_Logger.info("Received GET request from client.");
		
		if (type==true) { 
			tempdata=ce.getRequestText();
			type=false;
			System.out.println("Get temperature data from Rasp: "+ tempdata);
			t = Float.parseFloat(tempdata);
		}
		else {
			humddata=ce.getRequestText();
			type=true;
			System.out.println("Get humdity data from Rasp: "+ humddata);
			h = Float.parseFloat(humddata);
		}
		initClient(t,h);
		
		//Subscribe feed
		_mqttFromCloud = new MqttClientConnector(_host, token, _pemFileName);
		_mqttFromCloud.connect();
		//setup the topic of subscribe 
		String topic1 = "/v1.6/devices/assignment8/tempactuator";
		//subscribe the topic
		_mqttFromCloud.subscribeToTopic(topic1); 
		_mqttFromCloud.disconnect();
	
		
	}
	public void handleDELETE(CoapExchange ce) {
		ce.respond(ResponseCode.VALID,"GET worked!");
		_Logger.info("Received GET request from client.");
	}
	
	
	
	
	private void initClient(float a,float b) {
//		ConfigUtil configUtil = ConfigUtil.getInstance();
		String baseUrl ="https://things.ubidots.com/api/v1.6/";
		//System.out.println(_apikey);
		//System.out.println(baseUrl);
		ApiClient _apiClient = new ApiClient(_apikey);
		DataSource dataSource = _apiClient.getDataSource("5c9e8fcc59163635f81f0517");
		Variable[] variables = dataSource.getVariables();
		for (Variable var : variables) {
			if (var.getId().equals("5cba812ec03f977c376922af")) {
				// found cloud sensor variable
				_cloudSensorVar = var;
				_cloudSensorVar.saveValue(b);
				System.out.println("Successfully, publish the humdity value to ubidots");
			}
			if (var.getId().equals("5c9e8fce5916360eac188468")) {
				// found cloud sensor variable
				_cloudSensorVar = var;
				_cloudSensorVar.saveValue(a);
				System.out.println("Successfully, publish the temperature value to ubidots");
			}
			
		}
		_apiClient.fromToken(_apikey);
		_apiClient.setBaseUrl(baseUrl);
	}
	
}
