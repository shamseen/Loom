package com.example.entry_service.core;

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
@SuperBuilder            // Lombok: enables builder pattern for class hierarchies
@Getter                  // Lombok: generates for all non-static fields
@Setter
public abstract class LoomObject {
  // TODO: generate id from UUID
  // this.id = getIdPrefix() + UUID.randomUUID().toString();
  private String id;
  @Builder.Default        // Lombok: sets default field value
  private Visibility visibility = Visibility.PUBLIC;
  @Builder.Default
  private ZonedDateTime createdAt =  ZonedDateTime.now(ZoneOffset.UTC);
  private ZonedDateTime updatedAt;


  /**
   * @return prefix string used to generate typed IDs, must include separator
   */
  protected abstract String getIdPrefix();
}
