package cn.xiuminglee.vboot.modules.codegenerate.entity;

import java.util.Arrays;

/**
 * @author 22
 * GenerateCode实体类
 */
public class GenerateCode {
    private String codePath;
    private String packageName;
    private String author;
    private String tablePrefix;
    private String[] tableNames;

    public String getCodePath() {
        return codePath;
    }

    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public String[] getTableNames() {
        return tableNames;
    }

    public void setTableNames(String[] tableNames) {
        this.tableNames = tableNames;
    }

    @Override
    public String toString() {
        return "GenerateCode{" +
                "codePath='" + codePath + '\'' +
                ", packageName='" + packageName + '\'' +
                ", author='" + author + '\'' +
                ", tablePrefix='" + tablePrefix + '\'' +
                ", tableNames=" + Arrays.toString(tableNames) +
                '}';
    }
}
