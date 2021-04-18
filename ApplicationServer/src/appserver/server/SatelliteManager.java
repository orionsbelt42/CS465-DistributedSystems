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
        // ...
        satellites = new Hashtable<>();
    }

    public void registerSatellite(ConnectivityInfo satelliteInfo) {
        // ...
        satellites.put(satelliteInfo.getName(), satelliteInfo);
        // LoadManager.satellites.add(satelliteInfo.getName());
    }

    public ConnectivityInfo getSatelliteForName(String satelliteName) {
        // ..
        return satellites.get(satelliteName);
    }
    
    public void removeSatellite(String satelliteName) {
        // ...
        satellites.remove(satelliteName);
        // LoadManager.satellites.remove(satelliteName);
    }
   
}
