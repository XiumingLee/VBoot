package cn.xiuminglee.vboot.modules.system.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author xiuminglee
 * @since 2018-09-01
 */
@TableName("vb_dict")
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 标识
     */
    private Integer num;
    /**
     * 父级id
     */
    private Integer parentid;
    /**
     * 属性名称
     */
    private String name;
    /**
     * 备注
     */
    private String tips;

    /**
     * 用于存放内容
     */
    @TableField(exist=false)
    private List<Dict> children;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public List<Dict> getChildren() {
        return children;
    }

    public void setChildren(List<Dict> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }


    @Override
    public String toString() {
        return "Dict{" +
                "id=" + id +
                ", num=" + num +
                ", parentid=" + parentid +
                ", name='" + name + '\'' +
                ", tips='" + tips + '\'' +
                ", children=" + children +
                '}';
    }
}
