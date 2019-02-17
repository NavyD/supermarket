package cn.navyd.app.supermarket.util;

import static com.google.common.base.Preconditions.checkNotNull;

public class EnumUtils {
  /**
   * 一个定序器，实现该接口的enum可以表示某种特定的顺序
   * @author navyd
   *
   */
  public interface EnumSequencer {
    /**
     * 返回顺序值
     * @return
     */
    int getSequence();
  }
  
  /**
   * 如果指定的class存在重复的sequence则抛出异常
   * @param clazz
   */
  public static <T extends Enum<?> & EnumSequencer> void checkUniqueEnumSequencer(Class<T> clazz) {
    if (hasRepeatedSequence(clazz))
      throw new IllegalArgumentException(clazz.getName() + " 存在重复的sequence");
  }

  /**
   * 如果指定的enum class存在重复的序列值则返回true。该方法要求class实现{@link Sequencer}接口
   * @param clazz
   * @return
   */
  public static <T extends Enum<?> & EnumSequencer> boolean hasRepeatedSequence(Class<T> clazz) {
    checkNotNull(clazz);
    T[] enumConsts = clazz.getEnumConstants();
    // 查找是否存在重复元素
    for (int i = 0; i < enumConsts.length; i++) {
      T e1 = enumConsts[i];
      for (int j = 0; j < enumConsts.length; j++) {
        if (i == j)
          continue;
        T e2 = enumConsts[j];
        if (e1.getSequence() == e2.getSequence())
          return true;
      }
    }
    return false;
  }
}
