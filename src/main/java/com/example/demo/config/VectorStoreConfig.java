package com.example.demo.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;

@Configuration
public class VectorStoreConfig {

	@Value("classpath:wikipedia-hurricane-milton-page.pdf")
	Resource hurricaneDocs;

	// Lazy so the PDF ingestion (and the embedding model it triggers) only happens on
	// the first request that needs it, instead of slowing down app startup.
	@Bean
	@Lazy
	VectorStore vectorStore(EmbeddingModel embeddingModel) {
		VectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
		vectorStore.add(new TokenTextSplitter().split(new PagePdfDocumentReader(hurricaneDocs).read()));
		return vectorStore;
	}

}
