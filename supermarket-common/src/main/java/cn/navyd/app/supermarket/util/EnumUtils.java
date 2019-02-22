package cn.navyd.app.supermarket.util;

import static com.google.common.base.Preconditions.checkNotNull; 
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
   * @param enumSequencerClass
   */
  public static <T extends Enum<?> & EnumSequencer> void checkUniqueEnumSequencer(Class<T> enumSequencerClass) {
    if (hasRepeatedSequence(enumSequencerClass))
      throw new IllegalArgumentException(enumSequencerClass.getName() + " 存在重复的sequence");
  }

  /**
   * 如果指定的enum class存在重复的序列值则返回true。该方法要求class实现{@link EnumSequencer}接口
   * @param enumSequencerClass
   * @return
   */
  public static <T extends Enum<?> & EnumSequencer> boolean hasRepeatedSequence(Class<T> enumSequencerClass) {
    checkNotNull(enumSequencerClass);
    T[] enumConsts = enumSequencerClass.getEnumConstants();
    // 查找是否存在重复元素 使用hash set去重
    Set<EnumSequencer> sequencers = new HashSet<>(enumConsts.length);
    for (T e : enumConsts) {
      boolean isSuccess = sequencers.add(new HashableEnumSequencerProxy(e));
      if (!isSuccess) {
        log.debug("{}.{}.sequence={} 已存在", enumSequencerClass.getSimpleName(), e, e.getSequence());
        return true;
      }
    }
    return false;
  }
  
  /**
   * 用于hash实现重复查找算法。 
   * @author navyd
   *
   */
  @RequiredArgsConstructor
  private static class HashableEnumSequencerProxy implements EnumSequencer {
    private final EnumSequencer enumSequencer;
    
    @Override
    public int getSequence() {
      return enumSequencer.getSequence();
    }
    
    @Override
    public boolean equals(Object obj) {
      return obj instanceof EnumSequencer
          && getSequence() == ((EnumSequencer) obj).getSequence();
    }
    
    @Override
    public int hashCode() {
      return getSequence();
    }
  }
}


