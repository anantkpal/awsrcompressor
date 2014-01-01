awsrcompressor -*A Website Static Resource Compressor*
====================================


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

