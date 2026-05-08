package com.example.springai.demo.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class TravellingTools {

    @Tool(description = "Get the weather for the given location")
    public String getWeather(@ToolParam(description = "location for which we get the weather") String location){
        return switch (location) {
            case "Delhi" -> "Sunny, 30 degree celsius";
            case "Vizag" -> "Rainy, 15 degree celsius";
            default -> "Unknown location";
        };
    }
}
