package cn.navyd.app.supermarket.util.typehandler;

import static com.google.common.base.Preconditions.checkNotNull;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import cn.navyd.app.supermarket.util.EnumUtils;
import cn.navyd.app.supermarket.util.EnumUtils.EnumSequencer;

/**
 * mybatis自定义enum处理类。
 * <p>该类仅会处理实现了{@link EnumSequencer}的enum类，作用为映射sql int <==> {@link EnumSequencer#getSequence()}
 * @author navyd
 *
 * @param <E>
 */
public class EnumSequencerTypeHandler<E extends Enum<?> & EnumSequencer>
    extends BaseTypeHandler<E> {
  private Class<E> type;

  public EnumSequencerTypeHandler(Class<E> type) {
    this.type = checkNotNull(type);
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, E parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setInt(i, parameter.getSequence());
  }

  @Override
  public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
    int code = rs.getInt(columnName);
    return rs.wasNull() ? null : codeOf(code);
  }

  @Override
  public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    int code = rs.getInt(columnIndex);
    return rs.wasNull() ? null : codeOf(code);
  }

  @Override
  public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    int code = cs.getInt(columnIndex);
    return cs.wasNull() ? null : codeOf(code);
  }

  private E codeOf(int code) {
    var sequencer = EnumUtils.ofSequence(type, code);
    if (sequencer.isEmpty())
      throw new IllegalArgumentException(
          "Cannot convert " + code + " to " + type.getSimpleName() + " by code value.");
    return sequencer.get();
  }
}
