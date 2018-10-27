package UI;

import java.awt.HeadlessException;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import Process.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;

public class CheckerTool extends JFrame implements ActionListener {

	/**
	 * JFrame which interacts with the user
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * Private fields and components of the UI
	 */
	private JTextField textBrowse;
	private JButton btnBrowse;
	private JButton btnExplore;
	private JButton btnCancel;
	private JTextArea areaResult;
	private JButton btnCheck;
	private JLabel lblLogFile;
	private JTextField textLogPath;
	private JButton btnBrowseLog;
	
	/*
	 *  private variables not related to JFrame Controls
	 */
	private File fileToProcess;
	private File logFile;
	private UIController control;
	private String logger;
	
	public CheckerTool() throws HeadlessException {
		this.setTitle("Checker Tool");
		this.LocateComponents();
		this.addActionListeners();
	}

	public CheckerTool(String title) throws HeadlessException {
		this.setTitle(title);
		this.LocateComponents();
		this.addActionListeners();
	}
	/*
	 * Method that locates the components in the JFrame
	 */
	private void LocateComponents() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 53, 218, 87, 76, 0};
		gridBagLayout.rowHeights = new int[]{23, 28, 87, 134, 0, 15, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblProject = new JLabel("Project:");
		GridBagConstraints gbc_lblProject = new GridBagConstraints();
		gbc_lblProject.insets = new Insets(0, 0, 5, 5);
		gbc_lblProject.anchor = GridBagConstraints.EAST;
		gbc_lblProject.gridx = 1;
		gbc_lblProject.gridy = 1;
		getContentPane().add(lblProject, gbc_lblProject);
		
		textBrowse = new JTextField();
		GridBagConstraints gbc_textBrowse = new GridBagConstraints();
		gbc_textBrowse.fill = GridBagConstraints.HORIZONTAL;
		gbc_textBrowse.insets = new Insets(0, 0, 5, 5);
		gbc_textBrowse.gridx = 2;
		gbc_textBrowse.gridy = 1;
		getContentPane().add(textBrowse, gbc_textBrowse);
		textBrowse.setColumns(20);
		
		btnBrowse = new JButton("Browse");
		GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
		gbc_btnBrowse.insets = new Insets(0, 0, 5, 5);
		gbc_btnBrowse.anchor = GridBagConstraints.WEST;
		gbc_btnBrowse.gridx = 3;
		gbc_btnBrowse.gridy = 1;
		getContentPane().add(btnBrowse, gbc_btnBrowse);
		
		lblLogFile = new JLabel("Log File:");
		GridBagConstraints gbc_lblLogFile = new GridBagConstraints();
		gbc_lblLogFile.anchor = GridBagConstraints.EAST;
		gbc_lblLogFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblLogFile.gridx = 1;
		gbc_lblLogFile.gridy = 2;
		getContentPane().add(lblLogFile, gbc_lblLogFile);
		
		textLogPath = new JTextField();
		GridBagConstraints gbc_textLogPath = new GridBagConstraints();
		gbc_textLogPath.insets = new Insets(0, 0, 5, 5);
		gbc_textLogPath.fill = GridBagConstraints.BOTH;
		gbc_textLogPath.gridx = 2;
		gbc_textLogPath.gridy = 2;
		getContentPane().add(textLogPath, gbc_textLogPath);
		textLogPath.setColumns(10);
		
		btnBrowseLog = new JButton("Browse Log File");
		GridBagConstraints gbc_btnBrowseLog = new GridBagConstraints();
		gbc_btnBrowseLog.insets = new Insets(0, 0, 5, 5);
		gbc_btnBrowseLog.gridx = 3;
		gbc_btnBrowseLog.gridy = 2;
		getContentPane().add(btnBrowseLog, gbc_btnBrowseLog);
		
		areaResult = new JTextArea();
		GridBagConstraints gbc_areaResult = new GridBagConstraints();
		gbc_areaResult.insets = new Insets(0, 0, 5, 5);
		gbc_areaResult.fill = GridBagConstraints.BOTH;
		gbc_areaResult.gridx = 2;
		gbc_areaResult.gridy = 3;
		getContentPane().add(areaResult, gbc_areaResult);
		
		btnExplore = new JButton("Explore");
		GridBagConstraints gbc_btnExplore = new GridBagConstraints();
		gbc_btnExplore.anchor = GridBagConstraints.EAST;
		gbc_btnExplore.insets = new Insets(0, 0, 5, 5);
		gbc_btnExplore.gridx = 2;
		gbc_btnExplore.gridy = 4;
		getContentPane().add(btnExplore, gbc_btnExplore);
		
		btnCheck = new JButton("Check");
		GridBagConstraints gbc_btnCheck = new GridBagConstraints();
		gbc_btnCheck.insets = new Insets(0, 0, 5, 5);
		gbc_btnCheck.gridx = 3;
		gbc_btnCheck.gridy = 4;
		getContentPane().add(btnCheck, gbc_btnCheck);
		
		btnCancel = new JButton("Cancel");
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
		gbc_btnCancel.anchor = GridBagConstraints.WEST;
		gbc_btnCancel.gridx = 3;
		gbc_btnCancel.gridy = 5;
		getContentPane().add(btnCancel, gbc_btnCancel);
		this.setSize(403,312);
	}
	private void addActionListeners() {
		btnCancel.addActionListener(this);
		btnExplore.addActionListener(this);
		btnBrowse.addActionListener(this);
		btnCheck.addActionListener(this);
		btnBrowseLog.addActionListener(this);
	}
	/**
	 * Public Methods
	 */
	// actions performed by buttons

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if(actionEvent.getSource().equals(btnCancel)) {
			this.dispose();
		}else {
			if(actionEvent.getSource().equals(btnBrowse)) {
				this.BrowseAction();
			}else {
				if(actionEvent.getSource().equals(btnExplore)) {
					this.StartExploreAction();
				}else {
					if(actionEvent.getSource().equals(btnBrowseLog)) {
						this.browseLogPathAction();
					}else {
						this.StartCheckAction();
					}
				}
			}
		}
	}
	/**
	 * Private methods
	 */
	/**
	 * Method BrowseAction configures the selection of the project to check
	 */
	private void BrowseAction() {
		JFileChooser fileChooser = new JFileChooser( Paths.get(".").toAbsolutePath().toString());
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			textBrowse.setText(fileChooser.getSelectedFile().getPath());
			fileToProcess = fileChooser.getSelectedFile();
		}
	}
	/**
	 * Method StartExploreAction starts the process of exploring the project
	 */
	private void StartExploreAction() {
		if(showWarning()) {
			if(control == null) {
				if(logger == null) {
					logger = fileToProcess.getName()+"Log.txt";
				}
				try {
					control = new UIController(fileToProcess,logger);
					areaResult.append("Number of directories: "+control.getNumberOfDirectories()+"\n");
					areaResult.append("Number of Java files: "+control.getNumberOfCodes()+"\n");
				}catch(IOException ex){
					JOptionPane.showMessageDialog(this,"Log could not be created");
				}
			}
		}
	}
	/**
	 * Method StartCheckAction starts the process of checking the code
	 */
	private void StartCheckAction() {
		if(showWarning()) {
			if(control == null) {
				this.StartExploreAction();
			}
			control.startAnalysis();
		}
	}
	private Boolean showWarning() {
		if(fileToProcess ==null || !fileToProcess.exists()) {
			JOptionPane.showMessageDialog(this, "A Project needs to be selected");
			return false;
		}else {
			return true;
		}
	}
	private void browseLogPathAction() {
		JFileChooser fileChooser = new JFileChooser( Paths.get(".").toAbsolutePath().toString());
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text", "txt");
		fileChooser.setFileFilter(filter);
		if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			textLogPath.setText(fileChooser.getSelectedFile().getPath());
			logFile = fileChooser.getSelectedFile();
			logger = logFile.getPath();
		}
	}
	@Override
	public void dispose() {
		super.dispose();
	}
}
