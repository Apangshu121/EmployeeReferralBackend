package com.accolite.EmployeeReferralBackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FileStorageService {

    private final Map<String, byte[]> inMemoryStorage = new ConcurrentHashMap<>();

    public String storeInMemory(MultipartFile file) throws IOException {
        String fileId = UUID.randomUUID().toString();
        inMemoryStorage.put(fileId, file.getBytes());
        return fileId;
    }

    public byte[] getFromMemory(String fileId) throws FileNotFoundException {
        byte[] fileBytes = inMemoryStorage.get(fileId);
        if (fileBytes != null) {
            return fileBytes;
        } else {
            throw new FileNotFoundException("File not found in memory: " + fileId);
        }
    }

    public void removeFromMemory(String fileId) throws FileNotFoundException {
        if (inMemoryStorage.containsKey(fileId)) {
            inMemoryStorage.remove(fileId);
        } else {
            throw new FileNotFoundException("File not found in memory: " + fileId);
        }
    }
}

