//see http://avilyne.com/?p=105
package com.ntier.rest.resource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Request;
import javax.inject.Singleton;

import com.ntier.rest.model.Location;
import com.ntier.android.REST.GaelLocation;

@Path("/location")
public class CopyOfLocationResource {
    private final static String FIRST_NAME = "firstName",
    							LAST_NAME = "lastName",
    							EMAIL = "email";
         
    private Location location = new Location(2); //, "JD Sample", "Location", "phaq08@sbcglobal.net");
    //private Connection conn = null;
    
    // The @Context annotation allows us to have certain contextual objects injected into this class.
    // UriInfo object allows us to get URI information (no kidding).
    @Context
    UriInfo uriInfo;
 
    // Another "injected" object. This allows us to use the information that's
    // part of any incoming request. We could, for example, get header information, or the requestor's address.
    @Context
    Request request;
     
    // Basic "is the service running" test
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.TEXT_PLAIN)
    public String respondAsReady() { return "GAEL service is ready!"; }
 
    @GET
    @Path("sample")
    @Produces(MediaType.APPLICATION_JSON)
    public Location getSampleLocation() {
         
        System.out.println("Returning sample location: id: " + location.id + ", lat: " + location.latitude );
         
        return location;
    }

    
    @GET
    @Path("status") 
    @Produces(MediaType.APPLICATION_JSON)
    public String Status() {//System.setProperty("file.encoding", "UTF-8");
    	StringBuilder retVal = new StringBuilder()
    							   .append("\nG.eo A.sset and E.vent L.ocator status:\n\tjava.nio.charset.Charset.defaultCharset:\t")
    							   .append(java.nio.charset.Charset.defaultCharset().toString() );

    	retVal.append(GaelConn.status());
    	
    	String retValStr = retVal.toString();
        System.out.println( retValStr );
        
        return retValStr;
    }
         

    // Use data from the client source to create a new Location object, returned in JSON format.  
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
/*        System.out.println("post location info:" 
        					+ "\n\t id:\t" + aLoc.id 
        					+ "\n\t updated:\t"  + aLoc.updated 
        					+ "\n\t provider:\t" + aLoc.provider 
        					+ "\n\t altitude:\t" + aLoc.altitude 
        					+ "\n\t latitude:\t" + aLoc.latitude
        					+ "\n\t longitude:\t" + aLoc.longitude);
*/
        System.out.println( location.toString() ); 

        return location;
                         
    }//postLocation
    
    public static Connection getConnection() throws SQLException { return GaelConn.getConnection(); } 
}//LocationResource


@Singleton
class GaelConn {
    private static DataSource dataSource;
    private static String JndiName = "jdbc/GaelDev";
    static {
		try {//http://netbeans.dzone.com/connection-pooling-glassfish-nb
			InitialContext ctx = new InitialContext();

			dataSource = (DataSource) ctx.lookup(JndiName); //eg. jdbc:oracle:thin:gaeluser1/gaeldev@localhost:1521:gaeldev
			ctx.close();
		} 
		catch (NamingException e2) { e2.printStackTrace(); }
    }//static

    public static Connection getConnection() throws SQLException {
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
   }
   
}//GAELConn

