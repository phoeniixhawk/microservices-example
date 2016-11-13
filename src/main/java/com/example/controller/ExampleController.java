package com.example.controller;

import com.example.service.ExampleService;
import com.example.ExampleMessageResource;
import com.example.domain.ExampleRequest;
import com.example.domain.ExampleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExampleController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ExampleService processor;

    @Autowired
    private ExampleMessageResource exampleMessageResource;

    @RequestMapping("/")
    public String index() {
        return exampleMessageResource.getMessage("application.welcome");
    }

    @RequestMapping(value = "/exampleParam", method = RequestMethod.GET)
    public ExampleResponse exampleParam(@RequestParam(value="name", defaultValue="World") String name) {
        ExampleResponse response = null;
        try {
            response = processor.process(name);
        } catch (Exception e) {
            logger.error("Unable to process Example Request", e);
        }
        return response;
    }

    @RequestMapping(value = "/exampleBody", method = RequestMethod.POST)
    public ExampleResponse exampleBody(@RequestBody ExampleRequest request) {
        ExampleResponse response = null;
        try {
            response = processor.process(request);
        } catch (Exception e) {
            logger.error("Unable to process Example Request", e);
        }
        return response;
    }
    
}
