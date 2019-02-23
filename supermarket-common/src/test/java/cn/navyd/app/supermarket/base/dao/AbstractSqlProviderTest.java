package cn.navyd.app.supermarket.base.dao;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class AbstractSqlProviderTest {

  @Test
  void getColumnsStringTest() {
    String columns = "id, product_name, production_date, shelf_life, product_unit, specification, "
        + "specification_unit, product_category_id, product_category_name, supplier_id, "
        + "supplier_name, gmt_create, gmt_modified";
    
    String[] testCols = {"id, product_name, production_date, shelf_life, product_unit, specification, ",
        "specification_unit, product_category_id, product_category_name, supplier_id, ",
        "supplier_name, gmt_create, gmt_modified"};
    assertThat(AbstractSqlProvider.getColumnsString(testCols)).isEqualTo(columns); 
  }
}
