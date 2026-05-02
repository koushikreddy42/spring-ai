package com.example.springai.demo.service;

import com.example.springai.demo.dto.Joke;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;
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
