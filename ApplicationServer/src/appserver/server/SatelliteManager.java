package appserver.server;

import appserver.comm.ConnectivityInfo;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class SatelliteManager {

    // (the one) hash table that contains the connectivity information of all satellite servers
    static private Hashtable<String, ConnectivityInfo> satellites = null;

    public SatelliteManager() {
        // initializes the Load Manager's 'satellites' hash table
        satellites = new Hashtable<>();
    }

    public void registerSatellite(ConnectivityInfo satelliteInfo) {
        // enters satellite into hash table, name first, then all other info
        satellites.put(satelliteInfo.getName(), satelliteInfo);
    }

    public ConnectivityInfo getSatelliteForName(String satelliteName) {
        // returns satellite from hash table using only its name
        return satellites.get(satelliteName);
    }
    
    public void removeSatellite(String satelliteName) {
        // satellite removed from LoadManager hash table
        satellites.remove(satelliteName);
    }
   
}
