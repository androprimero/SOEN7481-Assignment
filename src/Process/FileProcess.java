package Process;
import java.io.File;
import java.io.FileNotFoundException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;

public class FileProcess {
	private File code;
	private Statistics stats;
	private CompilationUnit unitToCompile;
	/*
	 * Create a File to process with a give file, statistics start in 0
	 */
	public FileProcess(File code) throws FileNotFoundException {
		this.code = code;
		this.ParseCode();
		stats = new Statistics();
	}
	/*
	 * 
	 */
	public Statistics getStatistics() {
		return this.stats;
	}
	/*
	 * Gets the compilation unit of the code
	 */
	public CompilationUnit getUnit() {
		return this.unitToCompile;
	}
	/*
	 * Parse the file to generate the compilation unit
	 */
	private void ParseCode() throws FileNotFoundException{
		unitToCompile = JavaParser.parse(code);
	}
}
