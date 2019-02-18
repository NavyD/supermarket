package cn.navyd.app.supermarket.picture;

import java.nio.file.Path;

/**
 * 一个图片路径策略接口。该接口定义了图片文件夹的路径
 * @author navyd
 *
 * @param <T>
 */
public interface PicturePathStrategy<T> {
  /**
   * 获取图片文件夹所在路径
   * @param key
   * @return
   */
  Path getPicturePath(T key);
}
