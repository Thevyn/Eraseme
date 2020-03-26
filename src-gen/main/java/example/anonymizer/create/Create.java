// Generated with g9 DBmasker.

package example.anonymizer.create;

import no.esito.anonymizer.ITask;
import no.esito.anonymizer.core.AbstractTasks;

/**
 * Inserting new random data
 */
public class Create extends AbstractTasks {

	@Override
	public String getName() {
		return  "Create";
	}

	@Override
	public String getDescription() {
		return "Inserting new random data";
	}

	@Override
	public ITask[] getTasks() {
		return new ITask[] {
		    new Create_HOTELCHAIN(),
		    new Create_HOTEL(),
		    new Create_ROOM(),
		};
	}

}
