package com.migudm.tang.code_generate.vo;

import freemarker.template.utility.StringUtil;
import org.springframework.util.StringUtils;

public class ColumnInfo {

    private String columnName;
    private String columnComment;
    private String fieldComment;
    private String typeName;//表字段类型
    private int typeCode;
    private String javaType;
    private String fieldName;
    private String importContent;
//    private


    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnComment() {
        return StringUtils.isEmpty(columnComment)?"//TODO 请填写注释":columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getFieldComment() {
        return columnComment;
    }

    public void setFieldComment(String fieldComment) {
        this.fieldComment = fieldComment;
    }

    public String getFieldName() {
        return com.migudm.tang.code_generate.utils.StringUtils.camelName(columnName,false);
    }
    public String getPropertyName(){
        return com.migudm.tang.code_generate.utils.StringUtils.camelName(columnName,true);
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getImportContent() {
        if (javaType.contains(".")){
            return javaType;
        }
        return "java.lang."+javaType;
    }

    public void setImportContent(String importContent) {
        this.importContent = importContent;
    }
}
