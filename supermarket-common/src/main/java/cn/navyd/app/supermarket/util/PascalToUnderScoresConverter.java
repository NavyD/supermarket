package cn.navyd.app.supermarket.util;

import static com.google.common.base.Preconditions.checkNotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PascalToUnderScoresConverter {
  private static final String REGEX_PASCAL = "([a-z]*)([A-Z])";
  private static final Pattern PATTERN_PASCAL = Pattern.compile(REGEX_PASCAL);
  private static final PascalToUnderScoresConverter INSTANCE = new PascalToUnderScoresConverter();
  
  private PascalToUnderScoresConverter() {
    
  }
  
  public static PascalToUnderScoresConverter getInstance() {
    return INSTANCE;
  }
  
  /**
   * 将指定的bean class的字段名称转换为under_scores的字符串集合
   * @param beanClazz
   * @return
   */
  public Collection<String> convert(Class<?> beanClazz) {
    checkNotNull(beanClazz);
    // 获取所有字段
    Collection<Field> fields = new ArrayList<>(10);
    do {
      for (Field f : beanClazz.getDeclaredFields()) {
        int mod = f.getModifiers();
        // 非static 
        if (!Modifier.isStatic(mod))
          fields.add(f);
      }
    } while ((beanClazz = beanClazz.getSuperclass()) != Object.class);
    // 匹配pascal名称
    Collection<String> underScores = new HashSet<>(fields.size());
    fields.forEach(field -> {
      String fieldName = field.getName();
      Matcher m = PATTERN_PASCAL.matcher(fieldName);
      String underScoreName = toUnderScore(m, fieldName);
      underScores.add(underScoreName);
    });
    return underScores;
  }
  
  /**
   * 将指定的name转换为under_score类型的name
   * @param m
   * @param name
   * @return
   */
  private String toUnderScore(Matcher m, String name) {
    StringBuilder sb = new StringBuilder();
    while (m.find()) {
      m.appendReplacement(sb, "$1_".concat(m.group(2).toLowerCase()));
    }
    m.appendTail(sb);
    return sb.toString();
  }
  
}
