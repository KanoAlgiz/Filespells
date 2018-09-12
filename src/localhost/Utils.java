package localhost;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class Utils {
    static ArrayList<String> findArgs(String[] spell) {
        ArrayList<String> list = new ArrayList<>();
        StringBuilder path = new StringBuilder();
        for (String aSpell : spell) {
            if (path.length() > 1) {
                if (aSpell.endsWith("\"")) {
                    path.append(" ").append(aSpell, 0, aSpell.length() - 1);
                    list.add(path.toString());
                    path = new StringBuilder();
                } else path.append(" ").append(aSpell);
            } else if (aSpell.startsWith("\"")) {
                path.append(aSpell.substring(1));
                if (aSpell.endsWith("\"")) {
                    list.add(path.toString().substring(0, path.length() - 1));
                    path = new StringBuilder();
                }
            } else {
                list.add(aSpell);
            }
        }
        if (path.length() > 1) {
            System.out.println("Incorrect spell! Number of quote marks must be even.");
        }
        list.add(null);
        return list;
    }

    static void unzip(Path zipFile, Path destination) {
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile.toString()))) {
            ZipEntry entry;
            String name;
            while ((entry = zipIn.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream out = new FileOutputStream(destination.toString() + System.getProperty("file.separator") + name);
                int data;
                while ((data = zipIn.read()) != -1) {
                    out.write(data);
                }
                out.flush();
                zipIn.closeEntry();
                out.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println(zipFile.toString() + " is not found!");
        } catch (IOException e) {
            System.out.println("Zip file is not readable at the moment!");
        }
    }


}
