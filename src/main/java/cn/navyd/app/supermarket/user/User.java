package cn.navyd.app.supermarket.user;

import java.io.Serializable;
import cn.navyd.app.supermarket.base.PrimaryKey;

/**
 * 定义了user的基本信息
 * @author navyd
 *
 */
public interface User extends PrimaryKey, Serializable {
  String getUsername();
  
  String getPassword();
  
  String getEmail();
  
  String getPhoneNumber();
  
  boolean isEnabled();
}