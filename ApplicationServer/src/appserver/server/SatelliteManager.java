package appserver.server;

import appserver.comm.ConnectivityInfo;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * SatelliteManager Class
 * 
 * @author Dr.-Ing. Wolf-Dieter Otte
 * @author Group 5
 */
public class SatelliteManager {

    // (the one) hash table that contains the connectivity information of all satellite servers
    static private Hashtable<String, ConnectivityInfo> satellites = null;

    /**
     * SatelliteManager constructor 
     */
    public SatelliteManager() {
        // ...
        satellites = new Hashtable<>();
    }

    /**
     * registers a satellite using given connection information
     * 
     * @param satelliteInfo identity and connection info for the satellite to add
     */
    public void registerSatellite(ConnectivityInfo satelliteInfo) {
        // ... add satellite to satellites
        satellites.put(satelliteInfo.getName(), satelliteInfo);
        
    }

    /**
     * searches known satellites for a satellite with the requested name
     * and returns the connection information
     * 
     * @param satelliteName name of the satellite to look for 
     * @return the found satellites connection information
     */
    public ConnectivityInfo getSatelliteFromName(String satelliteName) {
        // .. find satellite using satellite name and return connection info
        return satellites.get(satelliteName);
    }
    
    /**
     * remove satellite from known satellite list 
     * 
     * @param satelliteName satellite to remove
     */
    public void removeSatellite(String satelliteName) {
        // ... remove satellite from satellites
        satellites.remove(satelliteName);
    }
   
}
