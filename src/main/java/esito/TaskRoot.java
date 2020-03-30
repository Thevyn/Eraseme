// Generated with g9 DBmasker.

package esito;


import forgetme.ForgetMe;
import no.esito.anonymizer.ITask;
import no.esito.anonymizer.core.AbstractTasks;

public class TaskRoot extends AbstractTasks {

	@Override
	public String getName() {
		return  "Tasks";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public ITask[] getTasks() {
		return new ITask[] {
		    new ForgetMe(),
		};
	}

}
