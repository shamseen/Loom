package com.example.entry_service.model;

import com.example.entry_service.core.LoomObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * The atomic unit of knowledge; e.g. note, concept, quote, or any discrete thought.
 */
@SuperBuilder            // Lombok: enables builder pattern for class hierarchies
@Getter                  // Lombok: generates for all non-static fields
@Setter
public class LoomEntry extends LoomObject {
  private String title;
  private String body;
  private String entryType; // user-defined e.g. NOTE, CONCEPT, QUOTE, DIARY, DEFINITION

  @Override
  protected String getIdPrefix() { return "entry_"; }
  
}
