package com.airline_reservation_system.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.airline_reservation_system.util.LogUtil;
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
        LogUtil.system("READ_LOCK acquired on " + filePath);
        try {
            File file = new File("src/main/resources/"+filePath);
            if(!file.exists()){
                LogUtil.error("File not found (read): " + filePath);
                return Collections.emptyList();
            }
            return mapper.readValue(file, typeRef);
        }
        catch(IOException e){
            e.printStackTrace();
            LogUtil.error("Failed reading file " + filePath + ": " + e.getMessage());
            return Collections.emptyList();
        }
        finally{
            // Ensure that the lock is released after reading
            lock.readLock().unlock();
            LogUtil.system("READ_LOCK released on " + filePath);
        }
    }

    // Generic method to write a list of objects of type T to a JSON file
    public <T> void writeData(String filePath, List<T> data){
        lock.writeLock().lock();
        LogUtil.system("WRITE_LOCK acquired on " + filePath);
        try{
            File file = new File("src/main/resources/"+filePath);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
            LogUtil.system("Successfully wrote to " + filePath);
        }
        catch (IOException e) {
            e.printStackTrace();
            LogUtil.error("Failed writing file " + filePath + ": " + e.getMessage());
        }
        finally {
            lock.writeLock().unlock();
            LogUtil.system("WRITE_LOCK released on " + filePath);
        }
    }
}