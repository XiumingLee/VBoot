package cn.xiuminglee.vboot.modules.system.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author xiuminglee
 * @since 2018-08-31
 */
@TableName("vb_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 角色名称（英文）
     */
    private String name;
    /**
     * 角色名称（中文）
     */
    @TableField("nameZh")
    private String nameZh;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    @Override
    public String toString() {
        return "Role{" +
        ", id=" + id +
        ", name=" + name +
        ", nameZh=" + nameZh +
        "}";
    }
}
