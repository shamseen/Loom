package com.example.entry_service.core;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Subclasses should implement a static create() factory method which generates the id
 * <p>Example: public static LoomEntry create(String title, ...) { ... }
 */
@MappedSuperclass        // JPA: base class is not a table
@SuperBuilder            // Lombok: enables builder pattern for class hierarchies
@NoArgsConstructor       // Lombok: (required for JPA entity instantiation)
@AllArgsConstructor      // Lombok: fix NoArgsConstructor/Superbuilder conflict
@Getter                  // Lombok: generates for all non-static fields
@Setter
public abstract class LoomObject {
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

  /**
   * Generates a prefixed UUID for the object on creation. <br>
   * Sets createdAt and updatedAt timestamps.
   */

  @PrePersist             // JPA: runs before INSERT
  protected void onCreate() {
    this.id = getIdPrefix() + "_" + UUID.randomUUID().toString();
    if (this.visibility == null) this.visibility = Visibility.PUBLIC;
    if (this.createdAt == null) this.createdAt = ZonedDateTime.now(ZoneOffset.UTC);
    updatedAt = createdAt;
  }
}
