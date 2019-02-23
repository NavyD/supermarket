package cn.navyd.app.supermarket.product;

import java.util.Optional;
import cn.navyd.app.supermarket.base.BaseService;

public interface ProductService extends BaseService<ProductDO> {
  Optional<ProductDO> getByName(String name);
}
