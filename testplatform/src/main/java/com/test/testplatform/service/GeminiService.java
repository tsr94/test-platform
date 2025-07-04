package com.test.testplatform.service;


import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.testplatform.dto.ParsedQuestion;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Service
public class GeminiService {

    

	Client client = Client.builder().apiKey("AIzaSyAuy44yKOeP1kiCWUva920GGzfI4o0WWAw").build();
    private final Gson gson = new Gson();

//    @PostConstruct
//    public void init() {
//        String key = System.getenv("GEMINI_API_KEY");
//        System.out.println("✅ Detected GOOGLE_API_KEY: " + (key != null ? "SET" : "NOT SET"));
//        client = new Client();
//    }

    public List<ParsedQuestion> parseQuestions(String messyText) {
        try {
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-1.5-pro",
                    "Parse the following into JSON array. Each object should have: 'question', 'options' (a list of 4), and 'answer'.\n\n" + messyText,
                    null
            );

            String json = response.text();
            Type listType = new TypeToken<List<ParsedQuestion>>() {}.getType();
            return gson.fromJson(json, listType);
        } catch (Exception e) {
            System.err.println("Gemini parsing failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
