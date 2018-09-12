package localhost;

import java.util.ArrayList;

class Utils {
    static ArrayList<String> findArgs(String[] spell) {
        ArrayList<String> list = new ArrayList<>();
        StringBuilder path = new StringBuilder();
        for(int word = 0; word < spell.length; ++word) {
            if (path.length() > 1) {
                if (spell[word].endsWith("\"")) {
                    path.append(" ").append(spell[word], 0, spell[word].length() - 1);
                    list.add(path.toString());
                    path = new StringBuilder();
                } else path.append(" ").append(spell[word]);
            } else if (spell[word].startsWith("\"")) {
                path.append(spell[word].substring(1));
                if(spell[word].endsWith("\"")){
                    list.add(path.toString().substring(0, path.length() - 1));
                    path = new StringBuilder();
                }
            } else {
                list.add(spell[word]);
            }
        }
        if(path.length() > 1){
            System.out.println("Incorrect spell! Number quotemarks must be even.");
        }
        list.add(null);
        return list;
    }

}
