package Process;

import com.github.javaparser.ast.visitor.*;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import sun.invoke.util.BytecodeName;

public class CodeAnalyser extends VoidVisitorAdapter<Void> implements Runnable{
	private List<FileProcess> codes;
	private FileProcess codeInProcess;
	private Logger log;
	private boolean equalsMethod,hashCodeMethod;
	private CombinedTypeSolver typeSolver;
	private JavaParserFacade facade;
	public CodeAnalyser(Logger loger) {
		codes = new ArrayList<FileProcess>();
		log = loger;
	}
	public void addCodeToProcess(FileProcess file) {
		codes.add(file);
	}
	/*
	 * Set up the type solver
	 */
	public void setUpTypeSolver(File directory) {
		typeSolver = new CombinedTypeSolver();
		typeSolver.add(new JavaParserTypeSolver(directory));
		typeSolver.add(new ReflectionTypeSolver());
		facade = JavaParserFacade.get(typeSolver);
	}
	/*
	 * Sets the list of codes to process
	 */
	public void setListCodes(List<FileProcess> codes) {
		this.codes = codes;
	}
	@Override
	public void visit(MethodDeclaration methodDecl, Void arg) {
		log.writeLog("\tMethod checked: " + methodDecl.getNameAsString()+"\n");
		if(methodDecl.getNameAsString().equals("equals")) {
			this.equalsMethod = true;
		}
		if(methodDecl.getNameAsString().equals("hashCode")) {
			this.hashCodeMethod = true;
		}
		NodeList<Statement> statements = methodDecl.getBody().get().getStatements();
		for(Statement stmt: statements) {
			this.processTypeExpr(stmt);
		}
	}
	@Override
	public void run() {
		for(FileProcess code:codes) {
			this.cleanFlags();
			log.writeLog("Code Checked : " + code.getNameFile()+"\n");
			this.codeInProcess = code; 
			this.visit(code.getUnit(),null);
			this.checkFlags();
		}
		
	}
	/*
	 * Sets The equals and hashcode flags to false
	 */
	public void cleanFlags() {
		this.equalsMethod = false;
		this.hashCodeMethod = false;
	}
	public void checkFlags() {
		if(this.equalsMethod && !this.hashCodeMethod) {
			this.log.writeLog(codeInProcess.getNameFile() + " Overrides equals but no hashCode");
		}
	}
	/*
	 * Private methods
	 */
	/*
	 * find the type of expression to process
	 */
	private void processTypeExpr(Node node) {
		if(node.getClass().equals(BlockStmt.class)) {
			// BlockStmt is composed by other statements that are processed in this method
			BlockStmt block = (BlockStmt) node;
			for(Statement child: block.getStatements() ) {
				processTypeExpr(child);
			}
		}else if(node.getClass().equals(TryStmt.class)) {
			// Catch clauses are child of try statements then they are treated here
				TryStmt tryStm = (TryStmt) node;
				BlockStmt block = tryStm.getTryBlock();
				for(Statement child: block.getStatements() ) {
					processTypeExpr(child);
				}
				NodeList<CatchClause> catchClauses = tryStm.getCatchClauses();
				for(CatchClause catchClause: catchClauses) {
					processCatch(catchClause);
				}
		}else if(node.getClass().equals(IfStmt.class)) {
			// if statements are processed here
			IfStmt ifstm = (IfStmt) node;
			processIfStmt(ifstm);
		}else if(node.getClass().equals(ExpressionStmt.class)){
			ExpressionStmt stmt = (ExpressionStmt) node;
			//System.out.println(stmt.getExpression().toString());
			Expression expr = stmt.getExpression();
			ResolvedType type = this.solveType(expr);
			if(type != null) {
				System.out.println("Expression code "+ expr.toString());
				System.out.println("Expression type "+type.describe());
			}
		}else {	
			log.writeLog(node.getClass() + " code : "+ node.toString()+"\n");
		}
	}
	/*
	 * Look for different catch issues
	 */
	private void processCatch(CatchClause clause) {
		if(isEmptyStatement(clause.getBody())) {
			this.codeInProcess.getStatistics().incrementEmptyCatch();
			log.writeWarning("Empty Catch Exception Clause at ", clause);
		}
		if(isOverCatchException(clause)) {
			this.codeInProcess.getStatistics().incrementOverCatchException();
			log.writeWarning("Over Catch Exception Clause at ", clause);
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
	/*
	 * Resolves the type of a node
	 */
	private ResolvedType solveType(Node node) {
		try {
			ResolvedType type = facade.getType(node);
			return type;
		}catch(UnsolvedSymbolException ex) {
			return null;
		}catch(UnsupportedOperationException ex) {
			return null;
		}catch(Throwable ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	/*private void stringChecker(String className,Node codeNode) {
		if(className.equals("String")) {
			System.out.println(codeNode.toString());
		}else {
			System.out.println(codeNode.toString() + "is not a string");
		}
	}*/
	private void processIfStmt(IfStmt ifStmt) {
		Expression condition = ifStmt.getCondition();
		List<Node> statements = ifStmt.getChildNodes();
		processCondition(condition,ifStmt);
		for(Node statement:statements) {
			processTypeExpr(statement);
		}
	}
	private void processCondition(Expression condition, IfStmt ifStmt) {
		if(condition instanceof BooleanLiteralExpr) {
			log.writeWarning("Condition has No effect", ifStmt);
		} else if(condition instanceof BinaryExpr){
			BinaryExpr expr = (BinaryExpr) condition;
			BinaryExpr.Operator oper = expr.getOperator();
			Expression left = expr.getLeft();
			if(left instanceof BinaryExpr) {
				processCondition(left, ifStmt);
			}
			Expression right = expr.getRight();
			if(right instanceof BinaryExpr) {
				processCondition(right, ifStmt);
			}
			
		} else if(condition instanceof NameExpr) {
			ResolvedType type = this.solveType(condition);
			if(type != null) {
				System.out.println("condition code "+ condition.toString());
				System.out.println("condition type "+type.describe());
			}
			//System.out.println("Name Expression to locate");
		}
	}
	private void typeProcess(Expression expr, BinaryExpr.Operator oper) {
		ResolvedType type = this.solveType(expr);
		if(type != null) {
			if(type.describe().equals("String")) {
				if(oper.equals(BinaryExpr.Operator.EQUALS)) {
					
				}
			}
			System.out.println("condition code "+ expr.toString());
			System.out.println("condition type "+type.describe());
		}
	}
}
