package localhost;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static localhost.Checks.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String input;
        Target target = new Target();
        Target temp = new Target();
        Target pick = new Target();
        System.out.println(Arrays.deepToString(args));

        progcycle:
        while (true) {
            System.out.println("\nTarget: " + target.get().toString());
            System.out.println("Picked: " + pick.get().getFileName().toString() + "\n");
            input = scanner.nextLine().trim();
            List<String> spell = new ArrayList<>(Utils.findArgs(input.split(" ")));
            int sSize = spell.size();

            spellcycle:
            for (int word = 0; word < sSize; word++) {
                String lastWord = (word == 0 ? null : spell.get(word - 1));
                String nextWord = (word == sSize - 1 ? null : spell.get(word + 1));
                String thisWord = spell.get(word);

                switch (thisWord.toLowerCase()) {
                    case "size":
                        System.out.println("Size of " + target.get().getFileName() + " is " + Files.size(target.get()) + " bytes.");
                        break;
                    case "show":
                    case "contents":
                        try (DirectoryStream<Path> stream = Files.newDirectoryStream(target.getDir())) {
                            int i = 1;
                            for (Path file : stream) {
                                System.out.print("\t" + file.getFileName() + "\t");
                                if(i++ % 4 == 0) System.out.print("\n");
                            }
                        }
                        break;
                    case "delete":
                    case "del":
                    case "remove":
                        Files.deleteIfExists(target.get());
                        target.set(target.get().getParent());
                        break;
                    case "return":
                    case "back":
                        target.set(target.last());
                        break;
                    case "last":
                        pick.set(pick.last());
                        break;
                    case "log":
                        if (wordExists(nextWord) && Files.isRegularFile(target.get())) {
                            Files.write(target.get(), nextWord.substring(1).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                        }
                        word++;
                        break;
                    case "pick":
                    case "take":
                    case "choose":
                        pick.set(target.get());
                        break;
                    case "copy":
                    case "paste":
                        temp.set(target.getDir().resolve(pick.get().getFileName()));
                        if (Files.exists(temp.get())) {
                            System.out.println(temp.get().toString() + " is already exists!");
                        } else {
                            Files.copy(pick.get(), temp.get(), StandardCopyOption.COPY_ATTRIBUTES);
                            target.set(temp.get());
                        }
                        break;
                    case "move":
                    case "replace":
                        temp.set(target.getDir().resolve(pick.get().getFileName()));
                        if (Files.exists(temp.get())) {
                            System.out.println(temp.get().toString() + " is already exists!");
                        } else {
                            Files.move(pick.get(), temp.get());
                            Files.deleteIfExists(pick.get());
                            target.set(temp.get());
                        }
                        break;
                    case "rename":
                        temp.set(pick.getDir().resolve(nextWord.substring(1)));
                        if (Files.exists(temp.get())){
                            System.out.println(temp.get().toString() + " is already exists!");
                        } else {
                            Files.move(pick.get(), temp.get());
                            Files.deleteIfExists(pick.get());
                            target.set(temp.get());
                        }
                        break;
                    case "up":
                        target.set(target.get().getParent());
                        break;
                    case "new":
                    case "create":
                    case "make":
                        if (correctArg(nextWord)) {
                            try {
                                temp.set(target.getDir().resolve(nextWord.substring(1)));
                            } catch (InvalidPathException e) {
                                System.out.println("You used invalid characters!");
                                continue;
                            }
                            if (Files.exists(temp.get())) {
                                System.out.println(temp.get().toString() + " is already exists!");
                            } else {
                                target.set(temp.get());
                                if (target.get().toString().contains(".")) {
                                    Files.createFile(target.get());
                                } else {
                                    Files.createDirectory(target.get());
                                }
                            }
                        }
                        word++;
                        break;
                    case "root":
                        target.set(target.get().getRoot().toAbsolutePath());
                        break;
                    case "goto":
                        if (correctArg(nextWord)) {
                            temp.set(nextWord);
                            if (pathExists(temp.get())) {
                                target.set(temp.get());
                            }
                        }
                        word++;
                        break;
                    case "quit":
                    case "exit":
                        break progcycle;
                    case "stop":
                        break spellcycle;
                    default:
                        if (thisWord.charAt(0) == '|') {
                            temp.set(target.getDir().resolve(thisWord.substring(1)));
                            if (pathExists(temp.get())) {
                                target.set(temp.get());
                            }
                        } else {
                            System.out.println("\"" + thisWord + "\" is not correct magic word.");
                        }
                        if (sSize <= 1) break spellcycle;
                }
            }
        }

    }

}
