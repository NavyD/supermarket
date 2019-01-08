package cn.navyd.app.supermarket.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import lombok.NonNull;

public class SecurityUtils {
    /**
     * 返回字符串s被md5加密后的全小写加密字符
     *
     * @param s
     * @return
     */
    public final static String md5(@NonNull String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        byte[] btInput = s.getBytes();
        // 获得MD5摘要算法的 MessageDigest 对象
        MessageDigest mdInst = null;
        try {
            mdInst = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
        // 使用指定的字节更新摘要
        mdInst.update(btInput);
        // 获得密文
        byte[] md = mdInst.digest();
        // 把密文转换成十六进制的字符串形式
        int j = md.length;
        char str[] = new char[j << 1];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str).toLowerCase();
    }
    
    public final static AesCipher cipher = new AesCipher();
    
    public static byte[] encrypt(String key, String content) {
        return cipher.encrypt(key, content);
    }
    
    public static String decrypt(String key, byte[] content) {
        return cipher.decrypt(key, content);
    }
    
    public static String en(String key, String content) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_AES);
            keyGenerator.init(new SecureRandom());
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] bytes = secretKey.getEncoded();
            Key pkey = new SecretKeySpec(bytes, ALGORITHM_AES);
            Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
            cipher.init(Cipher.ENCRYPT_MODE, pkey);
            byte[] encrypt = cipher.doFinal(content.getBytes());
            String encoder = Base64.getEncoder().encodeToString(encrypt);
            System.err.println("encoder: " + encoder);
            cipher.init(Cipher.DECRYPT_MODE, pkey);
            byte[] decrypt = cipher.doFinal(Base64.getDecoder().decode(encoder));
            return new String(decrypt);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    private static final String ALGORITHM_AES = "AES";
    static class AesCipher {
        private final String ALGORITHM_AES = "AES";
        public byte[] encrypt(String key, String password){
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_AES);
                keyGenerator.init(new SecureRandom());
                SecretKey secretKey = keyGenerator.generateKey();
                byte[] bytes = secretKey.getEncoded();
                Key pkey = new SecretKeySpec(bytes, ALGORITHM_AES);
                Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
                cipher.init(Cipher.ENCRYPT_MODE, pkey);
                return cipher.doFinal(password.getBytes());
            } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                    | BadPaddingException e) {
                e.printStackTrace();
            }
            return null;
        }
        
        public String decrypt(String key, byte[] encryptBytes){
                try {
                    KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_AES); 
                    keyGenerator.init(new SecureRandom(key.getBytes()));
                    Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
                    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyGenerator.generateKey().getEncoded(), ALGORITHM_AES));
                    byte[] decoded = cipher.doFinal(encryptBytes);
                    return new String(decoded);
            } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
                        | IllegalBlockSizeException | BadPaddingException e) {
                    e.printStackTrace();
                }
            return null;
        }
        
        
        
        
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
