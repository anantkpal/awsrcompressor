/*
 * AWSRCompressor
 * Author: Anant Pal -  anantkpal@yahoo.co.in
 * Copyright (c) 2013,2014 Anant Pal(anantkpal@yahoo.co.in).  All rights reserved.
 * The copyrights embodied in the content of this file are licensed
 * by Anant Pal(anantkpal@yahoo.co.in) under the BSD (revised) open source license.
 */


package in.anantkpal.awsrcompresor;

import in.anantkpal.awsrcompresor.html.HTMLCompressor;
import in.anantkpal.awsrcompresor.options.Options;
import in.anantkpal.common.util.file.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class AWSRCompressor {
	
	private Options options;
	
	public void compress() {
		 	Reader in = null;
	        Writer out = null;
	         
			List<File> filez = options.getGlobalOptions().getInputFiles();
		
            for (int i = 0; i < filez.size(); i++) {
				// Compressing Loop Starts Here
				String inputFilename = filez.get(i).getAbsolutePath();
				System.out.println("Compressing "+inputFilename+" ....");
				String outputFilename = !(new File(options.getGlobalOptions().getInputFileName()).isDirectory()) ? options.getGlobalOptions().getOutputFileName():options.getGlobalOptions().getOutputFileName()+"/"+filez.get(i).getAbsolutePath().substring(filez.get(i).getAbsolutePath().indexOf(options.getGlobalOptions().getInputFileName())+options.getGlobalOptions().getInputFileName().length()+1);
				System.out.println("Compressed output file "+outputFilename+" ....");
				String type = null;
				
				FileUtil.createDir(new File(outputFilename).getAbsolutePath());
				
				try {
					if (options.getGlobalOptions().getType() != null) {
						type = options.getGlobalOptions().getType();
					} else {
						int idx = inputFilename.lastIndexOf('.');
						if (idx >= 0 && idx < inputFilename.length() - 1) {
							type = inputFilename.substring(idx + 1);
						}
					}

					in = new InputStreamReader(new FileInputStream(inputFilename), options.getGlobalOptions().getCharset());

					if (type.equalsIgnoreCase("js")) {

						try {
							final String localFilename = inputFilename;

							JavaScriptCompressor compressor = new JavaScriptCompressor(in, new ErrorReporter() {

										public void warning(String message,	String sourceName, int line,String lineSource,int lineOffset) {
											System.err.println("\n[WARNING] in "+ localFilename);
											if (line < 0) {
												System.err.println("  "+ message);
											} else {
												System.err.println("  " + line+ ":" + lineOffset +":" + message);
											}
										}

										public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
											System.err.println("[ERROR] in "+ localFilename);
											if (line < 0) {
												System.err.println("  "	+ message);
											} else {
												System.err.println("  " + line+':' + lineOffset+ ':' + message);
											}
										}

										public EvaluatorException runtimeError(	String message,	String sourceName, int line,String lineSource,int lineOffset) {
											error(message, sourceName, line,lineSource, lineOffset);
											return new EvaluatorException(message);
										}
									});
							in.close();
							in = null;
							out = new OutputStreamWriter(new FileOutputStream(outputFilename), options.getGlobalOptions().getCharset());
							compressor.compress(out, options.getGlobalOptions().getLineBreak(), options.getJsOptions().isNomunge(),options.getGlobalOptions().isVerbose(), options.getJsOptions().isPreserveSemicolon(),options.getJsOptions().isDisableOptimizations());

						} catch (EvaluatorException e) {
							e.printStackTrace();
							System.err.println("[ERROR] in " + inputFilename);

						}

					} else if (type.equalsIgnoreCase("css")) {
						CssCompressor compressor = new CssCompressor(in);
						in.close();
						in = null;
						out = new OutputStreamWriter(new FileOutputStream(outputFilename), options.getGlobalOptions().getCharset());
						compressor.compress(out, options.getGlobalOptions().getLineBreak());
					} else if (type.equalsIgnoreCase("html")) {
						HTMLCompressor compressor = new HTMLCompressor(in);
						in.close();
						in = null;
						out = new OutputStreamWriter(new FileOutputStream(outputFilename), options.getGlobalOptions().getCharset());
						compressor.compress(out, options.getGlobalOptions().getLineBreak());
					}

				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				} finally {

					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
                
		
	}

	/**
	 * @param options
	 */
	public AWSRCompressor(Options options) {
		super();
		this.options = options;
	}

	/**
	 * @return the options
	 */
	public Options getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(Options options) {
		this.options = options;
	}
	
	


}
