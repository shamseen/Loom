package com.example.entry_service.service;

import com.example.entry_service.model.LoomEntry;
import com.example.entry_service.repository.EntryRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer for {@link LoomEntry} entities. 
 * Handles business logic for creating, reading, updating, and deleting entries.
 */
@Service          // Spring: marks as business logic layer; makes it a managed bean
public class EntryService {

  @Autowired      // Spring: dependency injection
  EntryRepository entryRepository;  // data layer

  public List<LoomEntry> getAllEntries() {
    return entryRepository.findAll();
  }

  public LoomEntry getEntryById(String id) {
    var e = entryRepository.findById(id); // returns Optional<LoomEntry>
    return e.orElse(null); // null if id not found
  }

  public String seedData() {   
    entryRepository.save(LoomEntry.builder()
      .id("e1")
      .title("entry 1")
      .body("body 1")
      .build()
    );
    
    entryRepository.save(LoomEntry.builder()
      .id("e2")
      .title("entry 2")
      .body("body 2")
      .build()
    );

    return "Data created.";
  }
}
