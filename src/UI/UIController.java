package UI;
import java.io.File;

import Process.Logger;
import Process.Project;
/*
 * Class controller of the UI
 */
public class UIController {
	private File directoryProcess;
	private Project projectToProcess;
	private Logger logger;
	/*
	 * Constructors
	 */
	public UIController(String path,Logger loger) {
		directoryProcess = new File(path);
		projectToProcess = new Project(directoryProcess,loger);
		logger = loger;
	}
	public UIController(File file,Logger loger) {
		directoryProcess = file;
		projectToProcess = new Project(directoryProcess,loger);
		logger = loger;
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
	/*
	 * Gets the status of every
	 */
	
}
