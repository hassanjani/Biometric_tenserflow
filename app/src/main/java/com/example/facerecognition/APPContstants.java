package com.example.facerecognition;

import android.util.Base64;
import android.util.Log;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class APPContstants {


    public static String encrypt(String key, String text){
        try
        {
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            System.err.println(new String(encrypted));
//            // decrypt the text
            String b4= Base64.encodeToString(encrypted, Base64.NO_WRAP);
            Log.d("Base64 encrypted",b4);
            Log.d("msgkey",key);
            return b4;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }



   public static String dycrypt(String key, String enc){
        try
        {
            key=key.substring(0,16);
            Log.d("cyperkey",key);
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            Log.d("cypertext",enc);

            byte[] decode = Base64.decode(enc, Base64.NO_WRAP);

//            // encrypt the text
//            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
//            byte[] encrypted = cipher.doFinal(text.getBytes());
//            System.err.println(new String(encrypted));
            // decrypt the text
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(decode));
            Log.d("cyperdec",decrypted);
            System.err.println(decrypted);
            return decrypted;
        }
        catch(Exception e)
        {
            Log.d("msg","Cached");

            e.printStackTrace();
            return null;
        }
    }

}
