package com.example.springai.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RAGService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:os.pdf")
    Resource pdfFile;

    public String askAi(String prompt){
        String template = """
                You are an ai assistant helping a developer.
                Rules:
                - Use ONLY the provided context to answer the question.
                - You may rephrase, summarize and explain in natural language.
                - Do NOT introduce new concepts or facts
                - If multiple context sections are relevant, combine them.
                - If the answer is not in the context, say "I don't know."
                
                Context:
                {context}
                
                Answer in a friendly and concise manner.
                """;
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(prompt)
                        .topK(4)
                        .similarityThreshold(0.4)
                        .filterExpression("file_name == 'os.pdf'")
                        .build()
        );
        String context = "";
        for(Document document : documents){
            context += document.getText() + "\n";
        }
        PromptTemplate promptTemplate = new PromptTemplate(template);
        String systemPrompt = promptTemplate.render(
                Map.of("context", context)
        );
        return chatClient.prompt(systemPrompt)
                .system(systemPrompt)
                .user(prompt)
                .advisors(new SimpleLoggerAdvisor())
                .call()
                .content();
    }

    public void ingestPdfToVectorStore(){
        PagePdfDocumentReader reader = new PagePdfDocumentReader(pdfFile);
        List<Document> pages = reader.get();
        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder()
                        .withChunkSize(200)
                                .build();
        List<Document> chunks = tokenTextSplitter.apply(pages);
        vectorStore.add(chunks);
    }
}
