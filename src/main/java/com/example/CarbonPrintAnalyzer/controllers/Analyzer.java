package com.example.CarbonPrintAnalyzer.controllers;

import com.example.CarbonPrintAnalyzer.model.Dish;
import com.example.CarbonPrintAnalyzer.model.Response;
import com.example.CarbonPrintAnalyzer.service.AnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("estimate")
@CrossOrigin
public class Analyzer {
    @Autowired
    AnalyzerService analyzerService;

    @PostMapping("")
    public ResponseEntity<?> textAnalyzer(@RequestBody Dish dish) throws Exception {
        Response response  = analyzerService.analyzeText(dish.getDish());
        if(response.getError()!=null){
            return new ResponseEntity<>(Map.of("error",response.getError()), HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/image")
    public ResponseEntity<?> imageAnalyzer(@RequestPart MultipartFile file) throws Exception {
        Response response = analyzerService.analyzeImage(file);
        if(response.getError()!=null){
            return new ResponseEntity<>(Map.of("error",response.getError()), HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
