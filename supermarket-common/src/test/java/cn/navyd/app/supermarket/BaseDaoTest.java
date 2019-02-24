package cn.navyd.app.supermarket;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.base.BaseDO;
import cn.navyd.app.supermarket.base.BaseDao;
import cn.navyd.app.supermarket.config.DaoConfig;
import cn.navyd.app.supermarket.config.DatasourceConfig;
import cn.navyd.app.supermarket.config.MyBatisConfig;
import cn.navyd.app.supermarket.config.SupermarketProfiles;
import cn.navyd.app.supermarket.util.PageUtils;

/**
 * 测试BaseDao的方法，具体测试子类dao应该实现该类。
 * @author navyd
 *
 * @param <T>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= {DaoConfig.class, DatasourceConfig.class, MyBatisConfig.class})
@ActiveProfiles(profiles = SupermarketProfiles.DEVELOPMENT)
public abstract class BaseDaoTest<T extends BaseDO> extends BaseTest {
  protected BasicDaoTestData<T> data;
  
  @BeforeEach
  void setup() {
    this.data = getBasicDaoTestData();
  }
  
  @Test
  void countTotalRowsTest() {
    int rows = data.getTotalRows();
    assertThat(data.getBaseDao().countTotalRows()).isNotNull().isEqualTo(rows);
  }
  
  @Test
  void countRowsByLastIdTest() {
    int lastId = data.getLastId();
    int remainderRows = data.getTotalRows() - lastId;
    assertThat(data.getBaseDao().countRowsByLastId(lastId)).isNotNull().isEqualTo(remainderRows);
  }
  
  @Test
  void getByPrimaryKeyTest() {
    int id = data.getFirstId();
    var dao = data.getBaseDao();
    assertThat(dao.getByPrimaryKey(id))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(data.getFirst(), BASE_PROPERTIES)
      .matches(p -> p.getId() == id);
    
    int invalidId = -1;
    assertThat(dao.getByPrimaryKey(invalidId)).isNull();
    
    invalidId = Integer.MAX_VALUE;
    assertThat(dao.getByPrimaryKey(invalidId)).isNull();
  }
  
  @Transactional
  @Test
  public void getByPrimaryKeyCacheTest() {
    var dao = data.getBaseDao();
    int id = data.getFirstId();
    var bean = dao.getByPrimaryKey(id);
    assertThat(bean).isNotNull().hasNoNullFieldsOrProperties();
    // 修改 字段 将会导致 局部缓存被修改
    var change = bean.getGmtCreate().minusDays(10);
    bean.setGmtCreate(change);
    // 缓存被修改
    assertThat(dao.getByPrimaryKey(id))
      .matches(b -> b.getGmtCreate().isEqual(change) && b == bean);
  }
  
  @Test
  void listPageTest() {
    var dao = data.getBaseDao();
    int pageSize = Integer.MAX_VALUE, pageNum = 0;
    final int totalRows = data.getTotalRows();
    int expectedSize = PageUtils.getCurrentSize(totalRows, pageNum, pageSize);
    assertThat(dao.listPage(pageNum, pageSize, null))
      .isNotNull()
      .isNotEmpty()
      .hasSize(expectedSize)
      .doesNotContainNull()
      .first().isEqualToIgnoringGivenFields(data.getFirst(), BASE_PROPERTIES);
    
    // 测试lastId
    int lastId = data.getLastId();
    expectedSize = data.getTotalRows() - lastId;
    assertThat(dao.listPage(pageNum, pageSize, lastId))
      .isNotNull()
      .isNotEmpty()
      .hasSize(expectedSize);
    
    // 测试是否数量完全
    pageSize = totalRows-1;
    pageNum = 0;
    expectedSize = PageUtils.getCurrentSize(totalRows, pageNum, pageSize);
    assertThat(dao.listPage(pageNum, pageSize, null))
      .isNotNull()
      .isNotEmpty()
      .hasSize(expectedSize)
      .doesNotContainNull()
      .first().isEqualToIgnoringGivenFields(data.getFirst(), BASE_PROPERTIES);
    
    // 测试最后一页
    pageSize = totalRows-1;
    pageNum = 1;
    expectedSize = PageUtils.getCurrentSize(totalRows, pageNum, pageSize);
    assertThat(dao.listPage(pageNum, pageSize, null))
      .isNotNull()
      .isNotEmpty()
      .hasSize(expectedSize);
  }
  
  @Transactional
  @Test
  void saveTest() {
    var savable = data.getSavable();
    var dao = data.getBaseDao();
    assertThat(savable)
      .isNotNull()
      .hasNoNullFieldsOrPropertiesExcept(BASE_PROPERTIES);
    dao.save(savable);
    assertThat(savable).matches(bean -> bean.getId() != null);
    assertThat(dao.getByPrimaryKey(savable.getId()))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(savable, BASE_PROPERTIES);
  }
  
  @Transactional
  @Test
  void updateByPrimaryKeyTest() {
    var dao = data.getBaseDao();
    final int firstId = data.getFirstId();
    var existingBean = dao.getByPrimaryKey(firstId);
    assertThat(existingBean).isNotNull();
    var savable = data.getSavable();
    assertThat(savable)
      .isNotNull()
      .hasNoNullFieldsOrPropertiesExcept(BASE_PROPERTIES);
    savable.setId(firstId);
    savable.setGmtCreate(existingBean.getGmtCreate());

    dao.updateByPrimaryKey(savable);

    assertThat(dao.getByPrimaryKey(firstId))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(savable, BASE_PROPERTIES[2])
      .matches(bean -> !bean.getGmtModified().isEqual(existingBean.getGmtModified()));
  }
  
  @Transactional
  @Test
  void removeByPrimaryKeyTest() {
    var dao = data.getBaseDao();
    int firstId = data.getFirstId();
    assertThat(dao.getByPrimaryKey(firstId)).isNotNull();
    dao.removeByPrimaryKey(firstId);
    assertThat(dao.getByPrimaryKey(firstId)).isNull();
  }
  
  /**
   * 获取lastId测试countRowsByLastId，默认返回getTotalRows() - 1
   * @return
   * @deprecated {@link #getBasicDaoTestData()}
   */
  @Deprecated
  protected int getLastId() {
    return getTotalRows() - 1;
  }
  
  /**
   * 获取第一个对象id。默认id=1
   * @return
   * @deprecated {@link #getBasicDaoTestData()}
   */
  @Deprecated
  protected int getFirstId() {
    return 1;
  }
  
  /**
   * 获取baseDao对象
   * @return
   * @deprecated {@link #getBasicDaoTestData()}
   */
  @Deprecated
  protected  BaseDao<T> getBaseDao() {
    return null;
  }
  
  /**
   * 返回第一个测试数据，不包含{@link BaseDO}字段。应该可以通过equals()判断
   * @return
   * @deprecated {@link #getBasicDaoTestData()}
   */
  @Deprecated
  protected  T getFirst(){
    return null;
  }
  
  /**
   * 获取最大行数
   * @return
   * @deprecated {@link #getBasicDaoTestData()}
   */
  @Deprecated
  protected  int getTotalRows(){
    return 0;
  }
  
  /**
   * 返回可保存的对象。要求对象仅{@link BaseDO}字段为null
   * @return
   * @deprecated {@link #getBasicDaoTestData()}
   */
  @Deprecated
  protected  T getSavable() {
    return null;
  }
  
  /**
   * 返回测试对象，多次调用应该返回同样的引用
   * @return
   */
  protected BasicDaoTestData<T> getBasicDaoTestData() {
    return new BasicDaoTestDataProxy();
  }
  
  private class BasicDaoTestDataProxy implements BasicDaoTestData<T> {

    @Override
    public T getFirst() {
      return BaseDaoTest.this.getFirst();
    }

    @Override
    public int getTotalRows() {
      return BaseDaoTest.this.getTotalRows();
    }

    @Override
    public T getSavable() {
      return BaseDaoTest.this.getSavable();
    }

    @Override
    public BaseDao<T> getBaseDao() {
      return BaseDaoTest.this.getBaseDao();
    }
    
  }
}
