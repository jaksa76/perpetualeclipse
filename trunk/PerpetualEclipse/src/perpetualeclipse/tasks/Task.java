package perpetualeclipse.tasks;

import perpetualeclipse.report.Report;

/**
 * Any operation performed in a build should be a task. Examples of tasks are
 * compiling a project, running tests etc.
 * 
 * @author Jaksa
 */
public interface Task {
	Report execute() throws Exception;
}
