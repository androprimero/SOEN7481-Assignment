package UI;
import java.io.File;
import Process.Project;
public class UIController {
	private File directoryProcess;
	private Project projectToProcess; 
	public UIController(String path) {
		directoryProcess = new File(path);
		projectToProcess = new Project(directoryProcess);
	}
	public UIController(File file) {
		directoryProcess = file;
		projectToProcess = new Project(directoryProcess);
	}
	public int getNumberofCodes() {
		return projectToProcess.getNumberOfFiles();
	}

}
