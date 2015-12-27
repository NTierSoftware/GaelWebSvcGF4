//see http://avilyne.com/?p=105
package com.ntier.rest.model;
 
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.ntier.rest.resource.LocationResource;


@XmlRootElement
public class Location {
	public final static long NoLocation = -1;
	public final static String datetimeformat = "yyyyMMddHHmmssSSS" ;

	public long id, 
				altitude,
				latitude,
				longitude;
    
    public String updated, provider;    
 
   //instance initializer
    { final Date created = Calendar.getInstance().getTime();        
      final DateFormat df = new SimpleDateFormat(datetimeformat);
      this.updated = df.format(created);

      this.id = this.altitude = this.latitude = this.longitude = Location.NoLocation;  
      this.provider = "NONE?";
      
    }//instance initializer
 
    //no-arg constructor required for webservice marshal/unmarshal serialization
    // TODO test private no-arg cstr private Location(){} AND  private members too.
    public Location() {this.id = -999;}
 
    //INSERT new location
    public Location(long id, 
    				String updated,
    				String provider, 
    				long altitude,
    				long latitude,
    				long longitude 
    				) {
        CallableStatement cs = null;
        Connection conn = null;
        try {
          conn =  LocationResource.getConnection();

          cs = conn.prepareCall("{call gael.pkgGael.insertLocations(?, ?, ?, ?, ?, ?)}");
          cs.setLong(1, id);
          cs.setString(2, updated);
          cs.setString(3, provider);
          cs.setLong(4, altitude);
          cs.setLong(5, latitude);
          cs.setLong(6, longitude);

          cs.execute();
//		  cs.executeUpdate();

        } 
        catch (SQLException e) { e.printStackTrace(); } 

		try {
        	if (conn != null) conn.close();
        	if (cs != null)  cs.close(); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }//cstr Location()

    public Location(long id) {
		CallableStatement cs = null;
		Connection conn = null;
		try {
		  conn =  LocationResource.getConnection();
		
		  cs = conn.prepareCall("{call gael.pkgGael.getLocation(?, ?, ?, ?)}");
		  cs.setLong(1, id);
		  cs.registerOutParameter(1, java.sql.Types.INTEGER);
		  cs.registerOutParameter(2, java.sql.Types.FLOAT);
		  cs.registerOutParameter(3, java.sql.Types.FLOAT);
		  cs.registerOutParameter(4, java.sql.Types.FLOAT);
 
			// execute stored procedure
		  cs.execute();
		  this.id = cs.getLong(1);
		  this.altitude = cs.getLong(2);
		  this.latitude = cs.getLong(3);
		  this.longitude = cs.getLong(4);
		} 
		catch (SQLException e) { e.printStackTrace(); } 

		try {
        	if (conn != null) conn.close();
        	if (cs != null)  cs.close(); 
		} catch (SQLException e) {
			String SQLState = e.getSQLState(),
				   message = e.getMessage(),
				   LocalizedMessage = e.getLocalizedMessage();
			
			System.out.println("message: " + message);
			System.out.println("LocalizedMessage: " + LocalizedMessage);
			System.out.println("SQLstate: " + SQLState);
		} 
	}//cstr Location()
    
    @Override  
    public String toString() {
    	return new StringBuilder().append("id: ").append(id)
								  .append(", updated: ").append(updated)
								  .append(", provider: ").append(provider)
								  .append(", altitude: ").append(altitude)
								  .append(", latitude: ").append(latitude)
								  .append(", longitude: ").append(longitude)
								  .toString();    			
    }//toString()  
}//class Location
 