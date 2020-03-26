// Generated with g9 DBmasker.

package example.anonymizer;

import java.sql.Connection;

import no.esito.anonymizer.ITasks;
import no.esito.anonymizer.Log;
import no.esito.anonymizer.core.AbstractAnonymizer;
import no.esito.anonymizer.core.ConsoleCommandHandler;
import no.esito.anonymizer.core.ConsoleContext;
import no.esito.anonymizer.core.ConsoleContext.IConnectHandler;

/**
 * Anonymizer - entry point for running JAR on the command line.
 *
 * For running Anonymizer from other Java programs using APIs:
 * <ul>
 * <li> Use Connect.createDefaultConnection() for connection from config.properties
 * <li> Use ContextFactory methods for creating a Context
 * </ul>
 *
 * Example running a task called MyTask: <code> new MyTask().run(context);</code>
 */
public class Anonymizer extends AbstractAnonymizer {

    @Override
    public ITasks getTaskRoot() {
        return new TaskRoot();
    }

    public static void main(String[] args) {
        Log.configureConsoleLoghandler();
        Anonymizer anonymizer = new Anonymizer();
        try {
        	IConnectHandler handler=new IConnectHandler() {

        		@Override
        		public Connection getConnection() throws Throwable {
        			return Connect.createDefaultConnection();
        		}
        	};
            ConsoleContext context = new ConsoleContext(handler);
            new ConsoleCommandHandler(anonymizer).run(context,args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getEmptyDbTables() {
        return "ADDRESS,BOOKING,HOTELROOMCATEGORY,ROOM,HOTEL,ROOMCATEGORY,CUSTOMER,HOTELCHAIN";
    }
}
