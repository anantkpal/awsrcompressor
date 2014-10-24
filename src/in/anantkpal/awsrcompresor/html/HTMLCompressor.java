/*
 * AWSRCompressor
 * Author: Anant Pal -  anantkpal@yahoo.co.in
 * Copyright (c) 2013,2014 Anant Pal(anantkpal@yahoo.co.in).  All rights reserved.
 * The copyrights embodied in the content of this file are licensed
 * by Anant Pal(anantkpal@yahoo.co.in) under the BSD (revised) open source license.
 */


package in.anantkpal.awsrcompresor.html;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class HTMLCompressor {

	
	 private StringBuffer htmlSrc = new StringBuffer();

	/**
	 * @throws IOException 
	 * 
	 */
	public HTMLCompressor(Reader in) throws IOException {
		// Read the stream...
        int c;
        while ((c = in.read()) != -1) {
        	htmlSrc.append((char) c);
        }
    }
	
	public void compress(Writer out, int linebreakpos)throws IOException {
		int startIndex = 0;
        int endIndex = 0;
        
      //Replace all spaces and new line characters between tags
        startIndex=0;
        endIndex=0;
        String token="";
        while ((startIndex = htmlSrc.indexOf(">", startIndex)) >= 0 && (endIndex = htmlSrc.indexOf("<", startIndex))>=0) {
            token=htmlSrc.substring(startIndex+1, endIndex);
            htmlSrc.replace(startIndex+1, endIndex, token.trim());
            startIndex=startIndex+1;
        }
        
        //Compress Styles
        startIndex=0;
        endIndex=0;
        token="";
        while ((startIndex = htmlSrc.indexOf("<style", startIndex)) >= 0 && (endIndex = htmlSrc.indexOf(">", startIndex))>=0) {
        	startIndex=endIndex;
        	endIndex = htmlSrc.indexOf("</style>", startIndex);
        	token=htmlSrc.substring(startIndex+1, endIndex);
        	
        	CssCompressor compressor = new CssCompressor(new StringReader(token));
            StringWriter sout = new StringWriter();
            compressor.compress(sout, linebreakpos);
            if(sout!=null && !sout.toString().trim().equalsIgnoreCase("")){
            	token=sout.toString();
            }
            htmlSrc.replace(startIndex+1, endIndex, token.trim());
        }
        
        //Compress Javascript
        startIndex=0;
        endIndex=0;
        token="";
        while ((startIndex = htmlSrc.indexOf("<script", startIndex)) >= 0 && (endIndex = htmlSrc.indexOf(">", startIndex))>=0) {
        	if(htmlSrc.substring(startIndex, endIndex).contains("src=\"")){
        		startIndex=endIndex;
        		continue;
        	}
        	startIndex=endIndex;
        	endIndex = htmlSrc.indexOf("</script>", startIndex);
        	token=htmlSrc.substring(startIndex+1, endIndex);
        	System.out.println(token);
        	JavaScriptCompressor compressor = new JavaScriptCompressor(new StringReader(token), new ErrorReporter() {

                public void warning(String message, String sourceName,int line, String lineSource, int lineOffset) {
                    if (line < 0) {
                        System.err.println("  " + message);
                    } else {
                        System.err.println("  " + line + ':' + lineOffset + ':' + message);
                    }
                }

                public void error(String message, String sourceName,int line, String lineSource, int lineOffset) {
                    if (line < 0) {
                        System.err.println("  " + message);
                    } else {
                        System.err.println("  " + line + ':' + lineOffset + ':' + message);
                    }
                }

                public EvaluatorException runtimeError(String message, String sourceName,int line, String lineSource, int lineOffset) {
                    error(message, sourceName, line, lineSource, lineOffset);
                    return new EvaluatorException(message);
                }
            });
            StringWriter sout = new StringWriter();
            compressor.compress(sout, linebreakpos, true, false,true, false);
            if(sout!=null && !sout.toString().trim().equalsIgnoreCase("")){
            	token=sout.toString();
            }
            htmlSrc.replace(startIndex+1, endIndex, token.trim());
        }
        
     // remove all comment blocks...
        while ((startIndex = htmlSrc.indexOf("<!--", startIndex)) >= 0) {
            endIndex = htmlSrc.indexOf("-->", startIndex + 3);
            htmlSrc.replace(startIndex, endIndex+3, "");
        }
        
        
        //add line breaks if needed
        if (linebreakpos >= 0) {
            int i = 0;
            int linestartpos = 0;
            while (i < htmlSrc.length()) {
                char c = htmlSrc.charAt(i++);
                if (c == '>' && i - linestartpos > linebreakpos) {
                	htmlSrc.insert(i, '\n');
                    linestartpos = i;
                }
            }

        }
        
        out.write(htmlSrc.toString().trim());
	}
	
	

}
