package com.example.entry_service.repository;

import com.example.entry_service.model.LoomEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data access layer for {@link LoomEntry} entities. 
 *  Extends JpaRepository for CRUD operations
 */

@Repository     // Spring: marks as data access layer; makes it a managed bean
public interface EntryRepository extends JpaRepository<LoomEntry, String>{
  /**
   * JpaRepository defines findAll, findById, save, delete
   * 
   * findById() - returns Optional<Product>, not Product directly
   *  - use .orElse(null) or .orElseThrow() to unwrap
   * save() - arg w null id = create new object + INSERT
   *  - generates new id via JPA annotation
   * saveAll() - creates multiple new entities
*/
}
