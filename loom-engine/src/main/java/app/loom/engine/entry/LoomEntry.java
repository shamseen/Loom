package app.loom.engine.entry;

import app.loom.engine.core.LoomObject;

public class LoomEntry extends LoomObject {
  private String title;
  private String body;
  private String entryType; // user-defined e.g. NOTE, CONCEPT, QUOTE, DIARY, DEFINITION

  @Override
  protected String getIdPrefix() { return "entry"; }
}
