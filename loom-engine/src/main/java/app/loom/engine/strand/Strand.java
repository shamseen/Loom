package app.loom.engine.strand;

import app.loom.engine.core.LoomObject;

public class Strand extends LoomObject{
  private String sourceEntryId;
  private String targetEntryId;
  private StrandType type;

  @Override
  protected String getIdPrefix() { return "strand"; }
}
