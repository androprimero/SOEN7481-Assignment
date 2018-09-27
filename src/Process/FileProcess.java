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
	 * increment the empty catch statistic
	 */
	public void incrementEmptyCatch() {
		stats.setCatch();
	}
	/*
	 * Restart the empty catch statistic
	 */
	public void resetEmptyCatch() {
		stats.setCatch(0);
	}
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
