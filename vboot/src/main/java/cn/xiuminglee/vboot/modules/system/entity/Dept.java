package cn.xiuminglee.vboot.modules.system.entity;

import cn.xiuminglee.vboot.core.common.entity.Node;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiuminglee
 * @since 2018-09-24
 */
@TableName("vb_dept")
public class Dept extends Node<Dept> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 父级部门id
     */
    private Integer parentid;
    /**
     * 是否删除
     */
    private Integer delete;

    /**
     * 子部门的集合
     */
    @TableField(exist=false)
    private List<Dept> children;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getParentid() {
        return parentid;
    }

    @Override
    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public Integer getDelete() {
        return delete;
    }

    public void setDelete(Integer delete) {
        this.delete = delete;
    }

    @Override
    public List<Dept> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<Dept> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentid=" + parentid +
                ", delete=" + delete +
                ", children=" + children +
                '}';
    }
}
