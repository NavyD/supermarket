package cn.navyd.app.supermarket.picture;

import java.nio.file.Path;
import java.util.Collection;
import cn.navyd.app.supermarket.user.UserDO;

public interface ProductPicturePathStrategy extends PicturePathStrategy<Integer> {
  /**
   * 获取主图文件路径
   * @param user
   * @return
   */
  Path getMainPicturePath(UserDO user);
  /**
   *  获取其他图片文件的路径，
   * @param user
   * @return
   */
  Collection<Path> getSubPicturePaths(UserDO user);
}
