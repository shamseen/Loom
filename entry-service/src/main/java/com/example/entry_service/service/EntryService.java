package com.example.entry_service.service;

import com.example.entry_service.model.LoomEntry;
import org.springframework.stereotype.Service;

@Service          // Spring: marks as business logic layer; makes it a managed bean
public class EntryService {

  public LoomEntry[] getAllEntries() {
    LoomEntry e1 = LoomEntry.builder()
      .id("e1")
      .title("entry 1")
      .body("body 1")
      .build();
    
      LoomEntry e2 = LoomEntry.builder()
      .id("e2")
      .title("entry 2")
      .body("body 2")
      .build();

    LoomEntry[] list = {e1, e2};

    return list;
  }

  public LoomEntry getEntryById(String id) {
      return LoomEntry.builder()
      .id(id)
      .title("entry")
      .body("body")
      .build();
  }
}
