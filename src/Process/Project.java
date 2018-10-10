package Process;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Project {
	private List <FileProcess> codes;
	private List<Thread> analysers;
	private int numberDirs;
	private File directory;
	private Logger logFile;
	public static String extension = ".java";
	private int filesToProcess = 10; // number of files to process per thread
	private int maxThreadsActive = 10; // number max of active threads at a moment
	/*
	 * Constructors
	 */
	public Project(String path,Logger log) {
		directory = new File(path);
		initializeComponents(log);
		exploreProject(directory);
	}
	public Project(File directory,Logger log) {
		this.directory = directory;
		initializeComponents(log);
		exploreProject(directory);
	}
	/*
	 *  Initialize all the components 
	 */
	private void initializeComponents(Logger log) {
		analysers = new ArrayList<Thread>();
		codes = new ArrayList<FileProcess>();
		numberDirs = 0;
		logFile = log;
	}
	/*
	 * Public methods
	 */
	/*
	 * Add a file to the list of files to process
	 */
	public void addFileToProject(File file) {
		try {
			FileProcess fileToAdd = new FileProcess(file);
			codes.add(fileToAdd);
		}catch(FileNotFoundException ex) {
			System.out.println("File not found in the project: "+file.getPath());
		}
	}
	/*
	 * gets the numbers of files that contains a project
	 */
	public int getNumberOfFiles() {
		return codes.size();
	}
	/*
	 * gets the number of directories of the projects
	 */
	public int getNumberDirs() {
		return numberDirs;
	}
	/*
	 * Clear the project of set
	 */
	public void clearProject() {
		codes.clear();
		numberDirs = 0;
		directory = null;
	}
	/*
	 * set a new directory 
	 */
	public void setProject(String path) {
		directory = new File(path);
		exploreProject(directory);
	}
	/*
	 * set a new directory
	 */
	public void setProject(File directory) {
		this.directory = directory;
		exploreProject(directory);
	}
	/*
	 * Process the files in blocks of filesToProcess * maxThreadsActive
	 */
	public void processFiles() {
		int act = 0;
		int end = 0;
		logFile.writeLog("Process starts\n");
		while(act < codes.size()) {
			if(activeProcess() < maxThreadsActive) {
				CodeAnalyser analyser = new CodeAnalyser(logFile);
				if((act + filesToProcess) < codes.size()){
					end = act + filesToProcess;
				}else
				{
					end = codes.size();
				}
				analyser.setListCodes(codes.subList(act,end));
				Thread process = new Thread(analyser);
				analysers.add(process);
				process.start();
				act += filesToProcess;
			}
		}
		while(activeProcess() > 0) {
		}
		logFile.writeLog("End Process\n");
	}
	/*
	 * Private Methods
	 */
	/*
	 * Explore the sub-directories and find the files with a given extension in this case java
	 */
	private void exploreProject(File directory) {
		if(directory.isDirectory()) {
			numberDirs++;
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
	/*
	 * Checks the number of threads that are active
	 */
	public int activeProcess() {
		int active = 0;
		if(analysers.size() > 0) {
			for(Thread analyser:analysers) {
				if(analyser.isAlive()) {
				active++;
				}
			}
		}
		return active;
	}
}
