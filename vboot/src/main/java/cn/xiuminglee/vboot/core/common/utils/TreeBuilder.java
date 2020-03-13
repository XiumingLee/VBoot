package cn.xiuminglee.vboot.core.common.utils;

import cn.xiuminglee.vboot.core.common.entity.Node;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 22
 * 根据id，parentid，构建树形结构，放到children
 */
public class TreeBuilder<T extends Node> {
    List<T> nodes = new ArrayList<T>();
    public String buildTree(List<T> nodes) {
        TreeBuilder treeBuilder = new TreeBuilder(nodes);
        return treeBuilder.buildJSONTree();
    }

    public TreeBuilder() {
    }

    public TreeBuilder(List<T> nodes) {
        super();
        this.nodes = nodes;
    }
    // 构建JSON树形结构
    private String buildJSONTree() {
        List<T> nodeTree = buildTree();
        JSONArray jsonArray = (JSONArray) JSONArray.toJSON(nodeTree);
        return jsonArray.toString();
    }

    // 构建树形结构
    private List<T> buildTree() {
        List<T> treeNodes = new ArrayList<T>();
        List<T> rootNodes = getRootNodes();
        for (T rootNode : rootNodes) {
            buildChildNodes(rootNode);
            treeNodes.add(rootNode);
        }
        return treeNodes;
    }
    // 递归子节点
    private void buildChildNodes(T node) {
        List<T> children = getChildNodes(node);
        if (!children.isEmpty()) {
            for (T child : children) {
                buildChildNodes(child);
            }
            node.setChildren(children);
        }
    }

    // 获取父节点下所有的子节点
    private List<T> getChildNodes(T pnode) {
        List<T> childNodes = new ArrayList<T>();
        for (T n : nodes) {
            if (pnode.getId().equals(n.getParentid())) {
                childNodes.add(n);
            }
        }
        return childNodes;
    }

    // 判断是否为根节点
    private boolean rootNode(T node) {
        boolean isRootNode = true;
        for (T n : nodes) {
            if (node.getParentid().equals(n.getId())) {
                isRootNode = false;
                break;
            }
        }
        return isRootNode;
    }

    // 获取集合中所有的根节点
    private List<T> getRootNodes() {
        List<T> rootNodes = new ArrayList<T>();
        for (T n : nodes) {
            if (rootNode(n)) {
                rootNodes.add(n);
            }
        }
        return rootNodes;
    }
}
