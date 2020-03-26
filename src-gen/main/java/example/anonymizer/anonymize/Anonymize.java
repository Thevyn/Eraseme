// Generated with g9 DBmasker.

package example.anonymizer.anonymize;

import no.esito.anonymizer.ITask;
import no.esito.anonymizer.core.AbstractTasks;

/**
 * Pure Anonymizations
 */
public class Anonymize extends AbstractTasks {

	@Override
	public String getName() {
		return  "Anonymize";
	}

	@Override
	public String getDescription() {
		return "Pure Anonymizations";
	}

	@Override
	public ITask[] getTasks() {
		return new ITask[] {
		    new Anonymize_CUSTOMER(),
		    new Anonymize_ROOMCATEGORY(),
		    new Delete_HOTELCHAIN(),
		    new Fix_ADDRESS(),
		};
	}

}
