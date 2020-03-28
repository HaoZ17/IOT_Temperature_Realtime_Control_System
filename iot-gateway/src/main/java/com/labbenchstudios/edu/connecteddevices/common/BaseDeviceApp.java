/**
 * Copyright (c) 2015-2019. Andrew D. King. All Rights Reserved.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.labbenchstudios.edu.connecteddevices.common;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides basic application functionality for all
 * sub-classes. It will enable easy processing of command line
 * arg's, as well as process startup (initialization) and shutdown.
 *
 * @author aking
 */
public abstract class BaseDeviceApp
{
    // static member var section
	private static final Logger _Logger =
		Logger.getLogger(BaseDeviceApp.class.getName());
	
    /** Indicates a normal exit when used as the exit code */
    public static final int NORMAL_EXIT_CODE   =  0;

    /** Indicates an abnormal, but not erroneous, exit */
    public static final int ABNORMAL_EXIT_CODE =  1;

    /** Indicates an error-forced exit */
    public static final int ERROR_EXIT_CODE    = -1;

    /** Default server port */
    public static final int DEFAULT_SERVER_PORT = 45832;
    
    /** Default server host */
    public static final String DEFAULT_SERVER_HOST = "localhost";
    
    /** Cmd line arg indicator ('-') */
    public static final String DASH_STR        = "-";

    /** Help cmd line arg - long */
    public static final String HELP_ARG_LONG   = "help";

    /** Help cmd line arg - short */
    public static final String HELP_ARG_SHORT  = "h";

    // static method section

    
    // private member var section
    
    private Properties _configProps    = null;
    private String     _appName        = this.getClass().getSimpleName();
    private String[]   _args           = null;
    private int        _exitCode       = NORMAL_EXIT_CODE;


    // constructor section

    /**
     * Create a default BaseApp with only a name passed in by the sub-class.
     * Use of this constructor assumes there are no command-line args, nor
     * is there an available configuration file.
     *
     */
    protected BaseDeviceApp()
    {
        this (null, null);
    }

    /**
     * Create a BaseApp object with params. This requires the sub-class
     * to pass in both the name of the application and the name of
     * the app's configuration file. Null is allowed in both cases,
     * but not recommended, as it severely limits some functionality.
     *
     * @param appName The name of the application. If null, the simple
     * class name will be used instead.
     */
    protected BaseDeviceApp(String appName)
    {
    	this(appName, null);
    }

    /**
     * Create a BaseApp object with 'args'. This requires the sub-class
     * to pass in both the name of the application and the name of
     * the app's configuration file. Null is allowed in both cases,
     * but not recommended, as it severely limits some functionality.
     *
     * @param appName The name of the application. If null, the simple
     * class name will be used instead.
     * @param args The command line args passed in to the app. If null,
     * empty, or otherwise invalid, this parameter will be ignored.
     */
    protected BaseDeviceApp(String appName, String[] args)
    {
        setAppName(appName);
        processCommandArgs(args);
    }


    // public methods

    /**
     * Returns the name of this application (set by the sub-class).
     * Null is a valid return value (if no name is set).
     *
     * @return String The name of this application.
     */
    public String getAppName()
    {
        return _appName;
    }
    
    /**
     * Returns the original arguments passed into this application.
     * 
     * @return String[]
     */
    public String[] getArgs()
    {
    	return _args;
    }
    
    /**
     * Returns the current exit code, which should be set internally, or
     * by the sub-class. This method should always be called to retrieve
     * the exit code, vs. just using the variable (in case the sub-class
     * overrides the get/set method(s).
     *
     * @return int The exit code for this app.
     */
    public int getExitCode()
    {
        return _exitCode;
    }
    
    /**
     * Entry point to start the application. This will invoke
     * {@link #start()} within a try / catch block, exiting
     * gracefully (if possible) should an exception get thrown.
     * 
     */
    public void startApp()
    {
    	try {
    		start();
    	} catch (Exception e) {
    		_Logger.log(Level.SEVERE, "Failed to start application: " + this.getAppName(), e);
    		
    		setExitCode(-1);
    		stopApp();
    	}
    }

    /**
     * Stops this application. The implementation can be overridden, but
     * it's likely that most app's can use what's given here. HOWEVER, if
     * an app is expected to have open resources (sockets, files, etc.)
     * when this method is called, it should override it, close all appropriate
     * resources, and finally call super.stop () as the last thing it does.
     *
     */
    public void stopApp()
    {
        try {
        	stop();
        	
            _Logger.log(
            	Level.INFO,
            	"App stop() method invoked. Exiting application: " +
            	getExitCode());
        } catch (Exception e) {
        	if (getExitCode() == 0) {
        		setExitCode(-1);
        	}
        	
        	_Logger.log(
        		Level.SEVERE,
        		"Failed to cleanly stop app. Exiting application:" +
        		getExitCode(),
        		e);
        } finally {
            System.exit(getExitCode());
        }
    }

    
    // protected methods

    /**
     * Returns the Properties for this application.
     * 
     * @return Properties
     */
    protected Properties getConfigurationProps()
    {
    	return _configProps;
    }
    
    /**
     * Sets the internally used exit code. This method will generally be
     * used by the sub-class to custom handle exit code values as they
     * relate to the concrete implementation.
     *
     * @param exitCode The exit code (int) for this app.
     */
    protected void setExitCode(int exitCode)
    {
        _exitCode = exitCode;
    }

    /**
     * Starts this application. The sub-class must implement this method.
     *
     * @exception DeviceApplicationException Thrown if an exception occurs during startup.
     */
    protected abstract void start()
        throws DeviceApplicationException;

    /**
     * Stops this application. The sub-class must implement this method.
     *
     * @exception DeviceApplicationException Thrown if an exception occurs during stop.
     */
    protected abstract void stop()
    	throws DeviceApplicationException;


    // private methods

    /**
     * This method processes the standard command-line args, such as those
     * that handle displaying help and usage information. All others are
     * relegated to the sub-classes implementation of processLocalArgs.
     *
     */
    private void processCommandArgs(String[] args)
    {
    	_args = args;
    	
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                
                // check for 'help' arg
            }
        }
    }

	/**
     * This method simply sets the name of the application. Null
     * is allowed.
     *
     * @param appNameThe name of the application. Usually used in UI controls.
     */
    private void setAppName(String appName)
    {
        if (appName != null && appName.length () > 0) {
            _appName = appName;
        }
    }

} // class BaseApp
