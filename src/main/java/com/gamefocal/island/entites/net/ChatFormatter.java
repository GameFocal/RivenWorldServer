package com.gamefocal.island.entites.net;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatFormatter {

    private HashMap<Character, String> keys = new HashMap<>();

    private String regexString;

    private Pattern pattern;

    public ChatFormatter() {
        this.keys.put('b', "*b*");
        this.keys.put('t', "*t*");
        this.keys.put('r', "*r*");
        this.keys.put('g', "*g*");
        this.keys.put('u', "*u*");
        this.keys.put('o', "*o*");


        StringBuilder r = new StringBuilder();
        r.append("\\\\&[");
        for (Character c : this.keys.keySet()) {
            r.append(c);
        }
        r.append("]*(.*?)");

        this.regexString = r.toString();
        this.pattern = Pattern.compile(this.regexString, Pattern.MULTILINE);
    }

    public String formatChatString(String chat) {
        StringBuilder builder = new StringBuilder();
        String[] parts = chat.split("\\^");

        for (String s : parts) {

            if (s.length() > 0) {
                char first = s.charAt(0);

                if (this.keys.containsKey(first)) {
                    for (int i = 0; i < s.length(); i++) {
                        char c = s.charAt(i);
                        if (this.keys.containsKey(c)) {
                            // Has it.
                            builder.append(this.keys.get(c));
                        } else {
                            break;
                        }
                    }

                    Matcher matcher = this.pattern.matcher("^" + s);
                    builder.append(matcher.replaceAll(""));
                    System.out.println("Match");
                } else {
                    System.out.println("No Match");
                    builder.append(s);
                }

                System.out.println(builder.toString());
            }
        }

        return builder.toString();
    }

}
