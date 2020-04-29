package utils;

public class Entity implements Comparable<Entity> {
  private String name, id, library, arkClass, archetypeId, levelDir;
  
  public Entity(String header) {
    // Parses an entity from its definition or invocation
  }

  private Entity(String name, String id, String library, String arkClass,
      String archetypeId, String levelDir) {
    this.name = name;
    this.id = id;
    this.library = library;
    this.arkClass = arkClass;
    this.archetypeId = archetypeId;
    this.levelDir = levelDir;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public String getLibrary() {
    return library;
  }

  public String getArkClass() {
    return arkClass;
  }

  public String getArchetypeId() {
    return archetypeId;
  }

  public String getLevelDir() {
    return levelDir;
  }

  public static Builder builder() {
    return new Entity.Builder();
  }
  
  public String toEntityArchetype() {
    return "";
  }
  
  public String toInvocation() {
    return "";
  }

  @Override
  public String toString() {
    return String
        .format(
            "Name: %s, ID: %s, Library: %s, ArkClass: %s, ArchetypeId: %s, LevelDir: %s",
            name, id, library, arkClass, archetypeId, levelDir);
  }

  @Override
  public boolean equals(Object o) {
    Entity other = (Entity) o;
    return other.getName().equals(name);

  }

  public static class Builder {
    private String name = "";
    private String nameInLevel = "";
    private String id = "";
    private String library = "";
    private String arkClass = "";
    private String archetypeId = "";
    private String levelDir = "";
    private String originalInvocation = "";
    private String originalDef = "";

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setId(String id) {
      this.id = id;
      return this;
    }

    public Builder setLibrary(String library) {
      this.library = library;
      return this;
    }

    public Builder setArkClass(String arkClass) {
      this.arkClass = arkClass;
      return this;
    }

    public Builder setArchetypeId(String archetypeId) {
      this.archetypeId = archetypeId;
      return this;
    }

    public Builder setLevelDir(String levelDir) {
      this.levelDir = levelDir;
      return this;
    }

    public Entity build() {
      return new Entity(name, id, library, arkClass, archetypeId, levelDir);
    }
  }

  @Override
  public int compareTo(Entity o) {
    return this.name.compareTo(o.name);
  }
}
