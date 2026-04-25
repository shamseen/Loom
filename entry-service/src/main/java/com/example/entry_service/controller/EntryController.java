package com.example.entry_service.controller;

import com.example.entry_service.model.LoomEntry;
import com.example.entry_service.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController               // Spring: handles HTTP requests; responses to JSON
@RequestMapping("/entries")   // Spring: base path for all endpoints
public class EntryController {
  
  @Autowired    // Spring: dependency injection
  private EntryService entryService; // business logic layer

  @GetMapping  // Spring: GET endpoint "/entries"
  public LoomEntry[] getAllEntries() {
    return entryService.getAllEntries();
  }

  @GetMapping("/{id}")
  public LoomEntry getEntryById(@PathVariable String id) {
    return  entryService.getEntryById(id);
  } 
}
