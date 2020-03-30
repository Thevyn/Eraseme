/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.transformations;

import no.esito.anonymizer.ITransformation;

/**
 * Email transformation. <br>
 * Translates various chars as space, hyphen and underscore.
 */
public class Email implements ITransformation {

    public static final String LABEL = "Email - Translates various chars as space, hyphen and underscore";

    @Override
    public String transform(String input) {
        if (input != null) {
            StringBuilder sb = new StringBuilder();
            char[] charArray = input.toLowerCase().toCharArray();
            int alpha = 0;
            for (char c : charArray) {
                if (c == '@') {
                    if (alpha == 0) {
                        if (sb.length() == 0)
                            sb.append("email");
                        sb.append('@');
                        alpha = sb.length();
                    }
                } else if (c >= 'a' && c <= 'z') {
                    sb.append(c);
                } else if (c >= '0' && c <= '9') {
                    sb.append(c);
                } else if (c == 'æ') {
                    sb.append("ae");
                } else if (c == 'ø') {
                    sb.append("oe");
                } else if (c == 'å') {
                    sb.append("aa");
                } else if (c == '.' || c == '_' || c == '-') {
                    sb.append('.');
                } else if (c == ' ') {
                    sb.append('.');
                } else if (c >= '0' && c <= '9') {
                    sb.append(c);
                }
            }
            if (alpha == 0)
                sb.append("@mail.com");
            if (alpha == sb.length())
                sb.append("mail.com");
            String mail = sb.toString();
            while (mail.contains("..")) {
                mail = mail.replace("..",".");
            }
            return mail;
        }
        return null;
    }

}
