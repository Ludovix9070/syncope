package org.apache.syncope.core.spring.security.encryptorTest;

import org.apache.syncope.common.lib.types.CipherAlgorithm;
import org.apache.syncope.core.spring.security.Encryptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/*public boolean verify(final String value, final CipherAlgorithm cipherAlgorithm,String encoded)*/



@RunWith(value = Parameterized.class)
public class EncryptorVerifyTest extends EncryptorStartTest {

    private String value;
    private boolean wellEncoded;
    private CipherAlgorithm cipherAlgorithm;
    private Boolean expected;
    private String encoded;
    private Encryptor encryptor;
    private static String filledString = generateRandomString(130);
    private static String emptyString = "";

    public EncryptorVerifyTest(String value, CipherAlgorithm cipherAlgorithm, Boolean wellEncoded, Boolean expected){
        configure(value, cipherAlgorithm, wellEncoded, expected);
    }

    private void configure(String value, CipherAlgorithm cipherAlgorithm, Boolean wellEncoded, Boolean expected){
        this.value = value;
        this.cipherAlgorithm = cipherAlgorithm;
        this.wellEncoded = wellEncoded;
        this.expected = expected;

    }

    @Parameterized.Parameters
    public static Collection parameters() throws Exception {
        return Arrays.asList(new Object[][] {

                //tests based on multidimensional analysis
                {null,CipherAlgorithm.SHA, true, false},
                {null,CipherAlgorithm.SHA, false, false},
                {null,CipherAlgorithm.SHA1, true, false},
                {null,CipherAlgorithm.SHA1, false, false},
                {null,CipherAlgorithm.SHA256, true, false},
                {null,CipherAlgorithm.SHA256, false, false},
                {null,CipherAlgorithm.SHA512, true, false},
                {null,CipherAlgorithm.SHA512, false, false},
                {null,CipherAlgorithm.AES, true, false},
                {null,CipherAlgorithm.AES, false, false},
                {null,CipherAlgorithm.SMD5, true, false},
                {null,CipherAlgorithm.SMD5, false, false},
                {null,CipherAlgorithm.SSHA, true, false},
                {null,CipherAlgorithm.SSHA, false, false},
                {null,CipherAlgorithm.SSHA1, true, false},
                {null,CipherAlgorithm.SSHA1, false, false},
                {null,CipherAlgorithm.SSHA256, true, false},
                {null,CipherAlgorithm.SSHA256, false, false},
                {null,CipherAlgorithm.SSHA512, true, false},
                {null,CipherAlgorithm.SSHA512, false, false},
                {null,CipherAlgorithm.BCRYPT, true, false},
                {null,CipherAlgorithm.BCRYPT, false, false},
                {null,null, true, false},
                {null,null, false, false},
                {emptyString,CipherAlgorithm.SHA, true, true},
                {emptyString,CipherAlgorithm.SHA, false, false},
                {emptyString,CipherAlgorithm.SHA1, true, true},
                {emptyString,CipherAlgorithm.SHA1, false, false},
                {emptyString,CipherAlgorithm.SHA256, true, true},
                {emptyString,CipherAlgorithm.SHA256, false, false},
                {emptyString,CipherAlgorithm.SHA512, true, true},
                {emptyString,CipherAlgorithm.SHA512, false, false},
                {emptyString,CipherAlgorithm.AES, true, true},
                {emptyString,CipherAlgorithm.AES, false, false},
                {emptyString,CipherAlgorithm.SMD5, true, true},
                {emptyString,CipherAlgorithm.SMD5, false, false},
                {emptyString,CipherAlgorithm.SSHA, true, true},
                {emptyString,CipherAlgorithm.SSHA, false, false},
                {emptyString,CipherAlgorithm.SSHA1, true, true},
                {emptyString,CipherAlgorithm.SSHA1, false, false},
                {emptyString,CipherAlgorithm.SSHA256, true, true},
                {emptyString,CipherAlgorithm.SSHA256, false, false},
                {emptyString,CipherAlgorithm.SSHA512, true, true},
                {emptyString,CipherAlgorithm.SSHA512, false, false},
                {emptyString,CipherAlgorithm.BCRYPT, true, true},
                {emptyString,CipherAlgorithm.BCRYPT, false, false},
                {emptyString,null,true, true},
                {emptyString,null,false, false},
                {filledString,CipherAlgorithm.SHA, true, true},
                {filledString,CipherAlgorithm.SHA, false, false},
                {filledString,CipherAlgorithm.SHA1, true, true},
                {filledString,CipherAlgorithm.SHA1, false, false},
                {filledString,CipherAlgorithm.SHA256, true, true},
                {filledString,CipherAlgorithm.SHA256, false, false},
                {filledString,CipherAlgorithm.SHA512, true, true},
                {filledString,CipherAlgorithm.SHA512, false, false},
                {filledString,CipherAlgorithm.AES, true, true},
                {filledString,CipherAlgorithm.AES, false, false},
                {filledString,CipherAlgorithm.SMD5, true, true},
                {filledString,CipherAlgorithm.SMD5, false, false},
                {filledString,CipherAlgorithm.SSHA, true, true},
                {filledString,CipherAlgorithm.SSHA, false, false},
                {filledString,CipherAlgorithm.SSHA1, true, true},
                {filledString,CipherAlgorithm.SSHA1, false, false},
                {filledString,CipherAlgorithm.SSHA256, true, true},
                {filledString,CipherAlgorithm.SSHA256, false, false},
                {filledString,CipherAlgorithm.SSHA512, true, true},
                {filledString,CipherAlgorithm.SSHA512, false, false},
                {filledString,CipherAlgorithm.BCRYPT, true, true},
                {filledString,CipherAlgorithm.BCRYPT, false, false},
                {filledString,null, true, true},
                {filledString,null, false, false},


        });
    }

    @Before
    public void setUp(){
        this.encryptor = Encryptor.getInstance();
    }


    @Test
    public void testVerify() {

        if(wellEncoded){
            try{
                this.encoded = encryptor.encode(value,cipherAlgorithm);
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            this.encoded = generateRandomString(130);
        }
        try {
            Boolean result = encryptor.verify(value, cipherAlgorithm, encoded);
            Assert.assertEquals(expected, result);
        } catch (Exception e) {
            System.out.println("test failed");
        }
    }
}
