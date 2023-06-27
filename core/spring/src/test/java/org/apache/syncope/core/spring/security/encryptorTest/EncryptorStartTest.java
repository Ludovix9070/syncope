package org.apache.syncope.core.spring.security.encryptorTest;

import junit.framework.TestCase;
import org.apache.syncope.core.spring.ApplicationContextProvider;
import org.apache.syncope.core.spring.security.SecurityProperties;
import org.junit.BeforeClass;
import org.mockito.Mockito;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Random;

import static org.mockito.Mockito.when;

public abstract class EncryptorStartTest{

    @BeforeClass
    public static void executeMocks(){

        SecurityProperties myProperties = new SecurityProperties();
        ConfigurableApplicationContext myContext = Mockito.mock(ConfigurableApplicationContext.class);
        when(myContext.getBean(SecurityProperties.class)).thenReturn(myProperties);
        ApplicationContextProvider.setApplicationContext(myContext);

    }

    public static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder((int)length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

}
