package com.example.entry_service.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

/*
 */
public abstract class LoomObject {
  private String id;
  private Visibility visibility;
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;

  /**
   * @return prefix string used to generate typed IDs
   */
  protected abstract String getIdPrefix();

  public LoomObject(){
    this(Visibility.PUBLIC);
  }

  /**
   * 
   * @param vis
   */
  public LoomObject(Visibility vis){
    this.id = getIdPrefix() + "_" + UUID.randomUUID().toString();
    this.createdAt = ZonedDateTime.now(ZoneOffset.UTC);
    this.updatedAt = this.createdAt;
    this.visibility = vis;
  }
}
