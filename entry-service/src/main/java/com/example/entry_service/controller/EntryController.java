package com.example.entry_service.controller;

import com.example.entry_service.model.LoomEntry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController               // Spring: handles HTTP requests; responses to JSON
@RequestMapping("/entries")   // Spring: base path for all endpoints
public class EntryController {

  @GetMapping  // Spring: GET endpoint "/entries"
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

  @GetMapping("/{id}")
  public LoomEntry getEntryById(@PathVariable String id) {
      return LoomEntry.builder()
      .id("e"+id)
      .title("entry")
      .body("body")
      .build();
  }
  
}
