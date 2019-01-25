package cn.navyd.app.supermarket.base;

import java.util.Objects;
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
public abstract class AbstractBaseService<T extends BaseDao<E>, E extends BaseDO>
    implements BaseService<E> {
  protected final T dao;

  public AbstractBaseService(T dao) {
    this.dao = dao;
  }

  @Override
  public Optional<E> getByPrimaryKey(Integer id) {
    return Optional.ofNullable(dao.getByPrimaryKey(checkPrimaryKeyId(id)));
  }

  @Override
  public PageInfo<E> listPage(Integer pageNum, Integer pageSize, Integer lastId) {
    int totalRows = dao.countTotalRows();
    // 检查参数是否合法。非法则不会执行查询操作
    PageUtil.checkPageParam(pageNum, pageSize, totalRows);
    var data = dao.listPage(pageNum, pageSize, lastId);
    return Page.of(pageNum, pageSize, totalRows, data);
  }

  @Transactional
  @Override
  public E save(E bean) throws ServiceException {
    Objects.requireNonNull(bean);
    // 检查关联对象
    checkAssociativeNotFound(bean);
    // 保存
    try {
      dao.save(bean);
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
    Objects.requireNonNull(bean);
    // 检查id是否已存在
    final var id = bean.getId();
    checkNotFoundByPrimaryKey(id);
    // 检查关联对象
    checkAssociativeNotFound(bean);
    // 更新
    try {
      dao.updateByPrimaryKey(bean);
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
    dao.removeByPrimaryKey(id);
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

  /**
   * 如果指定id == null 或 < 0 则抛出异常
   * 
   * @param id
   * @return
   */
  private Integer checkPrimaryKeyId(Integer id) {
    Objects.requireNonNull(id);
    if (id < 0)
      throw new IllegalArgumentException("id:" + id);
    return id;
  }
}
