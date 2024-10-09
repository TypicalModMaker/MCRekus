package dev.isnow.mcrekus.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import io.ebean.typequery.Generated;

import io.ebean.config.ModuleInfo;
import io.ebean.config.EntityClassRegister;

@io.ebean.typequery.Generated("io.ebean.querybean.generator")
@ModuleInfo(other={"dev.isnow.mcrekus.util.serializer.database.HomeSerializer","dev.isnow.mcrekus.util.serializer.database.RekusLocationSerializer"}, entities={"dev.isnow.mcrekus.data.PlayerData"})
public class EbeanEntityRegister implements EntityClassRegister {

  /**
   * Register AttributeConverter etc
   */
  private List<Class<?>> otherClasses() {
    List<Class<?>> others = new ArrayList<>();
    others.add(dev.isnow.mcrekus.util.serializer.database.HomeSerializer.class);
    others.add(dev.isnow.mcrekus.util.serializer.database.RekusLocationSerializer.class);
    return others;
  }

  /**
   * Entities with no @DbName
   */
  private List<Class<?>> defaultEntityClasses() {
    List<Class<?>> entities = new ArrayList<>();
    entities.add(dev.isnow.mcrekus.data.PlayerData.class);
    entities.addAll(otherClasses());
    return entities;
  }

  private List<Class<?>> classesFor(String dbName) {
    return new ArrayList<>();
  }

  @Override
  public List<Class<?>> classesFor(String dbName, boolean defaultServer) {
    List<Class<?>> classes = classesFor(dbName);
    if (defaultServer) {
      classes.addAll(defaultEntityClasses());
    }
    return classes;
  }

}
