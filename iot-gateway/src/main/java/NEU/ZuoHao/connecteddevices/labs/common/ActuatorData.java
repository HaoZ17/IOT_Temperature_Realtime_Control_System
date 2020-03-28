package NEU.ZuoHao.connecteddevices.labs.common;


import java.text.SimpleDateFormat;
import java.util.Date;

public class ActuatorData {
	
	// initialize the command
	int COMMAND_OFF = 0;
	int COMMAND_ON = 1;
	int COMMAND_SET = 2;
	int COMMAND_RESET = 3;

	// initialize the status code
	int STATUS_IDLE = 0;
	int STATUS_ACTIVE = 1;
	
	// initialize the error code
	int ERROR_OK = 0;
	int ERROR_COMMAND_FAILED = 1;
	int ERROR_NON_RESPONSIBLE = -1;
	
	// initialize all attributes
	String timeStamp = null;
	String name = "Hao's data";
	Boolean hasError = false;
	int command = 0;
	int errCode = 0;
	int statusCode = 0;
	String stateData = null;
	float val = 0.0f;
	
	// constructor 
	public ActuatorData() {
		updateTimeStamp();
	}
	
	// get the command
	public int getCommand() {
		return command;
	}
	
	//get the name
	public String getName() {
		return name;
	}
	
	
	//get the state data
	public String getStateData() {
		return stateData;
	}
	
	// get the status code
	public int getStatusCode() {
		return statusCode;
	}
	
	// get the error code
	public int getErrorCode() {
		return errCode;
	}
	
	// get the value
	public float getValue() {
		return val;
	}
	
	// make sure if has error
	public boolean hasError() {
		return hasError;
	}
	
	// set the command
	public void setCommand(int command) {
		this.command = command;
	}
	
	//set the name
	public void setName(String name) {
		this.name = name;
	}
	
	// set the state data
	public void setStateData(String stateData) {
		this.stateData = stateData;
	}
	
	// set the status code
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	// set the error code
	public void setErrorCode(int errCode) {
		this.errCode = errCode;
		
		if(errCode !=0) {
			this.hasError = true;
		}else {
			this.hasError = false;
		}
	}
	
	// set the value
	public void setValue(float val) {
		this.val = val;
	}
	
	// update the data from actuator data
	public void updateData(ActuatorData data) {
		this.command = data.getCommand();
		this.statusCode = data.getStatusCode();
		this.errCode = data.getErrorCode();
		this.stateData = data.getStateData();
		this.val = data.getValue();
	}
	
	// update the time
	public void updateTimeStamp()
    {
        timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm.ss").format(new Date());
    }
	
	// the output format
	public String output() {
		String customStr = this.name + ":\n" + 
							"\tTime:      " + this.timeStamp + "\n" + 
							"\tCommand:      " + this.command + "\n" +
							"\tStatus Code:      " + this.statusCode + "\n" +
							"\tError Code:      " + this.errCode + "\n" +
							"\tState Data:      " + this.stateData + "\n" +
							"\tValue:      " + this.val;
		return customStr;
	}
		
	

}