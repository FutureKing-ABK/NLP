# NLP Processing with Spring Boot and Stanford NLP

This project demonstrates a **Spring Boot REST API** that performs **Natural Language Processing (NLP)** tasks using the **Stanford NLP library**. The API processes user input, extracting linguistic features such as tokens, parts of speech (POS) tags, and named entities (NER).

## Overview

The `NLPController` class exposes an endpoint for processing text commands via a REST API. It uses **StanfordCoreNLP** to apply various NLP tasks such as tokenization, lemmatization, and named entity recognition. The results are returned in JSON format.

## Features

- **Tokenization**: Splits text into individual words (tokens).
- **Part-of-Speech (POS) Tagging**: Identifies the grammatical role of each token (e.g., noun, verb).
- **Lemmatization**: Converts words to their root form (e.g., "running" becomes "run").
- **Named Entity Recognition (NER)**: Detects named entities such as people, organizations, locations, and numerical values.

## How It Works

### 1. **Stanford NLP Pipeline Initialization**

When the `NLPController` is instantiated, it sets up a **Stanford NLP pipeline** with the necessary annotators:

```java
Properties props = new Properties();
props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse");
this.pipeline = new StanfordCoreNLP(props);
```

### 2. **API Endpoint**

The API exposes a single POST endpoint, `/process-command`, which accepts a JSON payload containing the command to be processed:

```java
@PostMapping("/process-command")
public ResponseEntity<Map<String, Object>> processCommand(@RequestBody Map<String, String> request) {
    String command = request.get("command");
```

### 3. **Text Processing**

The input text (from the `"command"` key) is processed using the Stanford NLP pipeline, which extracts various linguistic features:

```java
CoreDocument document = new CoreDocument(command);
pipeline.annotate(document);
```

### 4. **Extracting NLP Features**

After the text is processed, the following features are extracted:
- **Tokens**: Individual words from the input text.
- **POS Tags**: Part-of-speech tags for each token.
- **Named Entities**: Named entities like people, places, and numbers.

```java
List<String> tokens = document.tokens().stream()
    .map(CoreLabel::word)
    .collect(Collectors.toList());

List<String> posTags = document.tokens().stream()
    .map(CoreLabel::tag)
    .collect(Collectors.toList());

List<String> namedEntities = document.tokens().stream()
    .map(token -> token.word() + " (" + token.ner() + ")")
    .collect(Collectors.toList());
```

### 5. **Response Structure**

The extracted NLP features (tokens, POS tags, and named entities) are packaged into a JSON response:

```java
Map<String, Object> response = new HashMap<>();
response.put("tokens", tokens);
response.put("posTags", posTags);
response.put("namedEntities", namedEntities);

return ResponseEntity.ok(response);
```

## Example Workflow

### Input:
A user sends a POST request to `/api/nlp/process-command` with a JSON payload:

```json
{
    "command": "Create a bar chart for sales data in column B"
}
```

### Processing:
The Stanford NLP pipeline processes the text and extracts tokens, parts of speech, and named entities.

### Output:
The API returns a JSON response containing:
- **Tokens**: Words in the text.
- **POS Tags**: Part-of-speech tags for each word.
- **Named Entities**: Identified entities (e.g., numbers, organizations).

### Example Response:
```json
{
    "tokens": ["Create", "a", "bar", "chart", "for", "sales", "data", "in", "column", "B"],
    "posTags": ["VB", "DT", "NN", "NN", "IN", "NNS", "NN", "IN", "NN", "NNP"],
    "namedEntities": ["Create (O)", "a (O)", "bar (O)", "chart (O)", "for (O)", "sales (O)", "data (O)", "in (O)", "column (O)", "B (NUMBER)"]
}
```

## Use Cases

This API can be used for:
- **Natural language understanding**: Extracting linguistic features from text for further processing.
- **Automation**: Converting user commands into structured data that can be used to generate macros or other automated actions.

## Getting Started

To run this application:
1. Clone the repository.
2. Ensure you have **Java 8+** and **Spring Boot** installed.
3. Run the application with `./gradlew bootRun` or via your IDE.
4. Use Postman or Curl to send POST requests to the `/process-command` endpoint.

## Conclusion

This project demonstrates how to integrate **Stanford NLP** with **Spring Boot** to perform basic NLP tasks via a REST API. The extracted linguistic features can be leveraged for various use cases, such as automating tasks or transforming natural language commands into structured data.
