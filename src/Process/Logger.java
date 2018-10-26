package Process;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.github.javaparser.ast.Node;
public class Logger {
	private File logFile; 
	private BufferedWriter buffer;
	
	public Logger(String path)throws IOException {
		logFile = new File(path);
		this.startBuffers(logFile.getName());		
	}
	public Logger(File logFile) throws IOException {
		this.logFile = logFile;
		this.startBuffers(logFile.getName());
	}
	private void startBuffers(String name) throws IOException {
		this.buffer = new BufferedWriter(new FileWriter(name));
	}
	public void writeLog(String message){
		try {
			this.buffer.append(message);
		}catch(IOException ex) {
			System.out.println("Exeption generated writing "+ex.getMessage());
			System.out.println("Message attempting to write "+ message);
		}
	}
	public void writeWarning(String warningMessage,Node node) {
		try {
			this.buffer.append("Warning: "+ warningMessage+" at line: "+ node.getBegin().get().line + "\n"+node.toString()+"\n");
		}catch(IOException ex) {
			System.out.println("Exeption generated writing "+ex.getMessage());
			System.out.println("Message attempting to write "+ "Warning: "+ warningMessage+ node.getBegin().get().line + "\n"+
								node.toString()+"\n");
		}
	}
	public void writeWarning(String warningMessage,CodeElement element) {
		try {
			this.buffer.append("Warning: "+ warningMessage+" "+element.getName()+"\n");
		}catch(IOException ex) {
			System.out.println("Exeption generated writing "+ex.getMessage());
			System.out.println("Message attempting to write "+ "Warning: "+ warningMessage+ " "+element.getName() + "\n");
		}
	}
	public void closeLog() throws IOException {
		this.buffer.close();
	}
}
