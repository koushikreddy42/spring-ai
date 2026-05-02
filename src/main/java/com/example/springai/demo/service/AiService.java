package com.example.springai.demo.service;

import com.example.springai.demo.dto.Joke;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    public float[] getEmbedding(String text) {
        return embeddingModel.embed(text);
    }

    public static List<Document> springAiDocs() {
        return List.of(

                new Document(
                        "Spring AI helps Java developers integrate AI models into applications.",
                        Map.of(
                                "topic", "spring-ai",
                                "level", "beginner"
                        )
                ),

                new Document(
                        "Embeddings convert text into vectors for semantic similarity search.",
                        Map.of(
                                "topic", "embeddings",
                                "level", "beginner"
                        )
                ),

                new Document(
                        "Vector databases store embeddings and enable fast similarity search.",
                        Map.of(
                                "topic", "vector-db",
                                "level", "intermediate"
                        )
                ),

                new Document(
                        "RAG combines vector search with LLMs to generate accurate responses.",
                        Map.of(
                                "topic", "rag",
                                "level", "intermediate"
                        )
                ),

                new Document(
                        "Chunking breaks large text into smaller pieces for better retrieval.",
                        Map.of(
                                "topic", "chunking",
                                "level", "intermediate"
                        )
                )

        );
    }

    public void ingestDataToVectorStore() {
//        Document document = new Document(text);
        List<Document> movies = List.of(

                new Document(
                        "Inception is a sci-fi movie where a thief enters people's dreams to steal secrets.",
                        Map.of(
                                "title", "Inception",
                                "genre", "Sci-Fi",
                                "year", "2010",
                                "rating", "8.8"
                        )
                ),

                new Document(
                        "Interstellar follows astronauts traveling through a wormhole to find a new home for humanity.",
                        Map.of(
                                "title", "Interstellar",
                                "genre", "Sci-Fi",
                                "year", "2014",
                                "rating", "8.6"
                        )
                ),

                new Document(
                        "The Dark Knight shows Batman fighting the Joker to save Gotham City.",
                        Map.of(
                                "title", "The Dark Knight",
                                "genre", "Action",
                                "year", "2008",
                                "rating", "9.0"
                        )
                ),

                new Document(
                        "Avengers Endgame is about superheroes coming together to defeat Thanos and restore the universe.",
                        Map.of(
                                "title", "Avengers Endgame",
                                "genre", "Superhero",
                                "year", "2019",
                                "rating", "8.4"
                        )
                )

        );
        vectorStore.add(movies);
        vectorStore.add(springAiDocs());
    }

    List<Document> similaritySearch(String query){
        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(3)
                        .similarityThreshold(0.4)
                        .build()
        );
    }

    public String chat(String topic) {
        String systemPrompt = """
            You are a sarcastic joker, make poetic joke in 4 lines
            Give a joke on topic: {topic}
            """;
        PromptTemplate promptTemplate = new PromptTemplate(systemPrompt);
        String renderedText = promptTemplate.render(Map.of("topic", topic));
        var response = chatClient.prompt()
//                .system("Give response in two lines")
                .user(renderedText)
                .advisors(
                        new SimpleLoggerAdvisor()
                )
                .call()
//                .content();
//                .chatClientResponse();
                .entity(Joke.class);
//        return response.chatResponse().getResult().getOutput().getText();
        return response.text();
    }
}
