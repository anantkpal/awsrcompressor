/*
 * AWSRCompressor
 * Author: Anant Pal -  anantkpal@yahoo.co.in
 * Copyright (c) 2013,2014 Anant Pal(anantkpal@yahoo.co.in).  All rights reserved.
 * The copyrights embodied in the content of this file are licensed
 * by Anant Pal(anantkpal@yahoo.co.in) under the BSD (revised) open source license.
 */


package in.anantkpal.awsrcompresor;

import in.anantkpal.awsrcompresor.html.HTMLCompressor;
import in.anantkpal.common.util.file.FileUtil;
import jargs.gnu.CmdLineParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class AWSRCompressor {
	
	private static final String VERSION="1.0-beta";
	
	public static void main(String[] args) {
		
		CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option typeOpt = parser.addStringOption("type");
        CmdLineParser.Option versionOpt = parser.addBooleanOption('V', "version");
        CmdLineParser.Option verboseOpt = parser.addBooleanOption('v', "verbose");
        CmdLineParser.Option nomungeOpt = parser.addBooleanOption("nomunge");
        CmdLineParser.Option linebreakOpt = parser.addStringOption("line-break");
        CmdLineParser.Option preserveSemiOpt = parser.addBooleanOption("preserve-semi");
        CmdLineParser.Option disableOptimizationsOpt = parser.addBooleanOption("disable-optimizations");
        CmdLineParser.Option helpOpt = parser.addBooleanOption('h', "help");
        CmdLineParser.Option charsetOpt = parser.addStringOption("charset");
        CmdLineParser.Option outputFilenameOpt = parser.addStringOption('o', "output");
        CmdLineParser.Option inputFilenameOpt = parser.addStringOption('i', "input");
        CmdLineParser.Option regexOpt = parser.addStringOption("regex");
        CmdLineParser.Option recursiveOpt = parser.addBooleanOption('r', "recurssive");
        
        Reader in = null;
        Writer out = null;
        
        try {

            parser.parse(args);

            
            //Help Display if args is Help
            Boolean help = (Boolean) parser.getOptionValue(helpOpt);
            if (help != null && help.booleanValue()) {
                usage();
                System.exit(0);
            }

            //Version Display if Args is Version
            Boolean version = (Boolean) parser.getOptionValue(versionOpt);
            if (version != null && version.booleanValue()) {
                version();
                System.exit(0);
            }
            //Validation of whether needs to run in verbose mode or not
            boolean verbose = parser.getOptionValue(verboseOpt) != null;
            
            //Setting the charset
            String charset = (String) parser.getOptionValue(charsetOpt);
            if (charset == null || !Charset.isSupported(charset)) {
                charset = "UTF-8";
                if (verbose) {
                    System.err.println("\n[INFO] Using charset " + charset);
                }
            }

            //Setting line columns width
            int linebreakpos = -1;
            String linebreakstr = (String) parser.getOptionValue(linebreakOpt);
            if (linebreakstr != null) {
                try {
                    linebreakpos = Integer.parseInt(linebreakstr, 10);
                } catch (NumberFormatException e) {
                    usage();
                    System.exit(1);
                }
            }

            //Selecting the type and validation of the same
            String typeOverride = (String) parser.getOptionValue(typeOpt);
            if (typeOverride != null && !typeOverride.equalsIgnoreCase("js") && !typeOverride.equalsIgnoreCase("css")&& !typeOverride.equalsIgnoreCase("html")) {
                usage();
                System.exit(1);
            }
            
            //Javascript Parameters
            boolean munge = parser.getOptionValue(nomungeOpt) == null;
            boolean preserveAllSemiColons = parser.getOptionValue(preserveSemiOpt) != null;
            boolean disableOptimizations = parser.getOptionValue(disableOptimizationsOpt) != null;

            //Recursive File Option
            boolean recursive = parser.getOptionValue(recursiveOpt) != null;
            
            //Regex 
            String regex = (String) (parser.getOptionValue(regexOpt) != null ? parser.getOptionValue(regexOpt) : "(.*)"); 
            
            
           //Read File/Folder Arguments
            List<File> filez = new ArrayList<File>();
           String inputFile = (String) parser.getOptionValue(inputFilenameOpt);
            if (inputFile==null) {
                    usage();
                    System.exit(1);
            }else{
            	File inputputFil = new File(inputFile);
            	if(inputputFil.isDirectory()){
            		filez= FileUtil.getFileList(inputputFil, regex,recursive);
            	}else{
            		filez.add(inputputFil);	
            	}
            }

            
            //Read Output File/Folder Arguments
            String output = (String) parser.getOptionValue(outputFilenameOpt);
            if (inputFile==null) {
                usage();
                System.exit(1);
            }
            
            if(recursive &!(new File(inputFile).isDirectory())){
            	usage();
                System.exit(1);
            }
            
            for (int i = 0; i < filez.size(); i++) {
				// Compressing Loop Starts Here
				String inputFilename = filez.get(i).getAbsolutePath();
				String outputFilename = filez.size()==1 ? output:output+"/"+filez.get(i).getAbsolutePath().substring(filez.get(i).getAbsolutePath().indexOf(inputFile)+inputFile.length()+1);
				String type = null;
				
				FileUtil.createDir(new File(outputFilename).getAbsolutePath());
				
				try {
					if (typeOverride != null) {
						type = typeOverride;
					} else {
						int idx = inputFilename.lastIndexOf('.');
						if (idx >= 0 && idx < inputFilename.length() - 1) {
							type = inputFilename.substring(idx + 1);
						}
					}

					if (type == null || !type.equalsIgnoreCase("js")
							&& !type.equalsIgnoreCase("css")
							&& !type.equalsIgnoreCase("html")) {
						usage();
						System.exit(1);
					}

					in = new InputStreamReader(new FileInputStream(
							inputFilename), charset);

					if (type.equalsIgnoreCase("js")) {

						try {
							final String localFilename = inputFilename;

							JavaScriptCompressor compressor = new JavaScriptCompressor(
									in, new ErrorReporter() {

										public void warning(String message,
												String sourceName, int line,
												String lineSource,
												int lineOffset) {
											System.err
													.println("\n[WARNING] in "
															+ localFilename);
											if (line < 0) {
												System.err.println("  "
														+ message);
											} else {
												System.err.println("  " + line
														+ ':' + lineOffset
														+ ':' + message);
											}
										}

										public void error(String message,
												String sourceName, int line,
												String lineSource,
												int lineOffset) {
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
							out = new OutputStreamWriter(new FileOutputStream(
									outputFilename), charset);
							compressor.compress(out, linebreakpos, munge,
									verbose, preserveAllSemiColons,
									disableOptimizations);

						} catch (EvaluatorException e) {
							e.printStackTrace();
							System.err.println("[ERROR] in " + inputFilename);

						}

					} else if (type.equalsIgnoreCase("css")) {
						CssCompressor compressor = new CssCompressor(in);
						in.close();
						in = null;
						out = new OutputStreamWriter(new FileOutputStream(
								outputFilename), charset);
						compressor.compress(out, linebreakpos);
					} else if (type.equalsIgnoreCase("html")) {
						HTMLCompressor compressor = new HTMLCompressor(in);
						in.close();
						in = null;
						out = new OutputStreamWriter(new FileOutputStream(
								outputFilename), charset);
						compressor.compress(out, linebreakpos);
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
                
                //Compressing Loop Ends Here
        } catch (CmdLineParser.OptionException e) {
            usage();
            System.exit(1);
        } 
		
	}
	
	
	private static void usage() {
        System.err.println(
                "AWSRCompressor Version: "+VERSION+"\n"

                        + "\nUsage: java -jar awsrcompressor-"+VERSION+".jar [options] -i <input file/folder> -o <ouput file/folder>\n"
                        + "\n"
                        + "Global Options\n"
                        + "  -V, --version             Print version information\n"
                        + "  -h, --help                Displays this information\n"
                        + "  --type <js|css|html>      Specifies the type of the input file\n"
                        + "  --charset <charset>       Read the input file using <charset>\n"
                        + "  --line-break <column>     Insert a line break after the specified column number\n"
                        + "  -v, --verbose             Display informational messages and warnings\n"
                        + "  -o <file>                 Place the output into <file>.\n"
                        + "  -i <file>                 Place the input <file>.\n"
                        + "  -r, --recurssive          If input path is for the folder it will do recurively\n"
                        + "  --regex                   If input path is for the folder than file selection regular expression\n\n"
                        
                        + "JavaScript Options\n"
                        + "  --nomunge                 Minify only, do not obfuscate\n"
                        + "  --preserve-semi           Preserve all semicolons\n"
                        + "  --disable-optimizations   Disable all micro optimizations\n\n");
    }
	
	
	private static void version() {
        System.err.println("AWSRCompressor Version: "+VERSION);
    }

}
