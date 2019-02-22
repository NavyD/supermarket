package cn.navyd.app.supermarket.base;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BaseDO implements Serializable, PrimaryKey {
    /**
     * 
     */
    private static final long serialVersionUID = -5900214769066177947L;
    private Integer id;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
