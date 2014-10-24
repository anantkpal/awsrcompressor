package in.anantkpal.awsrcompresor.options;

import java.io.File;
import java.util.List;

public class GlobalOptions {

	private String type;
	private String charset;
	private int lineBreak;
	
	
	
	/**
	 * @return the lineBreak
	 */
	public int getLineBreak() {
		return lineBreak;
	}
	/**
	 * @param lineBreak the lineBreak to set
	 */
	public void setLineBreak(int lineBreak) {
		this.lineBreak = lineBreak;
	}
	private String inputFileName;
	/**
	 * @return the inputFileName
	 */
	public String getInputFileName() {
		return inputFileName;
	}
	/**
	 * @param inputFileName the inputFileName to set
	 */
	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}
	private List<File> inputFiles;
	private String outputFileName;
	private boolean recurssive;
	private String regex;
	private boolean verbose;
	
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}
	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}


	/**
	 * @return the inputFiles
	 */
	public List<File> getInputFiles() {
		return inputFiles;
	}
	/**
	 * @param inputFiles the inputFiles to set
	 */
	public void setInputFiles(List<File> inputFiles) {
		this.inputFiles = inputFiles;
	}
	/**
	 * @return the outputFileName
	 */
	public String getOutputFileName() {
		return outputFileName;
	}
	/**
	 * @param outputFileName the outputFileName to set
	 */
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
	/**
	 * @return the recurssive
	 */
	public boolean isRecurssive() {
		return recurssive;
	}
	/**
	 * @param recurssive the recurssive to set
	 */
	public void setRecurssive(boolean recurssive) {
		this.recurssive = recurssive;
	}
	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}
	/**
	 * @param regex the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}
	/**
	 * @return the verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}
	/**
	 * @param verbose the verbose to set
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	
	
}
