// Generated with g9 DBmasker.

package example.anonymizer.forgetme;

import no.esito.anonymizer.ITask;
import no.esito.anonymizer.core.AbstractTasks;

/**
 * Forget Me - based on customer number
 */
public class ForgetMe extends AbstractTasks {

	@Override
	public String getName() {
		return  "ForgetMe";
	}

	@Override
	public String getDescription() {
		return "Forget Me - based on customer number";
	}

	@Override
	public ITask[] getTasks() {
		return new ITask[] {
		    new Erase_CUSTOMER(),
		};
	}

}
