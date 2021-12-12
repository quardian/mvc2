package com.inho.mvc2.web.controller.fileupload.spring.domain;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FileItemRepository {
    private static final Map<Long, FileItem> store = new HashMap<>(); //static
    private static long sequence = 0L; //static

    public FileItem save(FileItem item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public FileItem findById(Long id) {
        return store.get(id);
    }

    public List<FileItem> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
