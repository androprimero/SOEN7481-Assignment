package Process;

import com.github.javaparser.ast.visitor.*;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.NodeList;
//import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.*;
public class CodeAnalyser extends VoidVisitorAdapter<Void> implements Runnable{
	private List<FileProcess> codes;
	private FileProcess codeInProcess;
	public CodeAnalyser() {
		codes = new ArrayList<FileProcess>();
	}
	public void addCodeToProcess(FileProcess file) {
		codes.add(file);
	}
	/*
	 * Sets the list of codes to process
	 */
	public void setListCodes(List<FileProcess> codes) {
		this.codes = codes;
	}
	@Override
	public void visit(CatchClause clause, Void arg) {
		this.codeInProcess.getStatistics().incrementNumberCatch();
		this.processCatch(clause);
		System.out.println("Number Catch "+this.codeInProcess.getStatistics().getNumberCatch());
	}
	@Override
	public void run() {
		for(FileProcess code:codes) {
			this.codeInProcess = code; 
			this.visit(code.getUnit(),null);
		}
	}
	/*
	 * Private methods
	 */
	/*
	 * Look for different catch issues
	 */
	private void processCatch(CatchClause clause) {
		if(isEmptyStatement(clause.getBody())) {
			this.codeInProcess.getStatistics().incrementEmptyCatch();
		}
		if(isOverCatchException(clause)) {
			this.codeInProcess.getStatistics().incrementOverCatchException();
		}
	}
	/*
	 * Find if the block statement is empty
	 */
	private boolean isEmptyStatement(BlockStmt statement) {
		boolean isEmpty = false;
		NodeList<Statement> childs = statement.getStatements();
		if(childs.isEmpty()) {
			isEmpty = false;
		}
		return isEmpty;
	}
	/*
	 * Check if the exception caught is a base Exception or not
	 */
	private boolean isOverCatchException(CatchClause clause) {
		boolean isOverCatch = false;
		String parameter = clause.getParameter().getTypeAsString();
		if(parameter.equals("Exception") || parameter.equals("Throwable")) {
			isOverCatch = true;
		}
		return isOverCatch;
	}
}
