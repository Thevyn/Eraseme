/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import no.esito.anonymizer.Log;

public class FileUtils {

    private static final String ENCRYPTION_TYPE = "Blowfish/ECB/PKCS5Padding";

    private static final String ENCRYPTION_ENGINE = "Blowfish";

    public static InputStream getResourceAsStream(String file) throws IOException {
        InputStream input = FileUtils.class.getClassLoader().getResourceAsStream(file);
        if (input == null) {
            throw new FileNotFoundException (file + " (File not found in classpath)");
        }
        return input;
    }

    public static String encrypt(String strClearText, String strKey) throws Exception {
        String strData;
        try {
            SecretKeySpec skeyspec = new SecretKeySpec(strKey.getBytes(), ENCRYPTION_ENGINE);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
            byte[] encrypted = cipher.doFinal(strClearText.getBytes());
            strData = new String(Base64.getEncoder().encode(encrypted));
        } catch (Exception e) {
            Log.error(e);
            throw new Exception(e);
        }
        return strData;
    }

    public static String decrypt(String strEncrypted, String strKey) throws Exception {
        String strData = "";
        try {
            SecretKeySpec skeyspec = new SecretKeySpec(strKey.getBytes(), ENCRYPTION_ENGINE);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, skeyspec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(strEncrypted.getBytes()));
            strData = new String(decrypted);

        } catch (Exception e) {
            Log.error(e);
            throw new Exception(e);
        }
        return strData;
    }

    public static String stream2String(InputStream is) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    public static InputStream string2stream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    public static void string2File(String out, File file) {
        try {
            FileWriter os = new FileWriter(file);
            os.write(out);
            os.close();
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public static void main(String[] args) {
        try {
            String in = "Little red fox jumps into the water trying to save the turtle.";
            String key = "12345";
            String encrypted = encrypt(in, key);
            String out = decrypt(encrypted, key);
            System.out.println("in:" + in);
            System.out.println("key:" + key);
            System.out.println("xx:" + encrypted);
            System.out.println("out:" + out);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
