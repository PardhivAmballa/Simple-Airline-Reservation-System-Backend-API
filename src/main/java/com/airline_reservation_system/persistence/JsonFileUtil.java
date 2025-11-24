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
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> List<T> readData(String filePath, TypeReference<List<T>> typeRef) {
        try {
            File file = new File("src/main/resources/"+filePath);
            if(!file.exists()){
                return Collections.emptyList();
            }
            // Uses 'src/main/resources/' path
            return mapper.readValue(file, typeRef);
        }
        catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static <T> void writeData(String filePath, List<T> data){
        try{
            // 💡 CRITICAL FIX APPLIED HERE: Must use the same full path for writing!
            File file = new File("src/main/resources/"+filePath);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}