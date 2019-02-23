package cn.navyd.app.supermarket;

import static org.assertj.core.api.Assertions.assertThat;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= {DaoConfig.class, DatasourceConfig.class, MyBatisConfig.class})
@ActiveProfiles(profiles = SupermarketProfiles.DEVELOPMENT)
public abstract class BaseDaoTest<T extends BaseDO> extends BaseTest {
  
  @Test
  void countTotalRowsTest() {
    int rows = getTotalRows();
    assertThat(getBaseDao().countTotalRows()).isNotNull().isEqualTo(rows);
  }
  
  @Test
  void countRowsByLastIdTest() {
    int lastId = getLastId();
    int remainderRows = getTotalRows() - lastId;
    assertThat(getBaseDao().countRowsByLastId(lastId)).isNotNull().isEqualTo(remainderRows);
  }
  
  @Test
  void getByPrimaryKeyTest() {
    int id = getFirstId();
    var dao = getBaseDao();
    assertThat(dao.getByPrimaryKey(id))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(getFirst(), BASE_PROPERTIES)
      .matches(p -> p.getId() == id);
    
    int invalidId = -1;
    assertThat(dao.getByPrimaryKey(invalidId)).isNull();
    
    invalidId = Integer.MAX_VALUE;
    assertThat(dao.getByPrimaryKey(invalidId)).isNull();
  }
  
  @Test
  void listPageTest() {
    var dao = getBaseDao();
    int pageSize = Integer.MAX_VALUE, pageNum = 0;
    final int totalRows = getTotalRows();
    int expectedSize = PageUtils.getCurrentSize(totalRows, pageNum, pageSize);
    assertThat(dao.listPage(pageNum, pageSize, null))
      .isNotNull()
      .isNotEmpty()
      .hasSize(expectedSize)
      .doesNotContainNull()
      .first().isEqualToIgnoringGivenFields(getFirst(), BASE_PROPERTIES);
    
    // 测试lastId
    int lastId = getLastId();
    expectedSize = getTotalRows() - lastId;
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
      .first().isEqualToIgnoringGivenFields(getFirst(), BASE_PROPERTIES);
    
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
    var savable = getSavable();
    var dao = getBaseDao();
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
    var dao = getBaseDao();
    final int firstId = getFirstId();
    var existingBean = dao.getByPrimaryKey(firstId);
    assertThat(existingBean).isNotNull();
    var savable = getSavable();
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
    var dao = getBaseDao();
    int firstId = getFirstId();
    assertThat(dao.getByPrimaryKey(firstId)).isNotNull();
    dao.removeByPrimaryKey(firstId);
    assertThat(dao.getByPrimaryKey(firstId)).isNull();
  }
  
  /**
   * 获取baseDao对象
   * @return
   */
  protected BaseDao<T> getBaseDao() {
    return null;
  }
  
  /**
   * 返回第一个测试数据，不包含{@link BaseDO}字段。应该可以通过equals()判断
   * @return
   */
  protected T getFirst() {
    return null;
  }
  
  /**
   * 获取第一个对象id。默认id=1
   * @return
   */
  protected int getFirstId() {
    return 1;
  }
  
  /**
   * 获取最大行数
   * @return
   */
  protected int getTotalRows() {
    return 0;
  }
  
  /**
   * 获取lastId测试countRowsByLastId，默认返回getTotalRows() - 1
   * @return
   */
  protected int getLastId() {
    return getTotalRows() - 1;
  }
  
  /**
   * 返回可保存的对象。要求对象仅{@link BaseDO}字段为null
   * @return
   */
  protected T getSavable() {
    return null;
  }
}
