/*
 * AWSRBootstrap
 * Author: Anant Pal -  anantkpal@yahoo.co.in
 * Copyright (c) 2014 Anant Pal(anantkpal@yahoo.co.in).  All rights reserved.
 * The copyrights embodied in the content of this file are licensed
 * by Anant Pal(anantkpal@yahoo.co.in) under the BSD (revised) open source license.
 */


package in.anantkpal.awsrcompresor.bootstrap;

import in.anantkpal.awsrcompresor.AWSRCompressor;
import in.anantkpal.awsrcompresor.options.Options;
import in.anantkpal.common.util.file.FileUtil;
import jargs.gnu.CmdLineParser;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class AWSRBootstrap {
	
	private static final String VERSION="1.1";
	
	public static void main(String[] args) {
		Options options =new Options();
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
            options.getGlobalOptions().setVerbose(verbose);
            
            //Setting the charset
            String charset = (String) parser.getOptionValue(charsetOpt);
            if (charset == null || !Charset.isSupported(charset)) {
                charset = "UTF-8";
                if (verbose) {
                    System.err.println("\n[INFO] Using charset " + charset);
                }
            }
            options.getGlobalOptions().setCharset(charset);
            
            //Setting line columns width
            String linebreakstr = (String) parser.getOptionValue(linebreakOpt);
            options.getGlobalOptions().setLineBreak(-1);
            if (linebreakstr != null) {
                try {
                    options.getGlobalOptions().setLineBreak(Integer.parseInt(linebreakstr, 10));
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
            options.getGlobalOptions().setType(typeOverride);
            
            
            //Javascript Parameters
            boolean munge = parser.getOptionValue(nomungeOpt) == null;
            options.getJsOptions().setNomunge(munge);
            boolean preserveAllSemiColons = parser.getOptionValue(preserveSemiOpt) != null;
            options.getJsOptions().setPreserveSemicolon(preserveAllSemiColons);
            boolean disableOptimizations = parser.getOptionValue(disableOptimizationsOpt) != null;
            options.getJsOptions().setDisableOptimizations(disableOptimizations);

            //Recursive File Option
            boolean recursive = parser.getOptionValue(recursiveOpt) != null;
            options.getGlobalOptions().setRecurssive(recursive);
            
            //Regex 
            String regex = (String) (parser.getOptionValue(regexOpt) != null ? parser.getOptionValue(regexOpt) : "(.*)"); 
            options.getGlobalOptions().setRegex(regex);
            
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
            options.getGlobalOptions().setInputFileName(inputFile);
            options.getGlobalOptions().setInputFiles(filez);
            
            
            //Read Output File/Folder Arguments
            String output = (String) parser.getOptionValue(outputFilenameOpt);
            if (output==null) {
                usage();
                System.exit(1);
            }
            options.getGlobalOptions().setOutputFileName(output);
            
            if(recursive &!(new File(inputFile).isDirectory())){
            	usage();
                System.exit(1);
            }
            
	        AWSRCompressor compressor = new AWSRCompressor(options);    
	        compressor.compress();
        
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
