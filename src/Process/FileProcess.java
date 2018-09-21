package Process;
import java.io.File;
public class FileProcess {
	private File code;
	private Statistics stats;
	/*
	 * Create a File to process with a give file, statistics start in 0
	 */
	public FileProcess(File code) {
		this.code = code;
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
}
