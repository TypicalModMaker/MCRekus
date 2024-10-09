package dev.isnow.mcrekus.data.query;

import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import io.ebean.typequery.PLong;
import io.ebean.typequery.PScalar;
import io.ebean.typequery.PUuid;
import java.lang.String;
import java.util.Map;

/**
 * Query bean for PlayerData.
 * <p>
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
@SuppressWarnings("unused")
@io.ebean.typequery.Generated("io.ebean.querybean.generator")
@io.ebean.typequery.TypeQueryBean("v1")
public final class QPlayerData extends io.ebean.typequery.QueryBean<dev.isnow.mcrekus.data.PlayerData,QPlayerData> {

  private static final QPlayerData _alias = new QPlayerData(true);

  /**
   * Return the shared 'Alias' instance used to provide properties to 
   * <code>select()</code> and <code>fetch()</code> 
   */
  public static QPlayerData alias() {
    return _alias;
  }

  public PLong<QPlayerData> id;
  public PUuid<QPlayerData> uuid;
  public PScalar<QPlayerData, Map<String,Home>> homeLocations;
  public PScalar<QPlayerData, RekusLocation> lastLocation;
  public PLong<QPlayerData> version;


  /**
   * Return a query bean used to build a FetchGroup.
   * <p>
   * FetchGroups are immutable and threadsafe and can be used by many
   * concurrent queries. We typically stored FetchGroup as a static final field.
   * <p>
   * Example creating and using a FetchGroup.
   * <pre>{@code
   * 
   * static final FetchGroup<Customer> fetchGroup = 
   *   QCustomer.forFetchGroup()
   *     .shippingAddress.fetch()
   *     .contacts.fetch()
   *     .buildFetchGroup();
   * 
   * List<Customer> customers = new QCustomer()
   *   .select(fetchGroup)
   *   .findList();
   * 
   * }</pre>
   */
  public static QPlayerData forFetchGroup() {
    return new QPlayerData(io.ebean.FetchGroup.queryFor(dev.isnow.mcrekus.data.PlayerData.class));
  }

  /** Construct using the default Database */
  public QPlayerData() {
    super(dev.isnow.mcrekus.data.PlayerData.class);
  }

  /** @deprecated migrate to query.usingTransaction() */
  @Deprecated(forRemoval = true)
  public QPlayerData(io.ebean.Transaction transaction) {
    super(dev.isnow.mcrekus.data.PlayerData.class, transaction);
  }

  /** Construct with a given Database */
  public QPlayerData(io.ebean.Database database) {
    super(dev.isnow.mcrekus.data.PlayerData.class, database);
  }


  /** Private constructor for Alias */
  private QPlayerData(boolean dummy) {
    super(dummy);
  }

  /** Private constructor for FetchGroup building */
  private QPlayerData(io.ebean.Query<dev.isnow.mcrekus.data.PlayerData> fetchGroupQuery) {
    super(fetchGroupQuery);
  }

  /** Private constructor for filterMany */
  private QPlayerData(io.ebean.ExpressionList<dev.isnow.mcrekus.data.PlayerData> filter) {
    super(filter);
  }

  /** Return a copy of the query bean. */
  @Override
  public QPlayerData copy() {
    return new QPlayerData(query().copy());
  }

  /**
   * Provides static properties to use in <em> select() and fetch() </em>
   * clauses of a query. Typically referenced via static imports. 
   */
  @io.ebean.typequery.Generated("io.ebean.querybean.generator")
  public static final class Alias {
    public static PLong<QPlayerData> id = _alias.id;
    public static PUuid<QPlayerData> uuid = _alias.uuid;
    public static PScalar<QPlayerData, Map<String,Home>> homeLocations = _alias.homeLocations;
    public static PScalar<QPlayerData, RekusLocation> lastLocation = _alias.lastLocation;
    public static PLong<QPlayerData> version = _alias.version;
  }

  /** Association query bean */
  @io.ebean.typequery.Generated("io.ebean.querybean.generator")
  @io.ebean.typequery.TypeQueryBean("v1")
  public static abstract class Assoc<R> extends io.ebean.typequery.TQAssocBean<dev.isnow.mcrekus.data.PlayerData,R,QPlayerData> {

    public PLong<R> id;
    public PUuid<R> uuid;
    public PScalar<R, Map<String,Home>> homeLocations;
    public PScalar<R, RekusLocation> lastLocation;
    public PLong<R> version;

    protected Assoc(String name, R root) { super(name, root); }
    protected Assoc(String name, R root, String prefix) { super(name, root, prefix); }
  }

  /** Associated ToOne query bean */
  @io.ebean.typequery.Generated("io.ebean.querybean.generator")
  @io.ebean.typequery.TypeQueryBean("v1")
  public static final class AssocOne<R> extends Assoc<R> {
    public AssocOne(String name, R root) { super(name, root); }
    public AssocOne(String name, R root, String prefix) { super(name, root, prefix); }
  }

  /** Associated ToMany query bean */
  @io.ebean.typequery.Generated("io.ebean.querybean.generator")
  @io.ebean.typequery.TypeQueryBean("v1")
  public static final class AssocMany<R> extends Assoc<R> implements io.ebean.typequery.TQAssocMany<dev.isnow.mcrekus.data.PlayerData, R, QPlayerData>{
    public AssocMany(String name, R root) { super(name, root); }
    public AssocMany(String name, R root, String prefix) { super(name, root, prefix); }

    @Override
    public R filterMany(java.util.function.Consumer<QPlayerData> apply) {
      final io.ebean.ExpressionList<dev.isnow.mcrekus.data.PlayerData> list = _newExpressionList();
      apply.accept(new QPlayerData(list));
      return _filterMany(list);
    }

    @Override
    public R filterMany(io.ebean.ExpressionList<dev.isnow.mcrekus.data.PlayerData> filter) { return _filterMany(filter); }

    @Override
    public R filterManyRaw(String rawExpressions, Object... params) { return _filterManyRaw(rawExpressions, params); }

    @Override
    @Deprecated(forRemoval = true)
    public R filterMany(String expressions, Object... params) { return _filterMany(expressions, params); }

    @Override
    public R isEmpty() { return _isEmpty(); }

    @Override
    public R isNotEmpty() { return _isNotEmpty(); }
  }
}
