package com.example.springai.demo.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AIServiceTest {
    
    @BeforeAll
    static void setup() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        System.setProperty("OPENAI_API_KEY", dotenv.get("OPENAI_API_KEY"));
    }
    
    @Autowired
    private AiService aiService;
    private RAGService ragService;
    @Test
    public void getMessage(){
        String message = aiService.chat("sun");
        System.out.println(message);
    }

    @Test
    public void testEmbedText(){
        float[] embed = aiService.getEmbedding("This is a big text!");
        System.out.println(embed.length);
        for(float e: embed) System.out.print(e + " ");
    }

    @Test
    public void storeTextInVectorDB(){
        aiService.ingestDataToVectorStore();
    }


    @Test
    public void getSimilarDocuments(){
        List<Document> similarDocuments = aiService.similaritySearch("Interstellar");
        for(Document document: similarDocuments){
            System.out.println(document);
        }
    }
}
