package localhost;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static localhost.Checks.correctArg;
import static localhost.Checks.pathExists;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Target target = new Target();
        Target temp = new Target();
        Target pick = new Target();
        List<String> spell;
        int sSize;

        progcycle:
        while (true) {
            System.out.println("\nTarget: " + target.get().toString());
            System.out.println("Picked: " + pick.get().getFileName().toString() + "\n");

            if (args.length == 0) {
                args = scanner.nextLine().trim().split(" ");
                spell = Utils.findArgs(args);
            } else {
                spell = new ArrayList<>(Arrays.asList(args));
                spell.add(null);
            }
            args = new String[]{};
            sSize = spell.size();

            spellcycle:
            for (int word = 0; word < sSize - 1; word++) {
                String nextWord = spell.get(word + 1);
                String thisWord = spell.get(word);
                switch (thisWord.toLowerCase()) {
                    case "home":
                        target.set(Paths.get(System.getProperty("user.dir")));
                        break;
                    case "size":
                        System.out.println("Size of " + target.get().getFileName() + " is " + Files.size(target.get()) + " bytes.");
                        break;
                    case "show":
                    case "list":
                    case "dir":
                    case "look":
                        try (DirectoryStream<Path> stream = Files.newDirectoryStream(target.getDir())) {
                            for (Path file : stream) {
                                System.out.println("\t" + file.getFileName());
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
                        if (correctArg(spell.get(word + 1))) {
                            if (Files.isRegularFile(target.get())) {
                                String s = nextWord + System.getProperty("line.separator");
                                Files.write(target.get(), s.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                            } else System.out.println("Your target is not a file!");
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
                    case "clone":
                        temp.set(target.getDir().resolve(pick.get().getFileName()));
                        if (Files.exists(temp.get())) {
                            System.out.println(temp.get().toString() + " is already exists!");
                        } else {
                            Files.copy(pick.get(), temp.get(), StandardCopyOption.COPY_ATTRIBUTES);
                            target.set(temp.get());
                        }
                        word++;
                        break;
                    case "move":
                    case "replace":
                        try {
                            temp.set(target.getDir().resolve(pick.get().getFileName()));
                        } catch (InvalidPathException e) {
                            System.out.println("You used invalid characters!");
                            continue;
                        }

                        if (Files.exists(temp.get())) {
                            System.out.println(temp.get().toString() + " is already exists!");
                        } else {
                            Files.move(pick.get(), temp.get());
                            Files.deleteIfExists(pick.get());
                            target.set(temp.get());
                        }
                        break;
                    case "rename":
                        if (correctArg(nextWord)) {
                            try {
                                temp.set(pick.get().getParent().resolve(nextWord));
                            } catch (InvalidPathException e) {
                                System.out.println("You used invalid characters!");
                                continue;
                            }
                            if (Files.exists(temp.get())) {
                                System.out.println(temp.get().toString() + " is already exists!");
                            } else {
                                Files.move(pick.get(), temp.get());
                                Files.deleteIfExists(pick.get());
                                target.set(temp.get());
                            }
                        }
                        word++;
                        break;
                    case "up":
                    case "..":
                    case "parent":
                        target.set(target.getDir().getParent());
                        break;
                    case "new":
                    case "create":
                    case "make":
                        if (correctArg(nextWord)) {
                            try {
                                temp.set(target.getDir().resolve(nextWord));
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
                    case "go":
                    case "goto":
                    case "path":
                        if (correctArg(spell.get(word + 1))) {
                            try {
                                temp.set(nextWord);
                            } catch (InvalidPathException e) {
                                System.out.println("You used invalid characters!");
                                continue;
                            }
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
                        if (thisWord.charAt(0) == '\\' || thisWord.charAt(0) == '/') {
                            thisWord = thisWord.substring(1);
                        }
                        try {
                            temp.set(target.getDir().resolve(thisWord));
                        } catch (InvalidPathException e) {
                            System.out.println("You used invalid characters!");
                            continue;
                        }
                        if (pathExists(temp.get())) {
                            target.set(temp.get());
                        }
                }
            }
        }

    }

}
