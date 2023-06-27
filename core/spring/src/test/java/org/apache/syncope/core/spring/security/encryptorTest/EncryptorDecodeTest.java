package org.apache.syncope.core.spring.security.encryptorTest;

import org.apache.syncope.common.lib.types.CipherAlgorithm;
import org.apache.syncope.core.spring.security.Encryptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/*public String decode(final String encoded, final CipherAlgorithm cipherAlgorithm)*/



@RunWith(value = Parameterized.class)
public class EncryptorDecodeTest extends EncryptorStartTest {

    private String encoded;
    private static String wellEncodedString;
    private static String badEncodedString = generateRandomString(130);
    private String stringToCheck;
    private CipherAlgorithm cipherAlgorithm;
    private Boolean wellEncoded;
    private Object expected;
    private static Encryptor encryptor;
    private static String filledString = generateRandomString(130);
    private static String emptyString = "";

    public EncryptorDecodeTest(String stringToCheck, Boolean wellEncoded, CipherAlgorithm cipherAlgorithm, Object expected){
        configure(stringToCheck, wellEncoded, cipherAlgorithm,expected);
    }

    private void configure(String stringToCheck, Boolean wellEncoded, CipherAlgorithm cipherAlgorithm, Object expected){
        this.wellEncoded = wellEncoded;
        this.stringToCheck = stringToCheck;
        this.cipherAlgorithm = cipherAlgorithm;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection parameters() throws Exception {

        return Arrays.asList(new Object[][] {

                //tests based on multidimensional analysis
                {null,true,CipherAlgorithm.AES, null},
                {null,false,CipherAlgorithm.AES, null},
                {null,true,CipherAlgorithm.SHA, null},
                {null,false,CipherAlgorithm.SHA, null},
                {null,true,null, null},
                {null,true,null, null},
                {emptyString,true,CipherAlgorithm.AES, emptyString},
                {emptyString,false,CipherAlgorithm.AES, IllegalBlockSizeException.class},
                {emptyString,true,CipherAlgorithm.SHA, null},
                {emptyString,false,CipherAlgorithm.SHA, null},
                {emptyString,true,null, null},
                {emptyString,true,null, null},
                {filledString,true,CipherAlgorithm.AES, filledString},
                {filledString,false,CipherAlgorithm.AES, IllegalBlockSizeException.class},
                {filledString,true,CipherAlgorithm.SHA, null},
                {filledString,false,CipherAlgorithm.SHA, null},
                {filledString,true,null, null},
                {filledString,true,null, null},

        });
    }

    @Before
    public void setUp(){
        this.encryptor = Encryptor.getInstance();
    }


    @Test
    public void testDecode() {

        if(wellEncoded == true || stringToCheck == null){
            try{
                this.encoded = encryptor.encode(stringToCheck,cipherAlgorithm);
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            this.encoded = generateRandomString(130);
        }

        try {
            Assert.assertEquals(expected,encryptor.decode(encoded,cipherAlgorithm));

        } catch (UnsupportedEncodingException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            Assert.assertEquals(expected,e.getClass());
        }
    }

}
