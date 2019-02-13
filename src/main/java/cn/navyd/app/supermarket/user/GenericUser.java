package cn.navyd.app.supermarket.user;

import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.beans.BeanUtils;
import cn.navyd.app.supermarket.util.SecurityUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class GenericUser extends AbstractFailedCountUserLocker implements User {
  /**
   * 
   */
  private static final long serialVersionUID = -2049615933010515629L;
  
  private Integer id;
  private String username;
  private String password;
  private String email;
  private Boolean enabled;
  private String phoneNumber;
  private Integer failedCount;
  private LocalDateTime lockedTime;
  
  @Override
  public boolean isEnabled() {
    return enabled;
  }
  
  public static GenericUser of(UserDO that) {
    GenericUser user = new GenericUser();
    BeanUtils.copyProperties(that, user);
    user.setPassword(that.getHashPassword());
    user.setLockedTime(that.getGmtModified());
    return user;
  }
  
  public static void main(String[] args) {
    Random rand = new Random();
    String username = "测试者_" + rand.nextInt(100000);
    String password = "" + rand.nextInt(100000);
    String email = rand.nextInt(1000000000) + "@a.com";
    String phoneNumber = "" + rand.nextInt(100000000);
    String icon = "/" + rand.nextInt(100000000);
    var user = new UserDO();
    user.setEmail(email);
    user.setEnabled(rand.nextBoolean());
    user.setHashPassword(SecurityUtils.md5(password));
    user.setIconPath(icon);
    user.setPhoneNumber(phoneNumber);
    user.setUsername(username);
    user.setFailedCount(1);
    user.setId(1);
    user.setGmtCreate(LocalDateTime.now());
    user.setGmtModified(LocalDateTime.now());
    
    var gu = of(user);
    System.err.println(gu);
  }
  
}
