package Process;

import com.github.javaparser.ast.visitor.*;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;


public class CodeAnalyser extends VoidVisitorAdapter<Void> implements Runnable{
	private static final int MAX_PUBLIC_METHODS = 1000;
	private List<FileProcess> codes;
	private FileProcess codeInProcess;
	private Logger log;
	private boolean equalsMethod,hashCodeMethod;
	private CombinedTypeSolver typeSolver;
	private JavaParserFacade facade;
	private List<String> varUsed; // variables used (something assign to them) in the method
	private List<CodeElement> privateMethods;
	private List<CodeElement> publicMethods;
	private List<CodeElement> CallMethods;
	private List<VariableDeclarator> varDeclared; // Variables Declared in the method
	private List<Parameter> parameters;
	private List<VariableDeclarator> classVariables; // have the variables of the class
	public CodeAnalyser(Logger loger,List<CodeElement> publicMethodsList) {
		codes 			= new ArrayList<FileProcess>();
		varUsed 		= new ArrayList<String>();
		privateMethods 	= new ArrayList<CodeElement>();
		varDeclared 	= new ArrayList<>();
		parameters 		= new ArrayList<>();
		publicMethods 	= publicMethodsList;
		classVariables 	= new ArrayList<>();
		CallMethods 	= new ArrayList<>();
		log 			= loger;
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
	
	// Chandrakanth
	
	public boolean processTry(TryStmt node) {
		ArrayList<String> printStatements = new ArrayList<String>();
		NodeList<CatchClause> catchClauses = node.getCatchClauses();
		for(CatchClause catchClause: catchClauses) {
			CatchClause tmpCatch = catchClause;
			BlockStmt block = tmpCatch.getBody();
			NodeList<Statement> statements = block.getStatements();
			for(Statement statement: statements) {
				for(String s: printStatements) {
					if(s.equals(statement.toString())) {
						System.out.println("Inadequate logging information in catch blocks found: ");
						System.out.println("Multiple catch blocks with statement: " + s);
						return false;
					}
				}
				printStatements.add(statement.toString());
			}
		}
		return true;
	}
	
	// Chandrakanth Ends
	
	@Override
	public void visit(VariableDeclarator n, Void arg) {
		classVariables.add(n);
	}
	@Override
	public void visit(MethodDeclaration methodDecl, Void arg) {
		/*
		 * Search inside the method code for different bugs
		 */
		varUsed.clear();// clear the variables when the method changes
		CallMethods.clear();// clear the methods called inside the method
		parameters.clear(); // clear the parameters
		log.writeLog("\tMethod checked: " + methodDecl.getNameAsString()+"\n"); // writes the log to keep track of methods analyzed
		this.processMethodDeclaration(methodDecl);
		NodeList<Statement> statements = methodDecl.getBody().get().getStatements();
		for(Statement stmt: statements) {
			this.processTypeExpr(stmt);
		}
		for(CodeElement element:CallMethods) {
			int pos = this.isMethodAlreadyListed(element.getName());
			if(pos > -1) {
				privateMethods.get(pos).setUsed(true);
			}
		}
		for(CodeElement method:privateMethods) {
			if(!method.getUsed()) {
				this.codeInProcess.getStatistics().incrementMethodNotUsed();
				log.writeWarning("Method is not used ", method);
			}
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
			this.checkUsedMethod();
			log.writeLog(code.getStatistics().toString());
		}
		
	}
	/*
	 * Sets The equals and hashcode flags to false
	 */
	public void cleanFlags() {
		this.equalsMethod = false;
		this.hashCodeMethod = false;
		this.privateMethods.clear();
		this.classVariables.clear();
		this.privateMethods.clear();
	}
	public void checkFlags() {
		if(this.equalsMethod && !this.hashCodeMethod) {
			this.log.writeLog( "Warning: Overrides equals but no hashCode \n"+codeInProcess.getNameFile()+"\n");
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
				
				// Chandrakanth
				processTry(tryStm);
				// Chandrakanth ends
				
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
			// other expressions as assignments are taken in account here (there are no bugs related directly, but some information is taken)
			ExpressionStmt stmt = (ExpressionStmt) node;
			//System.out.println(stmt.getExpression().toString());
			Expression expr = stmt.getExpression();
			if((expr instanceof AssignExpr)) {
				// takes the assignment of variables and add the name to the list
				this.varUsed.add(((AssignExpr) expr).getTarget().toString());
			}else if(expr instanceof MethodCallExpr) {
				// Checks the use of a method
				String method = ((MethodCallExpr)expr).toString();
				// check the variable name to find the type
				boolean isClass = false,isLocal = false;
				String varName = new String();
				if(method.contains(".")) {// get the name of the variable to find the class
					varName = method.substring(0, method.indexOf("."));
				}else {
					isClass= true; // method call without a variable name means that is from the class
				}
				String type = codeInProcess.getNameFile().substring(0,codeInProcess.getNameFile().indexOf("."));
				if(!isClass) {
					for(VariableDeclarator variable: classVariables) {
						if(variable.getNameAsString().equals(varName)) {
							isClass = true;
							type = variable.getTypeAsString();
						}
					}
				}
				if(!isClass) {
					for(VariableDeclarator variable:varDeclared) {
						if(variable.getNameAsString().equals(varName)) {
							isLocal = true;
							type = variable.getTypeAsString();
						}
					}
				}
				if(!isClass && !isLocal) {
					for(Parameter parameter:parameters) {
						if(parameter.getNameAsString().equals(varName)) {
							type = parameter.getTypeAsString();
						}
					}
				}
				CallMethods.add(new CodeElement(((MethodCallExpr)expr).getNameAsString(),type));
			}
		}else if(node instanceof FieldDeclaration){
			varDeclared.addAll(((FieldDeclaration) node).getVariables());
		}else{	
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
			this.codeInProcess.getStatistics().incrementConditionHasNoEffect();
			log.writeWarning("Condition has No effect ", ifStmt);
		} else if(condition instanceof BinaryExpr){
			BinaryExpr expr = (BinaryExpr) condition;
			BinaryExpr.Operator oper = expr.getOperator();
			Expression left = expr.getLeft();
			if(left instanceof BinaryExpr) {
				processCondition(left, ifStmt);
			}else {
				typeProcess(left,oper,ifStmt);
			}
			Expression right = expr.getRight();
			if(right instanceof BinaryExpr) {
				processCondition(right, ifStmt);
			}else {
				typeProcess(right,oper,ifStmt);
			}
		} else if(condition instanceof NameExpr) {
			if(!varUsed.contains(condition.toString())) {
				this.codeInProcess.getStatistics().incrementConditionHasNoEffect();
				log.writeWarning("Condition has no effect ", ifStmt);
			}
			//System.out.println("Name Expression to locate");
		}
	}
	private void typeProcess(Expression expr, BinaryExpr.Operator oper,Statement parentExpression) {
		ResolvedType type = this.solveType(expr);
		if(type != null) {
			if(type.describe().equals("java.lang.String")) {
				if(oper.equals(BinaryExpr.Operator.EQUALS)) {
					this.codeInProcess.getStatistics().incrementStringComparison();
					log.writeWarning("Comparison of String objects using == ", parentExpression);
				}else {
					if(oper.equals(BinaryExpr.Operator.NOT_EQUALS)) {
						this.codeInProcess.getStatistics().incrementStringComparison();
						log.writeWarning("Comparison of String objects using != ", parentExpression);
					}
				}
			}
			//System.out.println("condition code "+ expr.toString());
			//System.out.println("condition type "+type.describe());
		}
	}
	/**
	 * Process the method declarations
	 * @param methodDecl
	 */
	private void processMethodDeclaration(MethodDeclaration methodDecl) {
		EnumSet<com.github.javaparser.ast.Modifier> modifiers = methodDecl.getModifiers();
		Modifier modifierMethod = Modifier.DEFAULT;
		if(modifiers.iterator().hasNext()) {
			modifierMethod = modifiers.iterator().next();
		}
		parameters = methodDecl.getParameters();
		boolean isPrivate;
		if(modifierMethod.equals(com.github.javaparser.ast.Modifier.PRIVATE)) {
			isPrivate = true;
		}else {
			isPrivate = false;
			if(modifierMethod.equals(Modifier.PUBLIC)) {
				this.writePublicMethod(new CodeElement(methodDecl.getNameAsString(),codeInProcess.getNameFile().substring(0,codeInProcess.getNameFile().indexOf("."))));
			}
		}
		if(methodDecl.getNameAsString().equals("equals")) {
			this.equalsMethod = true;
		}
		if(methodDecl.getNameAsString().equals("hashCode")) {
			this.hashCodeMethod = true;
		}
		if(privateMethods.size() == 0) {
			if(isPrivate) {
				privateMethods.add(new CodeElement(methodDecl.getNameAsString(),codeInProcess.getNameFile())); // first method to process
			}
		}else {
			if(isPrivate) {
				String name = methodDecl.getName().asString();
				if(this.isMethodAlreadyListed(name) < 0) {
					privateMethods.add(new CodeElement(methodDecl.getName().asString(),codeInProcess.getNameFile())); // first method to process
				}
			}
		}
	}
	private int isMethodAlreadyListed(String methodName) {
		for(CodeElement element:privateMethods) {
			if(element.getName().equals(methodName)) {
				return privateMethods.indexOf(element);
			}
		}
		return -1;
	}
	private void writePublicMethod(CodeElement publicMethod) {
		try {
		synchronized(publicMethods) {
			while(publicMethods.size() > MAX_PUBLIC_METHODS) {
				publicMethods.wait();
			}
			publicMethods.add(publicMethod);
			publicMethods.notifyAll();
		}
		}catch(InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	private void checkUsedMethod() {
			synchronized(publicMethods) {
			for(CodeElement element:publicMethods) {
				for(CodeElement calledElement:CallMethods) {
					if(element.equals(calledElement)) {
						publicMethods.remove(element);
						publicMethods.notifyAll();
					}
				}
			}
		}
	}
}
