package com.example.springai.demo.controllers;

import com.example.springai.demo.tool.FlightBookingTools;
import com.example.springai.demo.tool.TravellingTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatClient chatClient;
    private final TravellingTools travellingTools;
    private final FlightBookingTools flightBookingTools;
    private final ChatMemory chatMemory;

    @PostMapping("/chat")
    public String chat(@RequestBody String prompt, @RequestParam String userId){
        String systemPrompt = String.format("""
                The current user's Id id "%s".
                When calling tools requiring a userId, use this EXACT value.
                """, userId);
        return chatClient.prompt()
                .user(prompt)
                .system(systemPrompt)
                .advisors(
                        MessageChatMemoryAdvisor.builder(chatMemory)
                                .conversationId(userId)
                                .build()
                )
                .tools(travellingTools, flightBookingTools)
                .call()
                .content();
    }
}
