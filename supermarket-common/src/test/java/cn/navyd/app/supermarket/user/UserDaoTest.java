package cn.navyd.app.supermarket.user;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import cn.navyd.app.supermarket.BaseDaoTest;
import cn.navyd.app.supermarket.base.BaseDao;

public class UserDaoTest extends BaseDaoTest<UserDO> {
  @Autowired
  private UserDao userDao;
  
  @Test
  void getByUsernameTest() {
    String username = getFirst().getUsername();
    assertThat(userDao.getByUsername(username))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .matches(u -> u.getUsername().equals(username));
  }
  
  @Test
  void getByEmailTest() {
    String email = getFirst().getEmail();
    assertThat(userDao.getByEmail(email))
      .isNotNull()
      .hasNoNullFieldsOrProperties()
      .matches(u -> u.getEmail().equals(email));
  }
  
  @Override
  protected UserDO getFirst() {
    var user = new UserDO();
    user.setEmail("aabb@cc.dd");
    user.setHashPassword("{bcrypt}$2a$10$OFszpBSOVJflK1gwrvKQCOgE7w/zn83amQak1.oRcFIY0DI28i6yq");
    user.setPhoneNumber("13344445555");
    user.setUsername("测试用户");
    user.setEnabled(true);
    user.setFailedCount(0);
    return user;
  }

  @Override
  protected BaseDao<UserDO> getBaseDao() {
    return userDao;
  }

  @Override
  protected int getTotalRows() {
    return 3;
  }

  @Override
  protected UserDO getSavable() {
    var user = new UserDO();
    user.setEmail(getTestData("aabb@cc.dd"));
    user.setHashPassword("{bcrypt}$2a$10$OFszpBSOVJflK1gwrvKQCOgE7w/zn83amQak1.oRcF2302328i6yq");
    user.setPhoneNumber("13344449999");
    user.setUsername(getTestData("测试用户"));
    user.setEnabled(true);
    user.setFailedCount(0);
    return user;
  }

}
