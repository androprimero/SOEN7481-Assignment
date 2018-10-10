package Process;

public class Statistics {
	private int emptyCatch;
	private int numberCatch;
	private int overCatchExeption;
	public Statistics() {
		emptyCatch = 0;
		numberCatch = 0;
		overCatchExeption = 0;
	}
	
	/*
	 * increase the number of catch clauses
	 */
	public void incrementNumberCatch() {
		this.numberCatch ++;
	}
	public void incrementOverCatchException() {
		this.overCatchExeption++;
	}
	public void incrementEmptyCatch() {
		this.emptyCatch++;
	}
	/*
	 * Set the number of Cacth clauses
	 */
	public void setNumberCatch(int newNumberCatch) {
		this.numberCatch = newNumberCatch;
	}
	public void setEmptyCatch(int newEmptyCatch) {
		this.emptyCatch = newEmptyCatch;
	}
	public void setOverCatchException(int newOverCatchException) {
		this.overCatchExeption = newOverCatchException;
	}

	/*
	 * Get the number of catch clauses
	 */
	public int getNumberCatch() {
		return this.numberCatch;
	}
	public int getEmptyCatch() {
		return this.emptyCatch;
	}
	public int getOverCatchException() {
		return this.overCatchExeption;
	}

}
