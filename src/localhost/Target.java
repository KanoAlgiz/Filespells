package localhost;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Target {
    private Path path;
    private Path last;

    Target(){
        path = Paths.get(System.getProperty("user.dir"));
        last = path;
    }

    void set(Path path){
        if(!this.path.equals(path)) last = this.path;
        this.path = path.normalize();
    }

    void set(String path){
        if (!this.path.equals(Paths.get(path))) last = this.path;
        this.path = Paths.get(path).normalize();
    }

    Path get(){
        return path;
    }

    Path getDir(){
        if (Files.isRegularFile(path)){
            return path.getParent();
        } else {
            return path;
        }
    }

    Path last(){

        return last;
    }
}
