package com.sauyee333.visatest1.utils;

import android.util.Log;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by sauyee on 25/10/16.
 */

public class XPayTokenGenerator {

    public static String generateXpaytoken(String resourcePath, String queryString, String requestBody, String secret) throws SignatureException {
        String timestamp = timeStamp();
        String beforeHash = timestamp + resourcePath + queryString + requestBody;
        String hash = hmacSha256Digest(beforeHash, secret);
        String token = "xv2:" + timestamp + ":" + hash;
        return token;
    }


    private static String timeStamp() {
        return String.valueOf(System.currentTimeMillis()/ 1000L);
    }

    private static String hmacSha256Digest(String data, String secret)
            throws SignatureException {
        String ret = getDigest("HmacSHA256", secret, data, true);
        return ret;
    }


    private static String getDigest(String algorithm, String sharedSecret, String data,
                                    boolean toLower) throws SignatureException {
        try {
            Mac sha256HMAC = Mac.getInstance(algorithm);
            SecretKeySpec secretKey = new SecretKeySpec(sharedSecret.getBytes(StandardCharsets.UTF_8), algorithm);
            sha256HMAC.init(secretKey);
            byte[] hashByte = sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String hashString = toHex(hashByte);
            return toLower ? hashString.toLowerCase() : hashString;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SignatureException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
}
