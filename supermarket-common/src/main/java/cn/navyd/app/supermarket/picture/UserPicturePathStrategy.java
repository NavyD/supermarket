package cn.navyd.app.supermarket.picture;

import java.nio.file.Path;

public interface UserPicturePathStrategy extends PicturePathStrategy<Integer> {
  /**
   * 获取用户头像文件路径
   * @param user
   * @return
   */
  Path getProfilePhotoPath(Integer id);
}
