package NEU.ZuoHao.connecteddevices.labs.common;

import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.FileNotFoundException;

public class DataUtil {
	
	// fromJson method is to read json string from json file
	public SensorData fromJson() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		//call sensordata method 
		SensorData sensordata = new SensorData();

		JsonObject jo = new JsonParser().parse(new FileReader("/Users/alex/git/iot-devices/data/data.json")).getAsJsonObject();
		// read file by fileReader covert data into SensorData
		sensordata.name = jo.get("name").getAsString();
		sensordata.timeStamp = jo.get("timeStamp").getAsString();
		sensordata.avgValue = jo.get("avgValue").getAsFloat();
		sensordata.minValue = jo.get("minValue").getAsFloat();
		sensordata.maxValue = jo.get("maxValue").getAsFloat();
		sensordata.curValue = jo.get("curValue").getAsFloat();
		sensordata.totValue = jo.get("totValue").getAsFloat();
		sensordata.sampleCount = jo.get("sampleCount").getAsInt();
		//print all of data and check result
		System.out.println("Name: " + sensordata.name);
		System.out.println("Timestamp: " + sensordata.timeStamp);
		System.out.println("Average : " + sensordata.avgValue);
		System.out.println("Min: " + sensordata.minValue);
		System.out.println("Max: " + sensordata.maxValue);
		System.out.println("Current : " + sensordata.curValue);
		System.out.println("totValue : " + sensordata.totValue);
		System.out.println("SampleCount: " + sensordata.sampleCount);
		
		return sensordata;
		
	}
	
	// convert sensorData instance to Json file
	public String sensorDataToJson(SensorData sensorData) {
		String jsonData = null;
		
		if(sensorData != null) {
			Gson gson = new Gson();
			jsonData = gson.toJson(sensorData);
		}
		System.out.println(jsonData);
		
		return jsonData;
	}
	
	// convert jsondata to actuatorData
	public ActuatorData jsonToActuatorData(String jsonData) {
		ActuatorData actuatorData = null;
		
		if(jsonData !=null && jsonData.trim().length() > 0) {
			Gson gson = new Gson();
			actuatorData = gson.fromJson(jsonData, ActuatorData.class);
		}
		
		return actuatorData;
	}

}
