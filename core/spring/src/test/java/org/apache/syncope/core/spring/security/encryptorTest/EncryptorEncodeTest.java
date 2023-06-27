package org.apache.syncope.core.spring.security.encryptorTest;

import junit.framework.TestCase;
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

import static org.junit.Assert.assertEquals;

/*public String encode(final String value, final CipherAlgorithm cipherAlgorithm)*/



@RunWith(value = Parameterized.class)
public class EncryptorEncodeTest extends EncryptorStartTest {

    private String value;
    private CipherAlgorithm cipherAlgorithm;
    private Object expected;
    private Encryptor encryptor;
    private static String filledString = generateRandomString(130);
    private static String emptyString = "";

    public EncryptorEncodeTest(String value, CipherAlgorithm cipherAlgorithm, Object expected){
        configure(value, cipherAlgorithm,expected);
    }

    private void configure(String value, CipherAlgorithm cipherAlgorithm, Object expected){
        this.value = value;
        this.cipherAlgorithm = cipherAlgorithm;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection parameters() throws Exception {
        return Arrays.asList(new Object[][] {

                //value, cipherAlgorithm, expected
                //tests based on multidimensional analysis
                {null,CipherAlgorithm.SHA, null},
                {null,CipherAlgorithm.SHA1, null},
                {null,CipherAlgorithm.SHA256, null},
                {null,CipherAlgorithm.SHA512, null},
                {null,CipherAlgorithm.AES, null},
                {null,CipherAlgorithm.SMD5, null},
                {null,CipherAlgorithm.SSHA, null},
                {null,CipherAlgorithm.SSHA1, null},
                {null,CipherAlgorithm.SSHA256, null},
                {null,CipherAlgorithm.SSHA512, null},
                {null,CipherAlgorithm.BCRYPT, null},
                {null,null, null},
                {emptyString,CipherAlgorithm.SHA, null},
                {emptyString,CipherAlgorithm.SHA1, null},
                {emptyString,CipherAlgorithm.SHA256, null},
                {emptyString,CipherAlgorithm.SHA512, null},
                {emptyString,CipherAlgorithm.AES, null},
                {emptyString,CipherAlgorithm.SMD5, null},
                {emptyString,CipherAlgorithm.SSHA, null},
                {emptyString,CipherAlgorithm.SSHA1, null},
                {emptyString,CipherAlgorithm.SSHA256, null},
                {emptyString,CipherAlgorithm.SSHA512, null},
                {emptyString,CipherAlgorithm.BCRYPT, null},
                {emptyString,null, null},
                {filledString,CipherAlgorithm.SHA, null},
                {filledString,CipherAlgorithm.SHA1, null},
                {filledString,CipherAlgorithm.SHA256, null},
                {filledString,CipherAlgorithm.SHA512, null},
                {filledString,CipherAlgorithm.AES, null},
                {filledString,CipherAlgorithm.SMD5, null},
                {filledString,CipherAlgorithm.SSHA, null},
                {filledString,CipherAlgorithm.SSHA1, null},
                {filledString,CipherAlgorithm.SSHA256, null},
                {filledString,CipherAlgorithm.SSHA512, null},
                {filledString,CipherAlgorithm.BCRYPT, null},
                {filledString,null, null},


        });
    }

    @Before
    public void setUp(){
        this.encryptor = Encryptor.getInstance();
    }


    @Test
    public void testEncode() {
        try {
            String encodedString = encryptor.encode(this.value, this.cipherAlgorithm);
            if (value == null) {
                Assert.assertNull(expected);
            } else {
                Assert.assertTrue(encryptor.verify(value, cipherAlgorithm, encodedString));
            }
        } catch (Exception e) {
            assertEquals(expected,e.getClass());
        }
    }
}
