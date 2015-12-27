//see http://avilyne.com/?p=105
package com.ntier.rest.model;
 
//import static org.acra.ReportField.*;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class ACRAException {
	public final static String datetimeformat = "yyyyMMddHHmmssSSS" ;

	private String 	REPORT_ID, // Report Identifier
	/**
	* Application version code. This is the incremental integer version code
	* used to differentiate versions on the android market.
	* 
	* @see android.content.pm.PackageInfo#versionCode
	*/
	APP_VERSION_CODE,
	/**
	* Application version name.
	* 
	* @see android.content.pm.PackageInfo#versionName
	*/
	APP_VERSION_NAME,
	/**
	* Application package name.
	* 
	* @see android.content.Context#getPackageName()
	*/
	PACKAGE_NAME,
	/**
	* Base path of the application's private file folder.
	* 
	* @see android.content.Context#getFilesDir()
	*/
	FILE_PATH,
	/**
	* Device model name.
	* 
	* @see android.os.Build#MODEL
	*/
	PHONE_MODEL,
	/**
	* Device android version name.
	* 
	* @see android.os.Build.VERSION#RELEASE
	*/
	ANDROID_VERSION,
	/**
	* Android Build details.
	* 
	* @see android.os.Build
	*/
	BUILD ,
	/**
	* Device brand (manufacturer or carrier).
	* 
	* @see android.os.Build#BRAND
	*/
	BRAND,
	/**
	* Device overall product code.
	* 
	* @see android.os.Build#PRODUCT
	*/
	PRODUCT,
	/**
	* Estimation of the total device memory size based on filesystem stats.
	*/
	TOTAL_MEM_SIZE,
	/**
	* Estimation of the available device memory size based on filesystem stats.
	*/
	AVAILABLE_MEM_SIZE,
	/**
	* Contains key = value pairs defined by the application developer during
	* the application build.
	*/
	//BUILD_CONFIG ,
	/**
	* Contains key = value pairs defined by the application developer during
	* the application execution.
	*/
	CUSTOM_DATA ,
	/**
	* The Holy Stack Trace.
	*/
	STACK_TRACE,
	/**
	* A hash of the stack trace, taking only method names into account.<br>
	* Line numbers are stripped out before computing the hash. This can help you
	* uniquely identify stack traces.
	*/
	//STACK_TRACE_HASH,
	/**
	* {@link Configuration} fields state on the application start.
	* 
	* @see Configuration
	*/
	INITIAL_CONFIGURATION ,
	/**
	* {@link Configuration} fields state on the application crash.
	* 
	* @see Configuration
	*/
	CRASH_CONFIGURATION ,
	/**
	* Device display specifications.
	* 
	* @see android.view.WindowManager#getDefaultDisplay()
	*/
	DISPLAY ,
	/**
	* Comment added by the user in the CrashReportDialog displayed in
	* {@link ReportingInteractionMode#NOTIFICATION} mode.
	*/
	USER_COMMENT,
	USER_APP_START_DATE, //User date on application start.
	USER_CRASH_DATE, //User date immediately after the crash occurred.
	DUMPSYS_MEMINFO,//* Memory state details for the application process.

	/**
	* Content of the android.os.DropBoxManager (introduced in API level 8).
	* Requires READ_LOGS permission.
	*/
	DROPBOX,
	LOGCAT,//Logcat default extract. Requires READ_LOGS permission.
	EVENTSLOG,//Logcat eventslog extract. Requires READ_LOGS permission.
	RADIOLOG, //Logcat radio extract. Requires READ_LOGS permission.
	IS_SILENT,//True if the report has been explicitly sent silently by the developer.
	/**
	* 
	*/
	DEVICE_ID, //Device unique ID (IMEI). Requires READ_PHONE_STATE permission.
	/**
	* Installation unique ID. This identifier allow you to track a specific
	* user application installation without using any personal data.
	*/
	INSTALLATION_ID,
	/**
	* User email address. Can be provided by the user in the
	* {@link ACRA#PREF_USER_EMAIL_ADDRESS} SharedPreference.
	*/
	USER_EMAIL,
	DEVICE_FEATURES, //Features declared as available on this device by the system. 
	ENVIRONMENT, 
	SETTINGS_SYSTEM, //External storage state and standard directories.

	SETTINGS_SECURE, /** Secure settings (applications can't modify them). */
	/**
	* Global settings, introduced in Android 4.2 (API level 17) to centralize settings for multiple users.
	*/
	SETTINGS_GLOBAL, 
	/**
	* SharedPreferences contents
	*/
	SHARED_PREFERENCES ,
	/**
	* Content of your own application log file. To be configured with
	* {@link ReportsCrashes#applicationLogFile()} to define the path/name of
	* the log file and {@link ReportsCrashes#applicationLogFileLines()} to set
	* the number of lines you want to be retrieved.
	*/
	APPLICATION_LOG,
	/**
	* Since Android API Level 16 (Android 4.1 - Jelly Beans), retrieve the list
	* of supported Media codecs and their capabilities (color format, profile
	* and level).
	*/
	MEDIA_CODEC_LIST,
	/**
	* Retrieves details of the failing thread (id, name, group name).
	*/
	THREAD_DETAILS,
	/**
	* Retrieves the user IP address(es).
	*/
	USER_IP;
 

	
    //no-arg constructor required for webservice marshal/unmarshal serialization
    // TODO test private no-arg cstr private Location(){} AND  private members too.
    public ACRAException() {}
 

    public ACRAException(
    		String user_email,
    		String settings_global,
    		String device_features,
    		String phone_model,
    		String device_id,
    		String settings_secure,
    		String installation_id,
    		String settings_system,
    		String thread_details,
    		String android_version,
    		String package_name,
    		String app_version_code,
    		String crash_configuration,
    		String eventslog,
    		String user_crash_date,
    		String build,
    		String stack_trace,
    		String product,
    		String display,
    		String user_ip,
    		String logcat,
    		String app_version_name,
    		String radiolog,
    		String available_mem_size,
    		String user_app_start_date,
    		String custom_data,
    		String brand,
    		String initial_configuration,
    		String total_mem_size,
    		String file_path,
    		String environment,
    		String report_id ) {
    	USER_EMAIL =  user_email;
    	SETTINGS_GLOBAL =  settings_global;
    	DEVICE_FEATURES =  device_features;
    	PHONE_MODEL =  phone_model;
    	DEVICE_ID =  device_id;
    	SETTINGS_SECURE =  settings_secure;
    	INSTALLATION_ID =  installation_id;
    	SETTINGS_SYSTEM =  settings_system;
    	THREAD_DETAILS =  thread_details;
    	ANDROID_VERSION =  android_version;
    	PACKAGE_NAME =  package_name;
    	APP_VERSION_CODE =  app_version_code;
    	CRASH_CONFIGURATION =  crash_configuration;
    	EVENTSLOG =  eventslog;
    	USER_CRASH_DATE =  user_crash_date;
    	BUILD =  build;
    	STACK_TRACE =  stack_trace;
    	PRODUCT =  product;
    	DISPLAY =  display;
    	USER_IP =  user_ip;
    	LOGCAT =  logcat;
    	APP_VERSION_NAME =  app_version_name;
    	RADIOLOG =  radiolog;
    	AVAILABLE_MEM_SIZE = available_mem_size ;
    	USER_APP_START_DATE =  user_app_start_date;
    	CUSTOM_DATA = custom_data ;
    	BRAND =  brand;
    	INITIAL_CONFIGURATION = initial_configuration ;
    	TOTAL_MEM_SIZE =  total_mem_size;
    	FILE_PATH =  file_path;
    	ENVIRONMENT =  environment;
    	REPORT_ID =  report_id;
    	 
	}//cstr ACRAException()
    
    @Override  
    public String toString() {
    	return new StringBuilder().append("DEVICE_ID: ")
    					   .append(DEVICE_ID)
    					   .append("\nUSER_IP: ")
    					   .append(USER_IP)
    					   .append("\nINSTALLATION_ID: ")
    					   .append(INSTALLATION_ID)
    					   .append("\nTHREAD_DETAILS: ")
    					   .append(THREAD_DETAILS)
    					   .append("\nREPORT_ID: ")
    					   .append(REPORT_ID)
    					   .toString();
    }//toString
    	  
}//class ACRAException
 