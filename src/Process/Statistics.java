package Process;

public class Statistics {
	private int emptyCatch;
	private int numberCatch;
	private int overCatchExeption;
	private int methodNotUsed;
	private int conditionHasNoEffect;
	private int stringComparision;

	public Statistics() {
		emptyCatch = 0;
		numberCatch = 0;
		overCatchExeption = 0;
		methodNotUsed = 0;
		conditionHasNoEffect = 0;
		stringComparision = 0;
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
	public void incrementMethodNotUsed() {
		this.methodNotUsed++;
	}
	public void incrementConditionHasNoEffect() {
		this.conditionHasNoEffect++;
	}
	public void incrementStringComparison() {
		this.stringComparision++;
	}
	/*
	 * Set the number of Catch clauses
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
	public void setMethodNotUsed(int newMethodNotUsed) {
		this.methodNotUsed = newMethodNotUsed;
	}
	public void setConditionHasNoEffect(int newConditionHasNoEffect) {
		this.conditionHasNoEffect = newConditionHasNoEffect;
	}
	public void setStringComparision(int newStringComparision) {
		this.stringComparision = newStringComparision;
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
	public int getMethodNotUsed() {
		return this.methodNotUsed;
	}
	public int getConditionHasNoEffect() {
		return this.conditionHasNoEffect;
	}
	public int getStringComparision() {
		return this.stringComparision;
	}
	public String toString() {
		String string = new String();
		string = "----------------------------\n";
		string += "Statistics \n";
		string += "Number Catch clauses : " + String.valueOf(this.numberCatch) + "\n";
		string += "Number Catch clauses empty: " + String.valueOf(this.emptyCatch) + "\n";
		string += "Number Catch clauses overCatch: " + String.valueOf(this.overCatchExeption) + "\n";
		string += "Number of condition with no effect: " + String.valueOf(this.conditionHasNoEffect) + "\n";
		string += "Methods not used " + String.valueOf(this.methodNotUsed) + "\n";
		string +="----------------------------\n";
		return string;
	}
}
