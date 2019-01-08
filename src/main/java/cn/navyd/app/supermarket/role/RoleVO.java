package cn.navyd.app.supermarket.role;

import lombok.Data;

@Data
public class RoleVO {
    private Integer id;
    private String name;
    private RoleVO parentRole;
}
