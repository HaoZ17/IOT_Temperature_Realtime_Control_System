package NEU.ZuoHao.connecteddevices.labs.FinalProject;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import com.labbenchstudios.edu.connecteddevices.common.ConfigConst;

/*
 * author: Hao Zuo
 */

public class MqttGatewayToDevice implements MqttCallback
{
    // static
    private static final Logger _Logger = Logger.getLogger(MqttGatewayToDevice.class.getName());
    // params
    private String _protocol = ConfigConst.DEFAULT_MQTT_PROTOCOL;
    private String _host     = "127.0.0.1";
    private int    _port     = ConfigConst.DEFAULT_MQTT_PORT;
    private String _clientID;
    private String _brokerAddr;
    private MqttClient _mqttClient;
    // constructors
    /**
     * Default.
     *
     */
    public MqttGatewayToDevice()
    {
       // use defaults
       this(null, false);
    }
    
/**
* Constructor.
*
* @param host  The name of the broker to connect.
* @param isSecure Currently unused.
*/
	public MqttGatewayToDevice(String host, boolean isSecure)
	{
		super();
		// NOTE: 'isSecure' ignored for now
		if (host != null && host.trim().length() > 0) {
		   _host = host;
		}
		// NOTE: URL does not have a protocol handler for "tcp",
		// so we need to construct the URL manually
		_clientID = MqttClient.generateClientId();
		_Logger.info("Using client ID for broker conn: " + _clientID);
		_brokerAddr = _protocol + "://" + _host + ":" + _port;
		_Logger.info("Using URL for broker conn: " + _brokerAddr);
	}


//public methods
	public void connect()
	{
	if (_mqttClient == null) {
	   //build a connection with server.
		   MemoryPersistence persistence = new MemoryPersistence();
		   try {
		          _mqttClient = new MqttClient(_brokerAddr, _clientID, persistence);
		          MqttConnectOptions connOpts = new MqttConnectOptions();
		          connOpts.setCleanSession(true);
		          _Logger.info("Connecting to broker: " + _brokerAddr);
		          _mqttClient.setCallback(this);
		          _mqttClient.connect(connOpts);
				  _Logger.info("Connected to broker: " + _brokerAddr);
		   } catch (MqttException e) {
				_Logger.log(Level.SEVERE, "Failed to connect to broker: " + _brokerAddr, e);
		   }
		}
	}
// disconnect the connection
	public void disconnect()
	{
		try {
		   _mqttClient.disconnect();
		   _Logger.info("Disconnected from broker: " + _brokerAddr);
		} catch (Exception e) {
		   _Logger.log(Level.SEVERE, "Failed to disconnect from broker: " + _brokerAddr, e);
		} 	
	}
/**
* Publishes the given payload to broker directly to topic 'topic'.
*
* @param topic
* @param qosLevel
* @param payload
*/

	//publish the message
	public boolean publishMessage(String topic, int qosLevel, byte[] payload) {
		boolean success = false;
		try {
			_Logger.info("Publishing message to topic: " + topic);
			
			//create a new MqttMessage, pass 'payload' to the constructor
			MqttMessage msg = new MqttMessage(payload);
			//set the QoS to qosLevel
			msg.setQos(qosLevel);
			//call 'publish' on the MQTT client, passing the 'topic' and MqttMessage
			msg.setRetained(true);
			_mqttClient.publish(topic, msg);
			success = true;
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "Failed!!! to publish MQTT message: " + e.getMessage());
		}
		return success;
	}
	
	//  build the subscribe all topic
	public boolean subscribeToAll()
	{
		try {
		   _mqttClient.subscribe("$SYS/#");
		   return true;
		} catch (MqttException e) {
		   _Logger.log(Level.WARNING, "Failed to subscribe to all topics.", e);
		}
		
		return false;
	}
	
	//build the subscribe to a specific topic
	public boolean subscribeToTopic (String topic)
	{
		try {
			   _mqttClient.subscribe(topic);
			   return true;
			} catch (MqttException e) {
			   _Logger.log(Level.WARNING, "Failed to subscribe to all topics.", e);
			}
		return false;
	}
/*
* (non-Javadoc)
*
* @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.
* Throwable)
*/
	
	// if the connection lost, throw a exception.
	public void connectionLost(Throwable t)
	{
	// TODO: now what?
	_Logger.log(Level.WARNING, "Connection to broker lost. Will retry soon.", t);
	}
/*
* (non-Javadoc)
*
* @see
* org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.paho
* .client.mqttv3.IMqttDeliveryToken)
*/
	//if the message delivery, so output a token.
	public void deliveryComplete(IMqttDeliveryToken token)
	{
		try {
		
		         _Logger.info("Delivery complete: " + token.getMessageId() + " - " + token.getResponse() + " - "
		                     + token.getMessage());
		 } catch (Exception e) {
		       _Logger.log(Level.SEVERE, "Failed to retrieve message from token.", e);} 
	}
/*
* (non-Javadoc)
*
* @see
* org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.String,
* org.eclipse.paho.client.mqttv3.MqttMessage)
*/
	public void messageArrived(String data, MqttMessage msg) throws Exception
	{
	 _Logger.info("Message arrived: " + data + ", " + msg.getId());
	}
}