package com.example.nlp.controller;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.ling.CoreLabel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/nlp")
public class NLPController {

    // Stanford NLP pipeline
    private final StanfordCoreNLP pipeline;

    public NLPController() {
        // Set up Stanford NLP pipeline with the necessary annotators
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse");
        this.pipeline = new StanfordCoreNLP(props);
    }

    // Endpoint to process user input
    @PostMapping("/process-command")
    public ResponseEntity<Map<String, Object>> processCommand(@RequestBody Map<String, String> request) {
        String command = request.get("command");

        // Process the text using Stanford NLP
        CoreDocument document = new CoreDocument(command);
        pipeline.annotate(document);

        // Extract specific NLP features (e.g., parts of speech, named entities)
        List<String> tokens = document.tokens().stream()
                .map(CoreLabel::word)
                .collect(Collectors.toList());

        List<String> posTags = document.tokens().stream()
                .map(CoreLabel::tag)
                .collect(Collectors.toList());

        // Example: Extract named entities (NER)
        List<String> namedEntities = document.tokens().stream()
                .map(token -> token.word() + " (" + token.ner() + ")")
                .collect(Collectors.toList());

        // Create a response with the extracted NLP info
        Map<String, Object> response = new HashMap<>();
        response.put("tokens", tokens);
        response.put("posTags", posTags);
        response.put("namedEntities", namedEntities);

        return ResponseEntity.ok(response);
    }
}
