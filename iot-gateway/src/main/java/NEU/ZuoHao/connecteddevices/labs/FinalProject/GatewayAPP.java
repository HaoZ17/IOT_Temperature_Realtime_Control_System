package NEU.ZuoHao.connecteddevices.labs.FinalProject;


public class GatewayAPP
{
	private static GatewayAPP _App;
	//server main method
    public static void main(String[] args)
    {
       _App = new GatewayAPP();
       try {
              _App.start();
       } catch (Exception e) {
              e.printStackTrace();
       } 
    }
    // private build a connecter
    private CoapServerConnector _coapServer;
    // constructors
    public GatewayAPP()
    {
	super(); 
	}
    // public methods
    public void start()
    {
       _coapServer = new CoapServerConnector();
       _coapServer.start();
       
    }
}