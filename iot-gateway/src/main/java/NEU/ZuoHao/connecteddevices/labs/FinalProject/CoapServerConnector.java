package NEU.ZuoHao.connecteddevices.labs.FinalProject;

import java.util.logging.Logger;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

public class CoapServerConnector
{
	//static
    private static final Logger _Logger =
       Logger.getLogger(CoapServerConnector.class.getName());
    // private var's
    private CoapServer _coapServer;
    
    // constructors
    public CoapServerConnector()
    {
	super(); 
    }
    
    // public methods
    public void addResource(CoapResource resource)
    {
       if (resource != null) {
              _coapServer.add(resource);
       } 
    }
    //start server method
    public void start()
    {
       if (_coapServer == null) {
              _Logger.info("Creating CoAP server instance and 'Temp&Humd data' handler...");
              _coapServer = new CoapServer();
              //use handler to deal with the received options
              TempResourceHandler tempHandler = new TempResourceHandler();
              _coapServer.add(tempHandler);
       }
       _Logger.info("Starting CoAP server...");
       _coapServer.start();
   }
   //stop server method
   public void stop()
   {
       _Logger.info("Stopping CoAP server...");
       _coapServer.stop();
   }
}