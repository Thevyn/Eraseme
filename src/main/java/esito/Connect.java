// Generated with g9 DBmasker.

package esito;


import java.sql.Connection;
import java.net.InetAddress;

import no.esito.anonymizer.ConfigUtil;
import no.esito.anonymizer.core.AbstractConnect;
import org.apache.derby.drda.NetworkServerControl;

public class Connect extends AbstractConnect{

    /**
     * Factory method for Connection from the config.properties
     * @return Connection
     * @throws Throwable Connection exceptions
     */
    public static Connection createDefaultConnection() throws Throwable {
        return new Connect().makeConnection("",ConfigUtil.getConfig());
    }

    public NetworkServerControl nsc;

    @Override
    protected void checkNetworkService(String host, String port) throws Throwable {
        if (nsc == null) {
            InetAddress inet = InetAddress.getByName(host);
            nsc = new NetworkServerControl(inet, Integer.parseInt(port));
            nsc.start(null);
            System.out.println("Starting Derby: "+ port);
        }
    }
}