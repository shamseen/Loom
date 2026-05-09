package com.example.entry_service.model;

import com.example.entry_service.core.LoomObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * The atomic unit of knowledge; e.g. note, concept, quote, or any discrete thought.
 */
@Entity                  // JPA: model/data structure, creates table
@Table(name = "loom_entries") // JPA: custom table name
@SuperBuilder            // Lombok: enables builder pattern for class hierarchies
@NoArgsConstructor       // Lombok: (required for JPA entity instantiation)
@AllArgsConstructor      // Lombok: fix NoArgsConstructor/Superbuilder conflict
@Getter                  // Lombok: generates for all non-static fields
@Setter
public class LoomEntry extends LoomObject {
  private String title;
  @Column(columnDefinition = "TEXT") // JPA: use TEXT type instead of VARCHAR
  private String body;
  private String entryType; // user-defined e.g. NOTE, CONCEPT, QUOTE, DIARY, DEFINITION

  @Override
  protected String getIdPrefix() { return "entry"; }
  
}
