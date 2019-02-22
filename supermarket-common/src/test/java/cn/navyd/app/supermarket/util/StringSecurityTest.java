package cn.navyd.app.supermarket.util;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.junit.Test;

import cn.navyd.app.supermarket.util.StringUtils;

public class StringSecurityTest {
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;
    
//    @Test
    public void encryptTest() {
        String s = "1234";
        String ss = StringUtils.encryptByMd5(s);
        System.err.println(ss.length());
    }
    @Test
    public void generator() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024, new SecureRandom());
            KeyPair keyPair= keyPairGen.generateKeyPair();
            this.privateKey= (RSAPrivateKey) keyPair.getPrivate();
            this.publicKey= (RSAPublicKey) keyPair.getPublic();
            
            System.err.println("pri: " + privateKey);
            System.err.println("publicKey: " + publicKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    
    public void key() {
        File file = new File("/home/navyd/Desktop/test/test.key");
        String alias = "myprivatekey";
        try {
            KeyStore keyStore = KeyStore.getInstance(file, getPassword());
            Key key = keyStore.getKey(alias, getPassword());
            System.err.println("key " + key.getClass().getName());
            KeyStore.ProtectionParameter protParam =
                    new KeyStore.PasswordProtection(getPassword());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }
    
    char[] getPassword() {
        return "1234".toCharArray();
    }
}
