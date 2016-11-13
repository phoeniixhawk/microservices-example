package com.example.config;

import com.example.ExampleApplication;
import com.example.ExampleMessageResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ExampleApplication.class, ExampleConfig.class})
public class ExampleMessageResourceTest {
    @Autowired
    private ExampleMessageResource exampleMessageResource;

    @Test
    public void testGetMessage() throws Exception {
        String expected = "Hello";
        String actual = exampleMessageResource.getMessage("application.greeting");
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageForLocale() throws Exception {
        String expected = "Bonjour";
        String actual = exampleMessageResource.getMessageForLocale("application.greeting", Locale.FRENCH);
        assertEquals(expected, actual);
        expected = "Hola";
        actual = exampleMessageResource.getMessageForLocale("application.greeting", Locale.forLanguageTag("es"));
        assertEquals(expected, actual);
        expected = "Bienvenue Ã  Exemple d'application!";
        actual = exampleMessageResource.getMessageForLocale("application.welcome", Locale.forLanguageTag("fr"));
        assertEquals(expected, actual);
    }
}