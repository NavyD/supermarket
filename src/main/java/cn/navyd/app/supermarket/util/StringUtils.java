package cn.navyd.app.supermarket.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.Getter;


public class StringUtils {
    private static final StringBuilder SB = new StringBuilder();
    private static final Security SECURITY = new Security();
   
    /**
     * 将字节转化为16进制字符串
     * @param b
     * @return
     */
    public static String byteToHexString(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2)
            SB.append('0');
        SB.append(hex);
        String result = SB.toString();
        clearStringBuilder();
        return result;
    }
    
    /**
     * 将字节数组转化为16进制的字符串
     * @param bytes
     * @return
     */
    public static String byteToHexString(byte[] bytes) {
        for (byte b : bytes) {
            // 由于java byte最大为127，使用0xFF转化为int保留最高符号位
            String hex = Integer.toHexString(b & 0xFF);
            // 如果只有一个 使用0填充
            if (hex.length() < 2)
                SB.append('0');
            SB.append(hex);
        }
        String result = SB.toString();
        clearStringBuilder();
        return result;
    }
    
    public static String encryptByMd5(String s) {
        SECURITY.setAlgorithm(MessageDigestAlgorithmEnum.MD5);
        return SECURITY.encrypt(s);
    }
    
    private static void clearStringBuilder() {
        SB.delete(0, SB.length());
    }
    
    private static class Security {
        private MessageDigest md;
        private MessageDigestAlgorithmEnum algorithm;

        public void setAlgorithm(MessageDigestAlgorithmEnum algorithm) {
            this.algorithm = algorithm;
        }

        public String encrypt(String s) {
            check();
            md.update(s.getBytes());
            byte[] bytes = md.digest();
            md.reset();
            return byteToHexString(bytes);
        }

        private void check() {
            if (algorithm == null)
                throw new NullPointerException();
            if (md == null || !md.getAlgorithm().equals(algorithm.getAlgorithm())) {
                try {
                    md = MessageDigest.getInstance(algorithm.getAlgorithm());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public enum MessageDigestAlgorithmEnum {
        // 32位字符 16进制
        MD5("MD5"),
        // 40位字符 16进制
        SHA1("SHA-1"),
        // 64位字符 16进制
        SHA256("SHA-256");
        @Getter private final String algorithm;
        
        MessageDigestAlgorithmEnum(String algorithm) {
            this.algorithm = algorithm;
        }
    }
}

