package localhost;

import java.nio.file.Files;
import java.nio.file.Path;

class Checks {

    static boolean correctArg(String path){
        if(path == null){
            System.out.println("Please do not forget to specify all needed arguments!");
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
