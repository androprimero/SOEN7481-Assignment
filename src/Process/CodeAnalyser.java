package Process;

import com.github.javaparser.ast.visitor.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
//import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.*;
public class CodeAnalyser extends VoidVisitorAdapter<Void> implements Runnable{
	private List<FileProcess> codes;
	private FileProcess codeInProcess;
	private Logger log;
	public CodeAnalyser(Logger loger) {
		codes = new ArrayList<FileProcess>();
		log = loger;
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
	/*@Override
	public void visit(CatchClause clause, Void arg) {
		this.codeInProcess.getStatistics().incrementNumberCatch();
		this.processCatch(clause);
		log.writeLog("Number Catch "+this.codeInProcess.getStatistics().getNumberCatch());
	}*/
	@Override
	public void visit(MethodDeclaration methodDecl, Void arg) {
		log.writeLog("\t Method checked: " + methodDecl.getNameAsString()+"\n");
		List<Node> childNodes = methodDecl.getChildNodes();
		for(Node child: childNodes) {
			log.writeLog("\t \t"+child.getClass().getName()+"\n");
		}
	}
	/*@Override
	public void visit(IfStmt ifarg, Void arg) {
		List<Node> childnodes = ifarg.getChildNodes();
		for(Node child:childnodes) {
			if(child.getClass().equals(BooleanLiteralExpr.class)) {
				BooleanLiteralExpr boolexpr = (BooleanLiteralExpr) child;
				System.out.println("I'm here " + boolexpr.getValue());
			}else {
				System.out.println("Child class "+child.getClass());
			}
		}
		System.out.println("if arg class "+ ifarg.getChildNodes());	
	}
	
	@Override
	public void visit(BooleanLiteralExpr booleanLiteral, Void arg) {
		Optional<Node> parent = booleanLiteral.getParentNode();
		if(parent.isPresent()) {
			if(parent.get().getClass().equals(IfStmt.class)) {
				System.out.println("I'm here");
			}
		}
	}
	@Override
	public void visit(BinaryExpr n, Void arg) {
		Optional<Node> parent = n.getParentNode();
		if(parent.isPresent()) {
			if(parent.get().getClass().equals(IfStmt.class)) {
				System.out.println("I'm here");
			}
		}
	}*/
	@Override
	public void run() {
		for(FileProcess code:codes) {
			log.writeLog("Code Checked : " + code.getNameFile()+"\n");
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
			log.writeLog("Warning: Empty Catch Exception Clause at " +clause.getBegin().get().line + clause.toString() + "\n");
		}
		if(isOverCatchException(clause)) {
			this.codeInProcess.getStatistics().incrementOverCatchException();
			log.writeLog("Warning: Over Catch Exception Clause at " +clause.getBegin().get().line + clause.toString() + "\n");
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
