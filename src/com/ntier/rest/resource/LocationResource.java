package com.ntier.rest.resource;
//see http://avilyne.com/?p=105
//see /GAEL1/src/com/ntier/android/accounts/Contract.java
//https://localhost:8181/GaelWebSvcGF4/rest/GAEL/Status
import java.sql.*;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.google.gson.*;

import com.ntier.rest.model.Deployment;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//import com.sun.jersey.core.header.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
//import com.sun.jersey.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataParam;

//import org.glassfish.jersey.media.multipart.MultiPartFeature;


@Path("/GAEL")
public class LocationResource {
    static final String Deploy = "DEV1";
    // The @Context annotation allows us to have certain contextual objects injected into this class.
    @Context
    UriInfo uriInfo;
 
    // Another "injected" object. This allows us to use the information that's
    // part of any incoming request. We could, for example, get header information, or the requestor's address.
    @Context
    Request request;
      
    @GET
    @Path("Test")
    @Produces(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.TEXT_PLAIN)
    public static Deployment Test() { // Basic "is the service running" test
    	final String delimiter = "\n.GAELTest.\t\t";
    	System.out.print( delimiter  );
    	//return Test;
        
        CallableStatement cs = null;
        Connection conn = null;
        ResultSet rs = null;
        Deployment Test = null;
        try {
          conn =  LocationResource.getConnection();
          cs = conn.prepareCall("{? = call gaelup.pkgGaelUp.sfTest}");
          cs.registerOutParameter(1, java.sql.Types.VARCHAR);
          rs = cs.executeQuery();
       	  Test = new Deployment(Deployment.Module.WEB,
				  Deployment.Deploy.TST,
				  cs.getString(1),
				  "hey this is another jdtest") ;

        } 
        catch (Exception e) { e.printStackTrace();  } 
        finally {
    		try {
            	if (conn != null) conn.close();
            	if (cs != null)   cs.close(); 
            	if (rs != null)   rs.close(); 
    		} 
    		catch (SQLException e) { e.printStackTrace(); } 
        }

    	
    	System.out.println( Test  );
        
    	return  Test;
    }//Test

/*    public static String Test() { // Basic "is the service running" test
    	final String delimiter = "\n.GAELTest.\t\t";
    	System.out.print( delimiter  );
        
        CallableStatement cs = null;
        Connection conn = null;
        ResultSet rs = null;
        String Test = "DATABASE STATUS UNKNOWN";
Exittest:try {
          conn =  LocationResource.getConnection();
          if (conn == null) break Exittest;
          cs = conn.prepareCall("{? = call gaelup.pkgGaelUp.sfTest}");
          cs.registerOutParameter(1, java.sql.Types.VARCHAR);
          rs = cs.executeQuery();
          Test = cs.getString(1);
        } 
        catch (SQLException e) { e.printStackTrace(); } 
        finally {
    		try {
            	if (conn != null) conn.close();
            	if (cs != null)   cs.close(); 
            	if (rs != null)   rs.close(); 
    		} 
    		catch (SQLException e) { e.printStackTrace(); } 
        }
        
    	System.out.println( Test  );
        
    	return  Test;
    }//Test
*/

/*    @GET
    @Path("sample")
    @Produces(MediaType.APPLICATION_JSON)
    public Location getSampleLocation() {
    	Location location = new Location(); //, "JD Sample", "Location", "phaq08@sbcglobal.net");

        System.out.println("Returning sample location: id: " + location.id + ", lat: " + location.latitude );
        return location;
    }
*/
    
    @GET
    @Path("Status") 
    @Produces(MediaType.APPLICATION_JSON)
    public static String Status() {//System.setProperty("file.encoding", "UTF-8");
    	StringBuilder retVal = new StringBuilder()
    							   .append("\nG.eo A.sset and E.vent L.ocator status:\n\tjava.nio.charset.Charset.defaultCharset:\t")
    							   .append(java.nio.charset.Charset.defaultCharset().toString() );

    	retVal.append(GaelConn.status());
    	
    	String retValStr = retVal.toString();
        System.out.println( retValStr );
        
        return retValStr;
    }
         

/*    // Use data from the client source to create a new Location object, returned in JSON format.  
    //@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    @Path("newlocation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
//    public Location postLocation( MultivaluedMap<String, String> locParams ) {
    public Location postLocation( Location aLoc ) {

        Location location = new Location(aLoc.id,
        								 aLoc.updated,
        								 aLoc.provider,
        								 aLoc.altitude,
        								 aLoc.latitude,
        								 aLoc.longitude        								 
        									);
        System.out.println( location.toString() ); 

        return location;
                         
    }//postLocation

*/

    @POST
    @Path("InsertuLocations")
    @Consumes(MediaType.APPLICATION_JSON)
    public static String InsertuLocations (String uLocations) {
    	String delimiter = "\n.InsertuLocations.\n";
    	System.out.println( delimiter  );
    	System.out.println( "\nuLocations:\t " + uLocations +"\n" );
        
        CallableStatement cs = null;
        Connection conn = null;
        String JSON;
        try {
          conn =  LocationResource.getConnection();
          
          cs = conn.prepareCall("{call gaelup.pkgGaelUp.spuLocations(?)}");
          JSON = new JsonParser()
					.parse( uLocations )
					.getAsJsonObject()
					.toString();

          cs.setString(1, JSON);
          
          cs.execute();
        } 
        catch (SQLException e) { e.printStackTrace(); } 
        catch (JsonSyntaxException e) { 
        	JSON = "MALFORMED JSON: " + uLocations;
        	System.out.println(JSON);
        }         
        finally {
    		try {
            	if (conn != null) conn.close();
            	if (cs != null)  cs.close(); 
    		} 
    		catch (SQLException e) { e.printStackTrace(); } 
        }
        
    	System.out.println( delimiter  );
        
    	return  uLocations.toString();
    }//InsertuLocations

        
        
    @POST
    @Path("getPipes")
    @Consumes(MediaType.APPLICATION_JSON)
    public static String getPipes (String uLocations) {
    	String delimiter = "\n.InsertuLocations.\n";
    	System.out.println( delimiter  );
    	System.out.println( "\nuLocations:\t " + uLocations +"\n" );

    	JsonObject jsonObject = new JsonParser()
    							.parse( uLocations
    									//.replace("\\\"", "") 
    									)
    							.getAsJsonObject();

        
        CallableStatement cs = null;
        Connection conn = null;
        try {
          conn =  LocationResource.getConnection();
          
          cs = conn.prepareCall("{call gaelup.pkgGaelUp.spuLocations(?)}");
          cs.setString(1, jsonObject.toString());
          cs.execute();
        } 
        catch (SQLException e) { e.printStackTrace(); } 
        finally {
    		try {
            	if (conn != null) conn.close();
            	if (cs != null)  cs.close(); 
    		} 
    		catch (SQLException e) { e.printStackTrace(); } 
        }
        
    	System.out.println( delimiter  );
        
    	return  uLocations.toString();
    }//getPipes

        
@Path("/fileupload")    
//public class UploadFileService {
//https://puspendu.wordpress.com/2012/08/23/restful-webservice-file-upload-with-jersey/     
        @POST
        @Consumes(MediaType.MULTIPART_FORM_DATA)
        public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {
            //@FormDataParam("fileDetail") FormDataContentDisposition fileDetail) {
	
        	System.out.println( "in public Response uploadFile\n" );
        	
			String uploadedFileLocation = "c://GEOFielder//images//" + fileDetail.getFileName(),
					//String uploadedFileLocation = "c://" + fileDetail.getFileName(),
			//String uploadedFileLocation = "c://" + "jdtest.jpg", //fileDetail.getFileName(),
				   output = "File uploaded via Jersey based RESTFul Webservice to: " + uploadedFileLocation;
     
            // save it
        	System.out.println( "public Response uploadFile\n" );

			saveToFile(uploadedInputStream, uploadedFileLocation);
        	System.out.println( "after saveToFile\n" );
     
//            String output = "File uploaded via Jersey based RESTFul Webservice to: " + uploadedFileLocation;
     
            return Response.status(200).entity(output).build();
     
        }//uploadFile
     
        // save uploaded file to new location
        private void saveToFile(InputStream uploadedInputStream,
        						String uploadedFileLocation) 
        {
        	System.out.println( "in saveToFile\n" );

        	try {
                //OutputStream out;// = null;
                int read = 0;
                byte[] bytes = new byte[1024];
     
                OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
                while ((read = uploadedInputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
                out.close();
            } catch (IOException e) {e.printStackTrace(); }
     
        }//saveToFile
     
//   }
    
    
    
@POST
@Path("ACRAException")
@Consumes(MediaType.APPLICATION_JSON)
public static String ACRAException (String postedException) {
	String delimiter = ".exception.";
	System.out.println( delimiter  );
	//if (myException.length() < 1) myException = "myException is EMPTY!!"; 
	System.out.println( postedException  );
	System.out.println( delimiter  );

	JsonObject jsonObject = new JsonParser()
								.parse(postedException)
								.getAsJsonObject();
	//begin debug
    Set<Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
    for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
        System.out.println( entry.getKey() + ": " +  entry.getValue()  ); 
    }
    System.out.println( "#Entries = " + entrySet.size()); //end debug
	
    
    CallableStatement cs = null;
    Connection conn = null;
    try {
      conn =  LocationResource.getConnection();
      
      cs = conn.prepareCall("{call gaelup.pkgGaelUp.spACRAException(?)}");
      cs.setString(1, jsonObject.toString());
      cs.execute();
    } 
    catch (SQLException e) { e.printStackTrace(); } 
    finally {
		try {
        	if (conn != null) conn.close();
        	if (cs != null)  cs.close(); 
		} 
		catch (SQLException e) { e.printStackTrace(); } 
    }
    
    
	return  postedException.toString();
	}//ACRAException

    public static Connection getConnection() throws SQLException { return GaelConn.getConnection(); } 
}//LocationResource


@Singleton
class GaelConn {
    private static DataSource dataSource;
/*ConnPool setup for Glassfish4
1) Copy ojdbc6.jar to C:\glassfish4\glassfish\domains\domain1\lib\ext
2) see http://netbeans.dzone.com/connection-pooling-glassfish-nb
3) snippet from C:\glassfish4\glassfish\domains\domain1\config\domain.xml
    <jdbc-connection-pool datasource-classname="oracle.jdbc.pool.OracleConnectionPoolDataSource" res-type="javax.sql.ConnectionPoolDataSource" steady-pool-size="6" name="GaelDev" idle-timeout-in-seconds="450">
    <property name="portNumber" value="1521"></property>
    <property name="databaseName" value="gaeldev"></property>
    <property name="networkProtocol" value="tcp"></property>
    <property name="serverName" value="localhost"></property>
    <property name="user" value="GaelPool1"></property>
    <property name="password" value="gaeldev"></property>
    <property name="URL" value="jdbc:oracle:thin:GaelPool1/gaeldev@localhost:1521:gaeldev"></property>
  </jdbc-connection-pool>
  <jdbc-resource pool-name="GaelDev" description="uses ora userid GaelPool1" jndi-name="jdbc/GaelDev"></jdbc-resource>
</resources>
<servers>

4) to test ConnPool, browse: https://localhost:8181/GaelWebSvcGF4/rest/location/status 
*/    
    private static String JndiName = "jdbc/GaelUp";
    static {
		try {
			InitialContext ctx = new InitialContext();

			dataSource = (DataSource) ctx.lookup(JndiName); //eg. jdbc:oracle:thin:gaeluser1/gaeldev@localhost:1521:gaeldev
			ctx.close();
		} 
		catch (NamingException e2) { e2.printStackTrace(); }
    }//static

/*    public static Connection getConnection() throws SQLException {
       	Connection conn = null;
    	try { 
    		conn = dataSource.getConnection();
    		conn.setAutoCommit(false); //http://javarevisited.blogspot.in/2012/01/improve-performance-java-database.html
    	} 
    	catch (SQLException e) {
			e.printStackTrace();
			final String errString = "\n\tERROR: GaelConn.getConnection() cannot get conn!\n";
			System.out.println(errString);
		}
    	
    	return conn;
    }
*/    
    public static Connection getConnection() throws SQLException {
       	Connection conn = dataSource.getConnection();
       	conn.setAutoCommit(false); //http://javarevisited.blogspot.in/2012/01/improve-performance-java-database.html
    	return conn;
    }
    
    public static String status() {//TODO https://developer.jboss.org/wiki/UsefulRoutines
    	//http://stackoverflow.com/questions/6759428/how-to-get-list-of-all-registered-ds-names-object-in-jboss-programmatically
    	//NamingEnumeration bindings = initialContext.listBindings("");
    	String retVal = "";
    	Connection conn = null;
    	//try with resources not avail until android 19.
    	try {
			conn = getConnection();
			retVal = status(conn);
		} catch (SQLException e) {
			//e.printStackTrace();
			final String errString = "\n\tERROR: GaelConn.status() cannot get conn!\n";
			System.out.println(errString);
			retVal += errString;
		}
    	finally {try { conn.close(); } 
				    	catch (SQLException e) {
							//e.printStackTrace();
							final String errString = "\n\tERROR: GaelConn.status() cannot close conn!\n";
							System.out.println(errString);
							retVal += errString;
						}
    	}
    	
    	return retVal;
    }
    
    public static String status(Connection conn) throws SQLException{
    	StringBuilder retVal = new StringBuilder().append("\nConnection status:");
    	try {
    		if (conn.isValid(0)) {
        	retVal.append("\n\tconn.toString:\t").append(conn.toString())
		  		  .append("\n\t\t.isValid( timeout = 0 ):\t").append(conn.isValid(0))
		  		  .append("\n\t\t.isClosed:\t").append(conn.isClosed())
		  		  .append("\n\t\t.isReadOnly:\t").append(conn.isReadOnly())
		  		  .append("\n\t\t.AutoCommit:\t").append(conn.getAutoCommit())
		  		  .append("\n\t\t.getCatalog:\t").append(conn.getCatalog())
		  		  .append("\n\t\t.getWarnings:\t").append(conn.getWarnings())
		  		  .append(getMetaData(conn));
    		}
    		else retVal.append("\n\t\t Conn NOT valid!\n");
/*  		  These next two throw SQLExceptions!!!
  		  .append("\nconn.getNetworkTimeout:\t").append(conn.getNetworkTimeout())
  		  .append("\nconn.getSchema:\t").append(conn.getSchema())*/
  		} 
    	catch (SQLException e) {
			e.printStackTrace();
			final String errString = "\n\tERROR: GaelConn.status(conn) cannot get conn info!\n";
			System.out.println(errString);
			retVal.append(errString);
			throw e;
    	}
    	
    	return retVal.append("\n").toString();
   }
    
   public static String getMetaData(Connection conn){
   	StringBuilder retVal = new StringBuilder().append("\nConn metadata:");
	try {
		DatabaseMetaData Mdata = conn.getMetaData();
    	retVal.append("\n\tautoCommitFailureClosesAllResultSets:\t").append( Mdata.autoCommitFailureClosesAllResultSets())
    		  .append("\n\tdataDefinitionCausesTransactionCommit:\t").append( Mdata.dataDefinitionCausesTransactionCommit())
    		  .append("\n\tdataDefinitionIgnoredInTransactions:\t").append( Mdata.dataDefinitionIgnoredInTransactions())
    		  .append("\n\tdoesMaxRowSizeIncludeBlobs:\t").append( Mdata.doesMaxRowSizeIncludeBlobs())

    		  .append("\n\tgetDatabaseMajorVersion:\t").append( Mdata.getDatabaseMajorVersion())
    		  .append("\n\tgetDatabaseMinorVersion:\t").append( Mdata.getDatabaseMinorVersion())
    		  .append("\n\tgetDatabaseProductName:\t").append( Mdata.getDatabaseProductName())
    		  .append("\n\tgetDatabaseProductVersion:\t").append( Mdata.getDatabaseProductVersion())
    		  
    		  .append("\n\tgetDefaultTransactionIsolation:\t").append( Mdata.getDefaultTransactionIsolation())
    		  
    		  .append("\n\tgetDriverMajorVersion:\t").append( Mdata.getDriverMajorVersion())
    		  .append("\n\tgetDriverMinorVersion:\t").append( Mdata.getDriverMinorVersion())
    		  
    		  .append("\n\tgetDriverName:\t").append( Mdata.getDriverName())
    		  .append("\n\tgetDriverVersion:\t").append( Mdata.getDriverVersion())
    		  .append("\n\tgetJDBCMajorVersion:\t").append( Mdata.getJDBCMajorVersion())
    		  .append("\n\tgetJDBCMinorVersion:\t").append( Mdata.getJDBCMinorVersion())
    		  
    		  .append("\n\tgetMaxCharLiteralLength:\t").append( Mdata.getMaxCharLiteralLength())
    		  .append("\n\tgetMaxStatements:\t").append( Mdata.getMaxStatements())
    		  .append("\n\tgetSQLStateType:\t").append( Mdata.getSQLStateType())

/*Security issues!
 *     		  .append("\n\tgetURL:\t").append( Mdata.getURL())
    		  .append("\n\tgetUserName:\t").append( Mdata.getUserName())
*/
    		  .append("\n\tsupportsMultipleOpenResults:\t").append( Mdata.supportsMultipleOpenResults())
    		  .append("\n\tsupportsSavepoints:\t").append( Mdata.supportsSavepoints())
    		  ;
	} 
	catch (SQLException e) {
		e.printStackTrace();
		final String errString = "\n\tERROR: GaelConngetMetaData(Connection conn) cannot get MetaData!\n";
		System.out.println(errString);
		retVal.append(errString);
	}
	

   	return retVal.append("\n").toString();
   }//getMetaData
}//GAELConn

