// Generated with g9 DBmasker.

package example.anonymizer;

import example.anonymizer.advanced.Advanced;
import example.anonymizer.anonymize.Anonymize;
import example.anonymizer.create.Create;
import example.anonymizer.forgetme.ForgetMe;
import example.anonymizer.subjectaccess.SubjectAccess;

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
		    new Anonymize(),
		    new Create(),
		    new Advanced(),
		    new ForgetMe(),
		    new SubjectAccess(),
		};
	}

}
