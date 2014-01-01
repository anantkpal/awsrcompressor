##awsrcompressor
#A Website Static Resource Compressor*

##Overview
*awsrcompressor* can compress HTML,js,css files.It is built using [YUICompressor](http://yui.github.io/yuicompressor/) as core for 
compressing js and css files, and custom code to trim spaces between tags, removing commented code in HTML as well Javascript and CSS content
present in the html file itself. It can compress the files recurssively if the input folder and target folder path is given.


## Usage
```
Usage: java -jar awsrcompressor-1.0-beta.jar [options] -i <input file/folder> -o <ouput file/folder>

Global Options
  -V, --version             Print version information
  -h, --help                Displays this information
  --type <js|css|html>      Specifies the type of the input file
  --charset <charset>       Read the input file using <charset>
  --line-break <column>     Insert a line break after the specified column number
  -v, --verbose             Display informational messages and warnings
  -o <file>                 Place the output into <file>.
  -i <file>                 Place the input <file>.
  -r, --recurssive          If input path is for the folder it will do recurively
  --regex                   If input path is for the folder than file selection regular expression

JavaScript Options
  --nomunge                 Minify only, do not obfuscate
  --preserve-semi           Preserve all semicolons
  --disable-optimizations   Disable all micro optimizations

```

## Authors

[Anant Pal](https://www.github.com/anantkpal)

#Copyright And License
Copyright (c) 2013 Anant Pal(anantkpal@yahoo.co.in) All rights reserved. 
The copyrights embodied in the content of this file are licensed by Anant Pal(anantkpal@yahoo.co.in) under the BSD open source license.

