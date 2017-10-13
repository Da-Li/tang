package ${daoBasePackage}.${modulePackage}.entity;

<#list importContent as ic>
import ${ic};
</#list>

/**
 * Description: ${className} 实体
 * Created by ${username} on ${.now}
 */
public class ${className} {

	<#list columns as column>
	/**
	 * ${column.columnComment}
	 */
	private ${column.javaType} ${column.fieldName};
	</#list>


	//start get set

	<#list columns as column>

	public ${column.javaType} get${column.propertyName}() {
		return ${column.fieldName};
	}
	public void set${column.propertyName}(${column.javaType} ${column.fieldName}) {
		this.${column.fieldName} = ${column.fieldName};
	}

	</#list>

	//end get set


	//start your code 非生成代码请写在注释内

	//end your code
}