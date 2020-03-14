package cn.xiuminglee.vboot.core.common.utils;

import cn.xiuminglee.vboot.core.common.entity.Node;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author Xiuming Lee
 * @Description
 */
public class TreeBuilder2 {
    public static <T extends Node> T builder(List<T> nodes) {
        T rootNode = (T) new Node();
        Multimap<Integer, T> pidMultimap = ArrayListMultimap.create();
        for (T node : nodes) {
            pidMultimap.put(node.getParentid(), node);
        }
        rootNode.setChildren(buildTree(rootNode, pidMultimap));
        return rootNode;
    }

    private static <T extends Node> List<T> buildTree(T parentNode, Multimap<Integer, T> pidMultimap) {
        //获得父级ID
        Integer currentPid = parentNode.getId();
        if (!pidMultimap.containsKey(currentPid)) {
            return null;
        }
        // 获取相同pid的所有数据;removeAll移除并返回。
        List<T> currentPidDataList = new ArrayList<>(pidMultimap.removeAll(currentPid));

        // 遍历，递归，
        for (T node : currentPidDataList) {
            node.setChildren(buildTree(node, pidMultimap));
        }
        //将数据挂到父级节点
        return currentPidDataList;

    }
}
