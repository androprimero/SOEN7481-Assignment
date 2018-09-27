package UI;
import java.io.File;
import Process.Project;
/*
 * Class controller of the UI
 */
public class UIController {
	private File directoryProcess;
	private Project projectToProcess;
	/*
	 * Constructors
	 */
	public UIController(String path) {
		directoryProcess = new File(path);
		projectToProcess = new Project(directoryProcess);
	}
	public UIController(File file) {
		directoryProcess = file;
		projectToProcess = new Project(directoryProcess);
	}
	/*
	 * gets the number of files of a project
	 */
	public int getNumberOfCodes() {
		return projectToProcess.getNumberOfFiles();
	}
	/*
	 * gets the number of directories of a projects
	 */
	public int getNumberOfDirectories() {
		return projectToProcess.getNumberDirs();
	}
	/*
	 * send the message to start analyse the code
	 */
	public void startAnalysis() {
		projectToProcess.processFiles();
	}

}
