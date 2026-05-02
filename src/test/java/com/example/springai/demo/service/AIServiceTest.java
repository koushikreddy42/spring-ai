package com.example.springai.demo.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AIServiceTest {
    
    @BeforeAll
    static void setup() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        System.setProperty("OPENAI_API_KEY", dotenv.get("OPENAI_API_KEY"));
    }
    
    @Autowired
    private AiService aiService;
    @Test
    public void getMessage(){
        String message = aiService.chat("sun");
        System.out.println(message);
    }
}
