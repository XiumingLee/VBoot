package cn.xiuminglee.vboot.core.common.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.List;

public class Node<T> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer parentid;
    @TableField(exist=false)
    private List<T> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", parentid=" + parentid +
                ", children=" + children +
                '}';
    }
}
