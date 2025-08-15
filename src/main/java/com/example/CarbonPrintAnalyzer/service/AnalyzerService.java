package com.example.CarbonPrintAnalyzer.service;

import com.example.CarbonPrintAnalyzer.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
@Service
public class AnalyzerService {

    @Autowired
    GeminiService geminiService;

    public Response analyzeImage(MultipartFile file) throws Exception {


    Response response = geminiService.analyzeMultipartImage(file);

        return response;
    }

    public Response analyzeText(String text) throws Exception {
        Response response = geminiService.analyzeTextOnly(text);
        return response;
    }
}
