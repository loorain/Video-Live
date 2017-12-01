package com.loorain.live;

import com.loorain.live.common.utils.CipherUtil;

import org.junit.Test;

import java.security.SecureRandom;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String info = "mima7412369";
        byte[] iv = genIV();

        byte[] enData = new byte[0];
        try {
            byte[] pwdByte = "TR%an%^&1^G4LUCK".getBytes("utf-8");
            for (int i = 0; i < pwdByte.length; i++) {
                pwdByte[i] = (byte) (0xf1 & pwdByte[i]);
            }
            enData = CipherUtil.encryptAES(info.getBytes("utf-8"),
                    pwdByte, iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String value = CipherUtil.byte2hex(iv) + CipherUtil.byte2hex(enData);
        System.out.println(value);
    }
    private byte[] genIV() {
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }

}