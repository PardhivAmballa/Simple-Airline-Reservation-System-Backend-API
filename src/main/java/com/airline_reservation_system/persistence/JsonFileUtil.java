package com.airline_reservation_system.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JsonFileUtil {
    // ObjectMapper instance for JSON processing
    private static final ObjectMapper mapper = new ObjectMapper();

    // Generic method to read data from a JSON file and convert it to a list of objects of type T
    public static <T> List<T> readData(String filePath, TypeReference<List<T>> typeRef) {
        try {
            File file = new File("src/main/resources/"+filePath);
            if(!file.exists()){
                return Collections.emptyList();
            }
            return mapper.readValue(file, typeRef);
        }
        catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Generic method to write a list of objects of type T to a JSON file
    public static <T> void writeData(String filePath, List<T> data){
        try{
            File file = new File("src/main/resources/"+filePath);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}