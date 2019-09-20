import java.util.HashMap;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;
import java.io.File;


class rename {
    public static void main(String[] args) {
      Queue queue = new LinkedList();
      Iterator iter = queue.iterator();
      LinkedList<String> filelist = new LinkedList<String>(); //filenames array
      int validity = parse(args, queue, filelist);
      //1 normal  2 help  3 error
      if(validity==2){
        return;
      }else if(validity==3){
        return;
      }
      validity = check(queue, filelist);
      if(validity==3){
        return;
      }


      Iterator iterator = queue.iterator();
      int len = filelist.size();
      while(iterator.hasNext()){
        HashMap<String, String> arguments = (HashMap<String, String>)iterator.next();

        for (String key : arguments.keySet()) {
          if(key.compareTo("prefix")==0){
            for(int i = 0; i < len; i++){
              File oldname = new File(filelist.get(i));
              File newname = new File(arguments.get(key)+filelist.get(i));

              if (!oldname.renameTo(newname)) {
                  System.out.println("Failed to rename file");
                  return;
              }
              filelist.set(i, arguments.get(key)+filelist.get(i));
            }
          }else if(key.compareTo("suffix")==0){
            for(int i = 0; i < len; i++){
              File oldname = new File(filelist.get(i));
              File newname = new File(filelist.get(i)+arguments.get(key));

              if (!oldname.renameTo(newname)) {
                  System.out.println("Failed to rename file");
                  return;
              }
              filelist.set(i, filelist.get(i)+arguments.get(key));
            }
          }else if(key.compareTo("replace")==0){
            for(int i = 0; i < len; i++){
              String para = arguments.get(key);
              String[] parts = para.split(" ");
              String part1 = parts[0];
              String part2 = parts[1];
              File oldname = new File(filelist.get(i));
              File newname = new File(filelist.get(i).replaceAll(part1, part2));
              if (!oldname.renameTo(newname)) {
                  System.out.println("Failed to rename file with replacement");
                  return;
              }
              filelist.set(i, filelist.get(i).replaceAll(part1, part2));
            }
          }
        }

      }
    }

    //second pass for queue to keep track of filename existence for every replace call before modification
    static int check(Queue queue, LinkedList<String> filelist){
      LinkedList<String> newfilelist = new LinkedList<String>(); //keep track of the new names
      newfilelist = (LinkedList) filelist.clone();
      Iterator iterator = queue.iterator();
      int len = newfilelist.size();
      while(iterator.hasNext()){
        HashMap<String, String> arguments = (HashMap<String, String>)iterator.next();
        for (String key : arguments.keySet()) {
          if(key.compareTo("prefix")==0){
            for(int i = 0; i < len; i++){
              newfilelist.set(i, arguments.get(key) + newfilelist.get(i));
            }
          }else if(key.compareTo("suffix")==0){
              for(int i = 0; i < len; i++){
                newfilelist.set(i, newfilelist.get(i) + arguments.get(key));
              }
          }else if(key.compareTo("replace")==0){
            for(int i = 0; i < len; i++){
              String args = arguments.get(key);
              String[] parts = args.split(" ");
              String part1 = parts[0];
              String part2 = parts[1];
              if(newfilelist.get(i).contains(part1)){
                newfilelist.set(i, newfilelist.get(i).replaceAll(part1, part2));
              }else{
                  System.out.println("Error: replacement string not found in filenames");
                  return 3;
              }
            }
          }
        }
      }
      return 1;
    }
    //first pass get the file name list and process queue
    static int parse(String[] args, Queue queue, LinkedList<String> filelist) {
        //components of each option

        //help flag
        boolean helpflag = false;
        //file flag
        boolean fileflag = false;
        //option flag
        boolean optionflag = false;

        //special option
        SimpleDateFormat formatDate = new SimpleDateFormat("MM-dd-yyyy");
        String date = formatDate.format(new Date());
        SimpleDateFormat formatTime = new SimpleDateFormat("hh-mm-ss");
        String time = formatTime.format(new Date());

        int len = args.length;

        for (int i = 0; i < len; i++ ){
          String entry = args[i];
          if(entry.startsWith("-")){
            if(entry.substring(1).compareTo("prefix")==0){
              optionflag = true;
              String prefix = new String();
              prefix="";
              int j = 1;
              if(((i+1)==len) || (args[i+1].startsWith("-"))){
                  System.out.println("Error: invalid prefix option, needs to specify arguments correctly");
                  return 3;
              }else{
                if(args[i+j].compareTo("@time")==0){
                  prefix=prefix+time;
                }else if(args[i+j].compareTo("@date")==0){
                  prefix=prefix+date;
                }else{
                  prefix=prefix+args[i+j];
                }
                j++;
                while(((i+j) < len)&& (! args[i+j].startsWith("-") )){
                  if(args[i+j].compareTo("@time")==0){
                    prefix=prefix+time;
                  }else if(args[i+j].compareTo("@date")==0){
                    prefix=prefix+date;
                  }else{
                    prefix=prefix+args[i+j];
                  }
                  j++;
                }
                i=i+j-1;
              }

              HashMap<String, String> arguments = new HashMap<String, String>();
              if(prefix.compareTo("@time")==0){
                prefix = time;
              }else if(prefix.compareTo("@date")==0){
                prefix = date;
              }
              arguments.put("prefix", prefix);
              queue.add(arguments);

            }else if(entry.substring(1).compareTo("suffix")==0){
              optionflag = true;
              String suffix = new String();
              suffix="";
              int j = 1;
              if(((i+1)==len) || (args[i+1].startsWith("-"))){
                  System.out.println("Error: invalid suffix option, needs to specify arguments correctly");
                  return 3;
              }else{
                if(args[i+j].compareTo("@time")==0){
                  suffix+=time;
                }else if(args[i+j].compareTo("@date")==0){
                  suffix+=date;
                }else{
                  suffix+=args[i+j];
                }
                j++;

                while(((i+j) < len)&&(! args[i+j].startsWith("-") )){
                  if(args[i+j].compareTo("@time")==0){
                    suffix+=time;
                  }else if(args[i+j].compareTo("@date")==0){
                    suffix+=date;
                  }else{
                    suffix+=args[i+j];
                  }
                  j++;
                }
                i=i+j-1;
              }
              HashMap<String, String> arguments = new HashMap<String, String>();
              arguments.put("suffix", suffix);
              queue.add(arguments);
            }else if(entry.substring(1).compareTo("help")==0) {
              //flag
              helpflag = true;
              printHelp();
              return 2;

            }else if(entry.substring(1).compareTo("replace")==0) {
              optionflag = true;
              String replace = new String();
              replace = "";

              if ( ((i+1)==len) || ((i+2)==len) || (args[i+1].startsWith("-")) || (args[i+2].startsWith("-")) ){
                System.out.println("Error: invalid replace option, needs to specify replacement arguments correctly");
                return 3;
              }
              String latter = args[i+2];
              if(latter.compareTo("@time")==0){
                latter = time;
              }else if(latter.compareTo("@date")==0){
                latter = date;
              }
              replace = args[i+1] + " " + latter;

              HashMap<String, String> arguments = new HashMap<String, String>();
              arguments.put("replace", replace);
              queue.add(arguments);
              i += 2;

            }else if(entry.substring(1).compareTo("file")==0){
              //flag
              if(fileflag){
                System.out.println("Error: -file can only be called once");
                return 3;
              }
              fileflag = true;
              String file = new String();
              file = "";

              if(((i+1)==len) || (args[i+1].startsWith("-"))){
                  System.out.println("Error: invalid file option, needs to specify filename correctly");
                  return 3;
              }else{
                //check file exist
                File tmp = new File(args[i+1]);
                boolean exists = tmp.exists();
                if(!exists){
                  System.out.println("Error: filename: " + args[i+1] + " does not exists");
                  return 3;
                }
                file+=args[i+1];
                filelist.add(args[i+1]);
                int j = 2;
                while(((i+j) < len) && (! args[i+j].startsWith("-") )){
                  //check file exist
                  tmp = new File(args[i+j]);
                  exists = tmp.exists();
                  if(!exists){
                    System.out.println("Error: filename: " + args[i+j] + " does not exists");
                    return 3;
                  }
                  file+=" ";
                  file+=args[i+j];
                  filelist.add(args[i+j]);
                  j++;
                }
                HashMap<String, String> arguments = new HashMap<String, String>();
                arguments.put("file", file);
                queue.add(arguments);
                i = i + j-1;
              }

            }else{
              //this is invalid
              System.out.println("Error: invalid option or misplaced dash in arguments!@2 ");
              return 3;
            }
          }else{
            //this is invalid
            System.out.println("Error: invalid option or misplaced dash in arguments! @3");
            return 3;
          }
        }



        // option flag
        if(!optionflag){
          //this is invalid
          System.out.println("Error: at least one option other than -file needs to be stated! ");
          return 3;
        }
        // file flag
        if(!fileflag){
          //this is invalid
          System.out.println("Error: at least one -file needs to be stated! ");
          return 3;
        }
        // no need for helper flag
        return 1;
    }



    // Print command-line syntax
    static void printHelp() {
        System.out.println("(c) 2019 Mingze Lin. Last revised: Sep 10, 2019.");
        System.out.println("Usage Description:");
        System.out.println("Every invocation requires as least one option, plus the `-file' option to specify a target filename.");
        System.out.println("If multiple files are specified, you should apply all operations to each file.");
        System.out.println("You can perform multiple operations at once e.g. `-prefix' and `-replace' can both be applied to one or more files with a single evocation. The exception to this is `-help’, which will cause other arguments to be ignored, and help to be displayed instead.");
        System.out.println("Multiple options may be passed on the command-line in a single invocation, as long as they follow this format. Options should be processed in order, with the output from one option being passed as input to the next option (i.e. your application should apply options from left-right in the order presented, with the output from one option being passed to the next).");
        System.out.println("The following options are available: ");
        System.out.println(" -help                    :: print out a help page including student name, correct usage and options, and exit the program.");
        System.out.println(" -prefix [string]         :: rename [filename] so that it starts with [string]. ");
        System.out.println(" -suffix [string]         :: rename [filename] so that it ends with [string].");
        System.out.println(" -replace [str1] [str2]   :: rename [filename] by replacing all instances of [str1] with [str2]. ");
        System.out.println(" -file [filename]         :: denotes the [filename] do be modified.");
    }
}
