package NEU.ZuoHao.connecteddevices.labs.FinalProject;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.google.gson.Gson;
import com.labbenchstudios.edu.connecteddevices.common.ConfigConst;
import com.labbenchstudios.edu.connecteddevices.common.ConfigUtil;
import com.ubidots.ApiClient;
import com.ubidots.DataSource;

import NEU.ZuoHao.connecteddevices.labs.FinalProject.MqttGatewayToDevice;

/*
 * author: Hao Zuo
 */

public class MqttClientConnector implements MqttCallback {
	// static
	private static final Logger _Logger = Logger.getLogger(MqttClientConnector.class.getName());
	// params
	private MqttClient _Client;
	private String  _protocol = ConfigConst.SECURE_MQTT_PROTOCOL;
	private String  _host     = "things.ubidots.com";
	private int     _port     = 8883;
	private String  _clientID;
	private String  _brokerAddress;
	private Boolean _isSecure;
	private String  _userName="A1E-wVPU1XIE23hpsykRJb0wfc7ZQByEvo";
	private String  _password;
	private String _token;
	private String _pemFileName= "/Users/alex/git/iot-gateway/ubidots_cert.pem";
	private MqttGatewayToDevice _mqttToDevice;

	// constructors
	/**
	 * Default.
	 *
	 */
	public MqttClientConnector() {
		// use defaults
	}
	/**
	 * Constructor.
	 *
	 * @param host     The name of the broker to connect.
	 * @param isSecure Currently unused.
	 */

	public MqttClientConnector(String host, String token, String pemFileName)
	{
		super();
		// NOTE: 'isSecure' ignored for now
		if (host != null && host.trim().length() > 0) {
		   _host = host;
		}
		_token=token;
		//System.out.println(_token);
		if (pemFileName != null) {
			File file = new File(pemFileName);
			if (file.exists()) {
				_protocol = "ssl";
				_port = 8883;
				_pemFileName = pemFileName;
				_isSecure = true;
				_Logger.info("PEM file valid. Using secure connection: " + _pemFileName);
			} else {
				_Logger.warning("PEM file invalid. Using insecure connection: " + pemFileName);
			}
		}
		_clientID = MqttClient.generateClientId();
		_Logger.info("Using client ID for broker conn: " + _clientID);
		_brokerAddress = _protocol + "://" + _host + ":" + _port;
		_Logger.info("Using URL for broker conn: " + _brokerAddress);
	}
	
	/**
	 * connect mqtt (ssl) with username password and token.
	 */
	public void connect() {

		if (_Client == null) {
			MemoryPersistence persistence = new MemoryPersistence();
			try {
				System.out.println("_brokerAddr: --" + _brokerAddress);
				System.out.println("_clientID: --" + _clientID);
				_Client = new MqttClient(_brokerAddress, _clientID, persistence);
				MqttConnectOptions cOptions = new MqttConnectOptions();
				System.out.println(_userName);
				System.out.println(_password);
				cOptions.setCleanSession(true);
				cOptions.setUserName(_userName);
				initSecureConnection(cOptions);
				_Client.setCallback(this);
				_Client.connect(cOptions);
				_Logger.info("Connected to broker: " + _brokerAddress);
			} catch (MqttException e) {
				_Logger.log(Level.SEVERE, "Failed!! to connect to broker: " + _brokerAddress, e);
			}
		}

	}

	//iniate seure Connection
	private void initSecureConnection(MqttConnectOptions connOpts) {
		try {
			_Logger.info("Configuring TLS...");
			SSLContext sslContext = SSLContext.getInstance("ssl");
			KeyStore keyStore = readCertificate();
			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keyStore);
			sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
			connOpts.setSocketFactory(sslContext.getSocketFactory());
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "Failed to initialize secure MQTT connection.", e);
		}
	}
	// read certificate via keystore.
	private KeyStore readCertificate()
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream fis = new FileInputStream(_pemFileName);
		BufferedInputStream bis = new BufferedInputStream(fis);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		ks.load(null);
		while (bis.available() > 0) {
			Certificate cert = cf.generateCertificate(bis);
			ks.setCertificateEntry("adk_store" + bis.available(), cert);
		}
		return ks;
	}

	/* co */
	public void disconnect() {
		try {
			_Client.disconnect();
			_Logger.info("Disconnected from broker: " + _brokerAddress);
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "Failed to disconnect from broker: " + _brokerAddress, e);
		}
	}

	/**
	 * Publishes the given payload to broker directly to topic 'topic'.
	 *
	 * @param topic
	 * @param qosLevel
	 * @param payload
	 */

	// publish the message
	public boolean publishMessage(String topic, int qosLevel, byte[] payload) {
		boolean success = false;
		try {
			_Logger.info("Publishing message to topic: " + topic);

			// create a new MqttMessage, pass 'payload' to the constructor
			MqttMessage msg = new MqttMessage(payload);

			// set the QoS to qosLevel
			msg.setQos(qosLevel);

			// call 'publish' on the MQTT client, passing the 'topic' and MqttMessage
			msg.setRetained(true);
			_Client.publish(topic, msg);
			success = true;
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "Failed!!! to publish MQTT message: " + e.getMessage());
		}
		return success;
	}

	// build the subscribe all topic
	public boolean subscribeToAll() {
		try {
			_Client.subscribe("$SYS/#");
			return true;
		} catch (MqttException e) {
			_Logger.log(Level.WARNING, "Failed to subscribe to all topics.", e);
		}

		return false;
	}

	// build the subscribe to a specific topic
	public boolean subscribeToTopic(String topic) {
		try {
			System.out.println("Topic: " + topic);
			_Client.subscribe(topic);
			return true;
		} catch (Exception e) {
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
	public void connectionLost(Throwable t) {
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
	// if the message delivery, so output a token.
	public void deliveryComplete(IMqttDeliveryToken token) {
		try {

			_Logger.info("Delivery complete: " + token.getMessageId() + " - " + token.getResponse() + " - "
					+ token.getMessage());
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "Failed to retrieve message from token.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.String,
	 * org.eclipse.paho.client.mqttv3.MqttMessage)
	 */
	public void messageArrived(String data, MqttMessage msg) throws Exception {
		System.out.print("Get Message From cloud: " + msg.toString() );
		_Logger.info("Message arrived: " + data + ", " + msg.getId());
		_mqttToDevice = new MqttGatewayToDevice();
		_mqttToDevice.connect();
		//setup topic name and payload
		String topic = "ActuatorData";
		//get data from sensorData
		String payload = msg.toString();
		// publish message
		_mqttToDevice.publishMessage(topic, 2, payload.getBytes());
		System.out.println(" Gateway Mqtt already sent actuator data back to Device!!!!!!!!! please check");
		// disconnect
		_mqttToDevice.disconnect();
	}
}