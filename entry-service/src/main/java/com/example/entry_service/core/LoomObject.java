package com.example.entry_service.core;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Subclasses should implement a static create() factory method which generates the id
 * <p>Example: public static LoomEntry create(String title, ...) { ... }
 */
@MappedSuperclass        // JPA: base class is not a table
@SuperBuilder            // Lombok: enables builder pattern for class hierarchies
@Getter                  // Lombok: generates for all non-static fields
@Setter
public abstract class LoomObject {
  // TODO: generate id from UUID
  // this.id = getIdPrefix() + UUID.randomUUID().toString();
  @Id
  private String id;
  
  @Builder.Default        // Lombok: sets default field value
  @Enumerated(EnumType.STRING) // JPA: store enum as string, not int
  private Visibility visibility = Visibility.PUBLIC;

  @Builder.Default
  private ZonedDateTime createdAt =  ZonedDateTime.now(ZoneOffset.UTC);
  private ZonedDateTime updatedAt;


  /**
   * @return prefix string used to generate typed IDs, must include separator
   */
  protected abstract String getIdPrefix();
}
