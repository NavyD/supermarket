package cn.navyd.app.supermarket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.base.BaseDO;
import cn.navyd.app.supermarket.base.NotFoundException;
import cn.navyd.app.supermarket.config.DaoConfig;
import cn.navyd.app.supermarket.config.DatasourceConfig;
import cn.navyd.app.supermarket.config.EmailProperties;
import cn.navyd.app.supermarket.config.MyBatisConfig;
import cn.navyd.app.supermarket.config.SecurityConfig;
import cn.navyd.app.supermarket.config.ServiceConfig;
import cn.navyd.app.supermarket.config.SupermarketProfiles;
import cn.navyd.app.supermarket.util.PageUtils;

@ActiveProfiles(profiles = SupermarketProfiles.DEVELOPMENT)
@EnableConfigurationProperties
@SpringBootTest(classes= {DaoConfig.class, DatasourceConfig.class, MyBatisConfig.class, ServiceConfig.class, SecurityConfig.class, EmailProperties.class})
public abstract class BaseServiceTest<T extends BaseDO> extends BaseTest {
  protected BasicServiceTestData<T> data;
  
  @BeforeEach
  public void setup() {
    this.data = getBasicServiceTestData();
  }
  
  @BeforeEach
  public void checkData() {
    assertThat(data.getSavable())
      .isNotNull()
      .hasNoNullFieldsOrPropertiesExcept(BASE_PROPERTIES);
  }
  
  @Test
  public void getByPrimaryKeyTest() {
    int id = data.getFirstId();
    assertThat(data.getBaseService().getByPrimaryKey(id))
      .isNotNull()
      .isPresent().get()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(data.getFirst(), BASE_PROPERTIES)
      .matches(p -> p.getId() == id);
    
    assertThat(data.getBaseService().getByPrimaryKey(Integer.MAX_VALUE))
      .isNotNull()
      .isEmpty();
  }
  
  @Test
  public void getByPrimaryKeyTestForArgsException() {
    final Class<?> argsExClazz = IllegalArgumentException.class;
    assertThatThrownBy(() -> data.getBaseService().getByPrimaryKey(-1))
      .isInstanceOf(argsExClazz);
    
    assertThatThrownBy(() -> data.getBaseService().getByPrimaryKey(Integer.MIN_VALUE))
      .isInstanceOf(argsExClazz);
    
    assertThatThrownBy(() -> data.getBaseService().getByPrimaryKey(null))
    .isInstanceOf(argsExClazz);
  }
  
  @Test
  public void listPageTest() {
    var service = data.getBaseService();
    int totalRows = data.getTotalRows();
    int pageNumber = 0, pageSize = 10;
    var beans = service.listPage(pageNumber, pageSize);
    assertThat(beans)
      .isNotNull()
      .matches(
          info -> info.getPageNumber() == pageNumber 
          && info.getPageSize() == pageSize
          && info.getTotalRows() == totalRows
          && info.getTotalPages() == PageUtils.getTotalPages(totalRows, pageSize)
          && info.getOffset() == PageUtils.getOffset(pageNumber, pageSize));
  }
  
  
  @Transactional
  @Test
  public void saveTest() {
    var bean = data.getSavable();
    var service = data.getBaseService();
    assertThat(service.save(bean))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(bean, BASE_PROPERTIES);
  }
  
  @Transactional
  @Test
  public void saveAllTest() {
    var service = data.getBaseService();
    var savableList = Arrays.asList(data.getSavable());
    assertThat(service.saveAll(savableList))
      .isNotNull()
      .isNotEmpty()
      .hasSize(savableList.size())
      .element(0)
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(savableList.get(0), BASE_PROPERTIES);
  }
  
  public abstract void saveTestForDuplicateException();
  
  public abstract void saveTestForNotFoundException();
  
  public abstract void saveTestForOtherException();
  
  @Transactional
  @Test
  public void updateByPrimaryKeyTest() {
    var service = data.getBaseService();
    int firstId = data.getFirstId();
    var existing = service.getByPrimaryKey(firstId);
    assertThat(existing).isNotNull().isPresent();
    
    var updateBean = data.getSavable();
    updateBean.setId(firstId);
    
    assertThat(service.updateByPrimaryKey(updateBean))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .isEqualToIgnoringGivenFields(updateBean, BASE_PROPERTIES[1], BASE_PROPERTIES[2]);
  }
  
  public  abstract void updateByPrimaryKeyTestForDuplicateException();
  
  @Transactional
  @Test
  public void updateByPrimaryKeyTestForNotFoundException() {
    var service = data.getBaseService();
    int id = Integer.MAX_VALUE;
    var updateBean = data.getSavable();
    updateBean.setId(id);
    assertThatThrownBy(() -> service.updateByPrimaryKey(updateBean)).isInstanceOf(NotFoundException.class);
  }
  
  public abstract void updateByPrimaryKeyTestForOtherException();
  
  @Transactional
  @Test
  public void removeByPrimaryKeyTest() {
    int id = data.getFirstId();
    var service = data.getBaseService();
    assertThat(service.getByPrimaryKey(id)).isNotNull().isPresent();
    service.removeByPrimaryKey(id);
    assertThat(service.getByPrimaryKey(id)).isNotNull().isEmpty();
  }
  
  @Transactional
  @Test
  public void removeByPrimaryKeyTestForNotFoundException() {
    int id = Integer.MAX_VALUE;
    assertThatThrownBy(() -> data.getBaseService().removeByPrimaryKey(id))
      .isInstanceOf(NotFoundException.class);
  }
  
  
  protected abstract BasicServiceTestData<T> getBasicServiceTestData();
}
