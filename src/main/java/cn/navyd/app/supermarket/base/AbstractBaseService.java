package cn.navyd.app.supermarket.base;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Optional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import cn.navyd.app.supermarket.util.Page;
import cn.navyd.app.supermarket.util.PageInfo;
import cn.navyd.app.supermarket.util.PageUtil;

/**
 * BaseService的抽象实现，对于单个Dao对象的service封装应该继承该类
 * 
 * @author navyd
 *
 * @param <T>
 * @param <E>
 */
public abstract class AbstractBaseService<E extends BaseDO>
    implements BaseService<E> {
  private final BaseDao<E> baseDao; 
  
  public AbstractBaseService(BaseDao<E> baseDao) {
    this.baseDao = baseDao;
  }

  @Override
  public Optional<E> getByPrimaryKey(Integer id) {
    checkArgument(id != null && id >= 0, "id: %s", id);
    return Optional.ofNullable(baseDao.getByPrimaryKey(id));
  }

  @Override
  public PageInfo<E> listPage(Integer pageNumber, Integer pageSize, Integer lastId) {
    checkArgument(pageNumber != null && pageNumber >= 0, "pageNumber: %s", pageNumber);
    checkArgument(pageSize != null && pageSize > 0, "pageSize: %s", pageSize);
    checkArgument(lastId == null || lastId >= 0, "lastId: %s", lastId);
    final int totalRows = baseDao.countTotalRows();
    // 检查参数是否合法。非法则不会执行查询操作
    PageUtil.checkPageParam(pageNumber, pageSize, totalRows);
    var data = baseDao.listPage(pageNumber, pageSize, lastId);
    return Page.of(pageNumber, pageSize, totalRows, data);
  }

  @Transactional
  @Override
  public E save(E bean) throws ServiceException {
//    checkNotNull(bean);
    // 检查关联对象
    checkAssociativeNotFound(bean);
    // 保存
    try {
      baseDao.save(bean);
    } catch (Exception e) {
      if (e instanceof DuplicateKeyException) {
        throw createDuplicateException(e.getMessage());
      }
      throw new ServiceException(e.getMessage());
    }
    // 返回
    return checkNotFoundByPrimaryKey(bean.getId());
  }

  @Transactional
  @Override
  public E updateByPrimaryKey(E bean) throws ServiceException {
    checkNotNull(bean);
    // 检查id是否已存在
    final var id = bean.getId();
    checkNotFoundByPrimaryKey(id);
    // 检查关联对象
    checkAssociativeNotFound(bean);
    // 更新
    try {
      baseDao.updateByPrimaryKey(bean);
    } catch (Exception e) {
      if (e instanceof DuplicateKeyException) {
        throw createDuplicateException(e.getMessage());
      }
      throw new ServiceException(e.getMessage());
    }
    // 返回
    return checkNotFoundByPrimaryKey(id);
  }

  @Transactional
  @Override
  public void removeByPrimaryKey(Integer id) throws ServiceException {
    checkNotFoundByPrimaryKey(id);
    baseDao.removeByPrimaryKey(id);
  }

  /**
   * 检查指定对象关联对象是否存在，用于保存和更新时检查对象是否满足要求。如果对应字段为null应该跳过不检查
   * 
   * @param bean
   */
  protected abstract void checkAssociativeNotFound(E bean) throws NotFoundException;

  /**
   * 用于在查找失败时抛出创建的异常。默认使用NotFoundException类型
   * 
   * @param message
   * @return
   */
  protected NotFoundException createNotFoundException(String message) {
    return new NotFoundException(message);
  }

  /**
   * 用于在更新时已存在资源时失败抛出创建的异常。默认使用DuplicateException类型
   * 
   * @param message
   * @return
   */
  protected DuplicateException createDuplicateException(String message) {
    return new DuplicateException(message);
  }
  
  /**
   * 通过主键检查对应对象是否存在，如果不存在则抛出异常
   * 
   * @param id
   * @return
   */
  protected final E checkNotFoundByPrimaryKey(Integer id) {
    var key = getByPrimaryKey(id);
    if (!key.isPresent()) {
      throw createNotFoundException("id: " + id);
    }
    return key.get();
  }
}
