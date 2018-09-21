package Process;

public class Statistics {
	private int emptyCatch;
	public Statistics() {
		emptyCatch = 0;
	}
	public int getEmptyCatch() {
		return emptyCatch;
	}
	/*
	 *  there is no setting in this method, its an increment
	 */
	public void setCatch() {
		this.emptyCatch++;
	}
	/*
	 * the assignment of a new value to the empty catch
	 */
	public void setCatch(int newEmptyCatch) {
		this.emptyCatch = newEmptyCatch;
	}

}
