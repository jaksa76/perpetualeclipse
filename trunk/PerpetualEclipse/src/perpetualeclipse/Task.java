package perpetualeclipse;

import perpetualeclipse.report.Report;

public interface Task {
	Report execute() throws Exception;
}
