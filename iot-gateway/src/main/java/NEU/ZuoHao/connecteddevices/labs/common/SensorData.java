package NEU.ZuoHao.connecteddevices.labs.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SensorData{
	
	// initialize the timeStamp, name, curValue, avgValue, minValue, maxValue, totValue and sampleCount
	public String name = "Hao's Data";
	public String timeStamp = null;
	public float  curValue = 0.0f;
	public float  avgValue = 0.0f;
	public float  minValue = 0.0f;
	public float  maxValue = 0.0f;
	public float  totValue = 0.0f;
	public int    sampleCount = 0;
	
	// constructor of SensorData class and setup time now
	public SensorData()
	{
		super();
	    updateTimeStamp();
		
	}
	
	//addValue from newVal
	public void addValue(float val)
	{
	    updateTimeStamp();
	    ++sampleCount;
	    curValue  = val;
	    totValue += val;
	    if (curValue < minValue) {
	          minValue = curValue;
	}
	    if (curValue > maxValue) {
	          maxValue = curValue;
	}
	    if (totValue != 0 && sampleCount > 0) {
	          avgValue = totValue / sampleCount;
	} }
	
	//get the average value
	public float getAvgValue()
	{
	    return avgValue;
	}
	
	//get max value
	public float getMaxValue()
	{
		
		return maxValue;
    }
	
	//get minimum value
    public float getMinValue()
    {
        return minValue;
    }
    
    //get the name
    public String getName()
    {
        return name;
    }
    
    //get the value
    public float getValue()
    {
        return curValue;
    }
    
    // set the name
    public void setName(String name)
    {
        if (name != null && name.trim().length() > 0) {
              this.name = name;
} }
   
    // update the time
    public void updateTimeStamp()
    {
        timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm.ss").format(new Date());
    }
    
    //set the output format of all data 
    public String output() {
		String customStr = this.name + ":\n" + 
							"\tTime:      " + this.timeStamp + "\n" + 
							"\tCurrent:      " + this.curValue + "\n" +
							"\tAverage:      " + this.avgValue + "\n" +
							"\tSamples:      " + this.sampleCount + "\n" +
							"\tMin:      " + this.minValue + "\n" +
							"\tMax:      " + this.maxValue;
		return customStr;
	}
	
}
