package cn.navyd.app.supermarket.productcategory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.AbstractBasicServiceTestData;
import cn.navyd.app.supermarket.BaseServiceTest;
import cn.navyd.app.supermarket.BasicServiceTestData;
import cn.navyd.app.supermarket.base.BaseService;
import cn.navyd.app.supermarket.base.ServiceException;

public class ProductCategoryServiceTest extends BaseServiceTest<ProductCategoryDO> {
  @Autowired
  private ProductCategoryService productCategoryService;
  
  @Override
  protected BasicServiceTestData<ProductCategoryDO> getBasicServiceTestData() {
    return new AbstractBasicServiceTestData<ProductCategoryDO>(ProductCategoryTestData.getInstance()) {
      @Override
      public BaseService<ProductCategoryDO> getBaseService() {
        return productCategoryService;
      }
    };
  }
  
  @Transactional
  @Test
  @Override
  public void saveTestForDuplicateException() {
    var savable = data.getSavable();
    savable.setCategoryName(data.getFirst().getCategoryName());
    assertThatThrownBy(() -> productCategoryService.save(savable))
      .isInstanceOf(DuplicateProductCategoryException.class);
  }
  
  @Transactional
  @Test
  @Override
  public void updateByPrimaryKeyTestForDuplicateException() {
    var lastUpdatable = data.getSavable();
    lastUpdatable.setId(data.getLastId());
    lastUpdatable.setCategoryName(data.getFirst().getCategoryName());
    assertThatThrownBy(() -> productCategoryService.updateByPrimaryKey(lastUpdatable))
      .isInstanceOf(DuplicateProductCategoryException.class);
  }

  @Override
  public void saveTestForNotFoundException() {
  }

  @Test
  @Transactional
  @Override
  public void saveTestForOtherException() {
    var savable = data.getSavable();
    // 超过长度限制
    savable.setCategoryName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    assertThatThrownBy(() -> productCategoryService.save(savable)).isInstanceOf(ServiceException.class);
  }

  @Transactional
  @Test
  @Override
  public void updateByPrimaryKeyTestForOtherException() {
    var updatable = data.getSavable();
    updatable.setId(data.getFirstId());
    // 超过长度限制
    updatable.setCategoryName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    assertThatThrownBy(() -> productCategoryService.updateByPrimaryKey(updatable)).isInstanceOf(ServiceException.class);
  }
  
  @Test
  void listChildrenByPrimaryKeyTest() {
    final int id = 1, childrenRows = 4;
    assertThat(productCategoryService.listChildrenByPrimaryKey(id))
      .isNotNull()
      .isNotEmpty()
      .doesNotContainNull()
      .hasSize(childrenRows)
      .allMatch(pc -> pc.getParentId() == id);
  }
  
  @Test
  void listDescendantsByPrimaryKeyTest() {
    int id = 0, descendantRows = data.getTotalRows();
    assertThat(productCategoryService.listDescendantsByPrimaryKey(id))
      .isNotNull()
      .isNotEmpty()
      .doesNotContainNull()
      .hasSize(descendantRows)
      .allSatisfy(pc -> assertThat(pc).hasNoNullFieldsOrProperties());
    
    id = 6;
    descendantRows = 5;
    assertThat(productCategoryService.listDescendantsByPrimaryKey(id))
      .isNotNull()
      .isNotEmpty()
      .doesNotContainNull()
      .hasSize(descendantRows)
      .allSatisfy(pc -> assertThat(pc).hasNoNullFieldsOrProperties());
  }
}
