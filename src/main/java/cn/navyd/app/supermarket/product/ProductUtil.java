package cn.navyd.app.supermarket.product;

import java.util.concurrent.TimeUnit;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

public interface ProductUtil {
  public static final ProductWeightUnitEnum DEFAULT_WEIGHT_UNIT = ProductWeightUnitEnum.GRAM;

  static void test() {
    SqlBuilder a;
    SqlProviderAdapter ab;
  }
  boolean isExpired();
}
