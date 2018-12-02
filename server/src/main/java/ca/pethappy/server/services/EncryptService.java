package ca.pethappy.server.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Service
public class EncryptService {
//    @Value("${cards.key}")
//    private String cardsKey;
//
//    public String encrypt(String initVector, String value) {
//        try {
//            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
//            SecretKeySpec skeySpec = new SecretKeySpec(cardsKey.getBytes(StandardCharsets.UTF_8), "AES");
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
//
//            byte[] encrypted = cipher.doFinal(value.getBytes());
//            return java.util.Base64.getEncoder().encodeToString(encrypted);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return null;
//    }
//
//    public String decrypt(String initVector, String encrypted) {
//        try {
//            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
//            SecretKeySpec skeySpec = new SecretKeySpec(cardsKey.getBytes(StandardCharsets.UTF_8), "AES");
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
//
//            byte[] original = cipher.doFinal(java.util.Base64.getDecoder().decode(encrypted));
//            return new String(original);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return null;
//    }
}
