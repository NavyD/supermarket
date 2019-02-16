package cn.navyd.app.supermarket.supplier;

import java.util.Collection;
import cn.navyd.app.supermarket.base.BaseDao;

public interface SupplierRecordDao extends BaseDao<SupplierRecordDO> {

  @Override
  int countTotalRows();

  @Override
  int countRowsByLastId(Integer lastId);

  @Override
  SupplierRecordDO getByPrimaryKey(Integer id);

  @Override
  Collection<SupplierRecordDO> listPage(Integer pageNumber, Integer pageSize,
      Integer lastId);

  @Override
  void save(SupplierRecordDO bean);
  
  @Override
  void saveAll(Collection<SupplierRecordDO> beans);

  @Override
  void updateByPrimaryKey(SupplierRecordDO bean);

  @Override
  void removeByPrimaryKey(Integer id);
  
  /**
   * 查询productId最新的供应记录
   * @param productId
   * @return
   */
  SupplierRecordDO getLastByProductId(Integer productId);
}
