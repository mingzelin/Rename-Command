# Rename-Command


Options
Options are specified as a list of strings provided on the command-line that determine how the program executes. Options always start with a dash ("-"), and multiple options can be provided at the same time. The following options are supported:

  -help                     :: print out a help page and exit the program.
  -prefix [string]          :: rename [filename] so that it starts with [string]. 
  -suffix [string]          :: rename [filename] so that it ends with [string]. 
  -replace [str1] [str2]    :: rename [filename] by replacing all instances of [str1] with [str2]. 
  -file [filename]          :: denotes the [filename] to be modified.
Arguments
Arguments are parameters for each option, and always follow options in the parameter list. Most options require arguments to be operate.

Arguments cannot start with a dash ("-"), and consist of one of the following:

a string, or
@date which should be replaced by your program with the current date as a string (MM-DD-YYYY format), or
@time which should be replaced by your program with the current time as a string (HH-MM-SS format), or
optionally, any combination of these separated by a space. Note that only some options support multiple arguments.
Specifying multiple arguments to `-prefix' or `-suffix' denotes that the arguments should be concatenated together, and passed to the option as a single argument (e.g. `-prefix a b c' is the same as '-prefix abc').

The `-replace` option is unique, in that it must be passed two (and only two) arguments, representing the source and destination for the rename operation.

The `file' option can support multiple arguments, representing multiple filenames. If more than a single filename is passed, it should be assumed that the operations are performed on all files. A valid filename argument can consist of:

a string representing a single file, or
multiple strings representing multiple files, separated by spaces,
multiple files specified using using an asterisk (*) or question mark (?) as wildcards.
Note on wildcards: if you are using a Unix shell (e.g. bash, zsh), the wildcards will be expanded by the shell, and the filenames will be passed as separate strings to your program automatically; you can assume that this is how the TAs will test it

You can assume that files are always in the current directory (i.e. you do not need to support relative paths when renaming files).

Usage
Every invocation requires as least one option, plus the `-file' option to specify a target filename.

You can perform multiple operations at once e.g. `-prefix' and `-replace' can both be applied to one or more files with a single evocation. The exception to this is `-help’, which will cause other arguments to be ignored, and help to be displayed instead.

Multiple options may be passed on the command-line in a single invocation, as long as they follow this format. Options should be processed in order, with the output from one option being passed as input to the next option (i.e. your application should apply options from left-right in the order presented, with the output from one option being passed to the next).

Examples
Valid examples (given the existence of files named `f1' and `f2'):

java rename -prefix a -file f1 —> af1
java rename -prefix a -suffix b -file f1 —> af1b
java rename -prefix a -suffix b -file f? —> af1b af2b
java rename -prefix y y _ -file f1 —> yy_f1
java rename -prefix @date _ -file f1 —> 08-26-2019_f1
java rename -replace f abcd -file f1 —> abcd1
java rename -prefix a -prefix b -file f1 —> baf1
java rename -suffix a b -file f1 —> f1ab
Invalid examples:

java rename f1 // no options provided
java rename -prefix a // no filename provided
java rename -prefix a -suffix b f1 // no filename provided (`f1' interpreted as argument)
java rename -prefix a -suffix b -file f3 // file f3 does not exist
