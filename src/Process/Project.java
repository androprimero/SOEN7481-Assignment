package Process;
import java.util.*;
import java.io.File;

public class Project {
	List <FileProcess> codes;
	File directory;
	public static String extension = ".java";
	/*
	 * Constructors
	 */
	public Project(String path) {
		codes = new ArrayList<FileProcess>();
		directory = new File(path);
		exploreProject(directory);
	}
	public Project(File directory) {
		codes = new ArrayList<FileProcess>();
		this.directory = directory;
		exploreProject(directory);
	}
	/*
	 * Public methods
	 */
	/*
	 * Add a file to the list of files to process
	 */
	public void addFileToProject(File file) {
		FileProcess fileToAdd = new FileProcess(file);
		codes.add(fileToAdd);
	}
	/*
	 * gets the numbers of files that contains a project
	 */
	public int getNumberOfFiles() {
		return codes.size();
	}
	/*
	 * Private Methods
	 */
	/*
	 * Explore the sub-directories and find the files with a given extension in this case java
	 */
	private void exploreProject(File directory) {
		if(directory.isDirectory()) {
			for(File child:directory.listFiles()) {
				exploreProject(child);
			}
		}else {
			if(directory.isFile()) {
				if(directory.getPath().endsWith(extension)) {
					this.addFileToProject(directory);
				}
			}
		}
	}

}
