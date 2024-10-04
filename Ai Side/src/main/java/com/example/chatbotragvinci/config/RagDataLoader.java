package com.example.chatbotragvinci.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.List;


@Component
public class RagDataLoader {

    @Value("classpath:/pdfs/cv.pdf")
    private Resource pdfResource;

    @Value("store-data-v5.json")
    private String storeFile;

    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        String fileStore = Path.of("src", "main", "resources", "store", storeFile).toAbsolutePath().toString();
        File file = new File(fileStore);
        SimpleVectorStore vectorStore = new SimpleVectorStore(embeddingModel);

        try {
            if (!file.exists()) {
                System.out.println("File does not exist, creating new file.");
                PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(pdfResource);
                List<Document> documents = pdfDocumentReader.get();
                TextSplitter textSplitter = new TokenTextSplitter();
                List<Document> chunks = textSplitter.split(documents);
                vectorStore.accept(chunks);
                vectorStore.save(file);
            } else {
                System.out.println("File exists, loading data.");
                vectorStore.load(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vectorStore;
    }
}

