package cn.navyd.app.supermarket.productcategory;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import cn.navyd.app.supermarket.AbstractBasicDaoTestData;
import cn.navyd.app.supermarket.BaseDaoTest;
import cn.navyd.app.supermarket.BasicDaoTestData;
import cn.navyd.app.supermarket.base.BaseDao;

public class ProductCategoryDaoTest extends BaseDaoTest<ProductCategoryDO> {
  @Autowired
  private ProductCategoryDao productCategoryDao;
  private BasicDaoTestData<ProductCategoryDO> data;
  
  @Test
  void listChildrenByPrimaryKeyTest() {
    final int id = 1, childrenRows = 4;
    assertThat(productCategoryDao.listChildrenByPrimaryKey(id))
      .isNotNull()
      .isNotEmpty()
      .doesNotContainNull()
      .hasSize(childrenRows)
      .allMatch(pc -> pc.getParentId() == id);
  }
  
  @Test
  void listDescendantsByPrimaryKeyTest() {
    int id = 0, descendantRows = data.getTotalRows();
    assertThat(productCategoryDao.listDescendantsByPrimaryKey(id))
      .isNotNull()
      .isNotEmpty()
      .doesNotContainNull()
      .hasSize(descendantRows)
      .allSatisfy(pc -> assertThat(pc).hasNoNullFieldsOrProperties());
    
    id = 6;
    descendantRows = 5;
    assertThat(productCategoryDao.listDescendantsByPrimaryKey(id))
      .isNotNull()
      .isNotEmpty()
      .doesNotContainNull()
      .hasSize(descendantRows)
      .allSatisfy(pc -> assertThat(pc).hasNoNullFieldsOrProperties());
  }

  @Override
  protected BasicDaoTestData<ProductCategoryDO> getBasicDaoTestData() {
    if (data == null)
      data =
          new AbstractBasicDaoTestData<ProductCategoryDO>(ProductCategoryTestData.getInstance()) {
            @Override
            public BaseDao<ProductCategoryDO> getBaseDao() {
              return productCategoryDao;
            }
          };
    return data;
  }
}
