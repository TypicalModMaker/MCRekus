package dev.isnow.mcrekus.data.base;

import io.ebean.Model;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseData extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
}
