// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.ITask;
import no.esito.anonymizer.core.AbstractTasks;

/**
 * Using mappings and anonymizing primary keys
 */
public class Advanced extends AbstractTasks {

	@Override
	public String getName() {
		return  "Advanced";
	}

	@Override
	public String getDescription() {
		return "Using mappings and anonymizing primary keys";
	}

	@Override
	public ITask[] getTasks() {
		return new ITask[] {
		    new Anonymize_HOTEL(),
		    new Anonymize_ADDRESS(),
		    new Anonymize_ROOMCATEGORY_PK(),
		    new Anonymize_BOOKING(),
		    new Anonymize_ROOM(),
		    new Anonymize_HOTELROOMCATEGORY(),
		};
	}

}
