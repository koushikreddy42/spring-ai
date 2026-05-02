package com.example.springai.demo.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RAGServiceTest {
    @BeforeAll
    static void setup() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        System.setProperty("OPENAI_API_KEY", dotenv.get("OPENAI_API_KEY"));
    }

    @Autowired
    private AiService aiService;
    
    @Autowired
    private RAGService ragService;

    @Test
    public void testIngest(){
        ragService.ingestPdfToVectorStore();
    }

    @Test
    public void testAskAi(){
        String message = ragService.askAi("what is os and pagination");
        System.out.println(message);
    }
}
