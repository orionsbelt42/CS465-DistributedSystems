package appserver.server;

import java.util.ArrayList;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class LoadManager {
    
    static ArrayList satellites = null; // list of connected satellites
    static int nextSatelliteIndex = 0; // index of next satellite to visit

    // default constructor
    public LoadManager() {
        // allocate memory for new satellite array
        satellites = new ArrayList<String>();
    }

    /**
     * Adds a satellite name to the satellite list
     * 
     * @param satelliteName valid satellite name to add
     */
    public void satelliteAdded(String satelliteName) {
        // add satellite
        satellites.add(satelliteName);
    }

    /** 
     * Gets next available satellite using a round robin strategy
     * 
     * @return a satellite name
     * @throws Exception if there are no connected satellites
     */
    public String nextSatellite() throws Exception {
        // number of known satellites
        int numberSatellites;
        
        // name of the satellite who is supposed to take the job...
        String sat;
        
        // get number of connected satellites
        numberSatellites = satellites.size();
        
        // make sure satellites exist or throw exception
        if ( numberSatellites == 0 ) {
            throw new Exception("UnknownSatelliteException: No Satellites Registered");
        }
        
        // implement policy that returns the satellite name according to a round robin methodology
        nextSatelliteIndex = nextSatelliteIndex % numberSatellites;

        // convert satellite name to string because satellites.get(index) 
        // evaluates to Object instead of String
        sat = String.valueOf(satellites.get(nextSatelliteIndex));
        
        // advance index to next satellite location (for next call)
        nextSatelliteIndex++;
        
        // return the result
        return sat;
    }
}
