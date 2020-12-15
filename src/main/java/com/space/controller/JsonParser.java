package com.space.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.space.model.Ship;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JsonParser {
    ObjectMapper mapper = new ObjectMapper();
}
