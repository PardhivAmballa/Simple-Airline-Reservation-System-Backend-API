package com.airline_reservation_system.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class JsonFileUtil {
    // ObjectMapper instance for JSON processing
    private final ObjectMapper mapper = new ObjectMapper();

    // ReentrantReadWriteLock to handle concurrent read/write operations
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    // Generic method to read data from a JSON file and convert it to a list of objects of type T
    public <T> List<T> readData(String filePath, TypeReference<List<T>> typeRef) {
        // Ensure that the read lock is acquired before reading
        lock.readLock().lock();
        try {
            File file = new File("src/main/resources/"+filePath);
            if(!file.exists()){
                return Collections.emptyList();
            }
            return mapper.readValue(file, typeRef);
        }
        catch(IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
        finally{
            // Ensure that the lock is released after reading
            lock.readLock().unlock();
        }
    }

    // Generic method to write a list of objects of type T to a JSON file
    public <T> void writeData(String filePath, List<T> data){
        lock.writeLock().lock();
        try{
            File file = new File("src/main/resources/"+filePath);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            lock.writeLock().unlock();
        }
    }
}