package localhost;

import java.nio.file.Files;
import java.nio.file.Path;

class Checks {

    static boolean wordExists(String s){
        if(s == null){
            System.out.println("Spell is wrong, some arguments are missing!");
            return false;
        } else return true;
    }

    static boolean correctArg(String path){
        if(path == null){
            System.out.println("Please do not forget to specify directory/file name!");
            return false;
        } else if(path.charAt(0) != '|'){
            System.out.println("Please enclose all paths and filenames in quotemarks!");
            return false;
        }
        return true;
    }

    static boolean pathExists(Path path){
        if(!Files.exists(path)){
            System.out.println("Given path \"" + path.toString() + "\" is not exists!");
            return false;
        } else return true;
    }
}
