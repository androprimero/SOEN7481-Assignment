package Process;

import com.github.javaparser.ast.visitor.*;

import java.util.ArrayList;
import java.util.List;

//import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.*;
public class CodeAnalyser extends VoidVisitorAdapter<Void> implements Runnable{
	private List<FileProcess> codes;
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
		System.out.println("Visiting catch clause");
	}
	@Override
	public void run() {
		for(FileProcess code:codes) {
			this.visit(code.getUnit(),null);
		}
	}
}
