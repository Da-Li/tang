package ${daoBasePackage}.${modulePackage}.mapper;

import ${daoBasePackage}.${modulePackage}.entity.${className};

/**
 * Description: ${className} 实体
 * Created by ${username} on ${.now}
 */
public interface ${className}Mapper {

	int deleteByPrimaryKey(${pkJavaType} ${pkFieldName});


	int insert(${className} record);


	int insertSelective(${className} record);


	${className} selectByPrimaryKey(${pkJavaType} termsId);


	int updateByPrimaryKeySelective(${className} record);


	int updateByPrimaryKey(${className} record);
}