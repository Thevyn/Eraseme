// Generated with g9 DBmasker.

package example.anonymizer.subjectaccess;

import no.esito.anonymizer.ITask;
import no.esito.anonymizer.core.AbstractTasks;

/**
 * Subject Access Request - based on customer number
 */
public class SubjectAccess extends AbstractTasks {

	@Override
	public String getName() {
		return  "SubjectAccess";
	}

	@Override
	public String getDescription() {
		return "Subject Access Request - based on customer number";
	}

	@Override
	public ITask[] getTasks() {
		return new ITask[] {
		    new SAR_CUSTOMER(),
		};
	}

}
