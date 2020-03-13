package cn.xiuminglee.vboot.core.common.entity;

import java.util.List;

public class MenuOption extends Node<MenuOption> {


    private Integer id;
    private String value;
    private String label;
    private Integer parentid;
    private List<MenuOption> children;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public Integer getParentid() {
        return parentid;
    }

    @Override
    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    @Override
    public List<MenuOption> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<MenuOption> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "MenuOption{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", label='" + label + '\'' +
                ", parentid=" + parentid +
                ", children=" + children +
                '}';
    }
}
