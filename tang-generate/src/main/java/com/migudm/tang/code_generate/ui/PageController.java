package com.migudm.tang.code_generate.ui;

import com.migudm.tang.code_generate.core.GenerateFile;
import com.migudm.tang.code_generate.utils.DatabaseDataTypesUtils;
import com.migudm.tang.code_generate.utils.StringUtils;
import com.migudm.tang.code_generate.vo.ColumnInfo;
import com.migudm.tang.code_generate.vo.GenCodeContext;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lixiaoshenxian on 2017/9/30.
 */
public class PageController implements Initializable {

	@FXML
	private TextField urlTextField;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;
	@FXML
	private TextField hostnameField;
	@FXML
	private TextField webModuleTextField;
	@FXML
	private TextField locationTextField;
	@FXML
	private ChoiceBox schemaSelect;
	@FXML
	private ChoiceBox tableSelect;
	@FXML
	private Button openButton;


	@FXML
	private TableView<ColumnInfo> columnTable;

	private static Connection connection = null;


	private String databaseUrl = "jdbc:oracle:thin:@192.168.1.5:1521:ora";
	private String username = "dmgy_3";
	private String password = "hisun_123";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getConnection();
		initContent();
		initTableColumn();
	}
	public void initContent(){
		urlTextField.textProperty().setValue(databaseUrl);
		usernameField.textProperty().setValue(username);
		passwordField.textProperty().setValue(password);
		schemaSelect.setItems(FXCollections.observableArrayList(getSchemas()));
		schemaSelect.setValue("DMGY_3");

		schemaSelect.valueProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				onSchemaSelected(newValue.toString());
			}
		});

		tableSelect.setItems(FXCollections.observableArrayList(getTablesBySchema("DMGY_3")));
		tableSelect.valueProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				setTable(newValue.toString());
			}
		});
		hostnameField.setText(System.getProperty("user.name"));
		locationTextField.setText("请指定到cyoperate-dao/src");
	}

	/**
	 * 根据数据库信息获取schema
	 * @return
	 */
	public List<String> getSchemas(){
		return getAllSchema();
	}

	/**
	 * 当schema选择完，根据schema 获取表名
	 */
	public void onSchemaSelected(String schema){
		tableSelect.setItems(FXCollections.observableArrayList(getTablesBySchema(schema)));
		///TODO 显示不出来
	}

	public void openFileChooser() {
		DirectoryChooser directoryChooser = new DirectoryChooser();

		directoryChooser.setTitle("选择生成目录");
		File selectedFolder = directoryChooser.showDialog(openButton.getScene().getWindow());
		if (selectedFolder != null) {
			locationTextField.setText(selectedFolder.getAbsolutePath());
		}
	}

	public void genCode(){
		GenCodeContext.put("username",hostnameField.getText());
		GenCodeContext.put("modulePackage",webModuleTextField.getText());
		GenCodeContext.put("modulePath",webModuleTextField.getText().replace(".",File.separator));
		GenCodeContext.put("locationPath",locationTextField.getText());
		GenCodeContext.put("daoBasePackage","com.migudm.dao");

		GenCodeContext.put("tableName",tableSelect.getValue());
		GenCodeContext.put("className", StringUtils.camelName(tableSelect.getValue().toString(),true));

		List<ColumnInfo> columnInfoList = (List<ColumnInfo>)GenCodeContext.get("columns");
		Set<String> importContent = new HashSet<>();
		columnInfoList.stream().forEach(e->
			importContent.add(e.getImportContent())
		);
		GenCodeContext.put("importContent",importContent);
		GenerateFile.genFile();

	}

	private static void getConnection(){

		String url = "jdbc:oracle:thin:@192.168.1.5:1521:ora";
		String username = "dmgy_3";
		String password = "hisun_123";
		Properties properties = new Properties();
		properties.setProperty("user", username);
		properties.setProperty("password", password);
		properties.setProperty("remarks", "true");

		try {
			connection = DriverManager.getConnection(url,properties);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (connection!=null){
					connection.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private static List<String> getAllSchema() {
		try {
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet allSchemas = databaseMetaData.getSchemas();
			List<String> schemas = new ArrayList<>();
			while (allSchemas.next()){
				schemas.add(allSchemas.getString(1));
			}
			return schemas;
		} catch (SQLException e) {
			e.printStackTrace();
			if (connection != null){
				try {
					connection.close();
				} catch (SQLException e1) {
					e.printStackTrace();
				}
			}
		}
		return new ArrayList<>();
	}

	private static List<String> getTablesBySchema(String schema){
		try {
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet tablesResultSet = databaseMetaData.getTables(null,schema,null,null);
			List<String> tables = new ArrayList<>();
			while (tablesResultSet.next()){
				tables.add(tablesResultSet.getString("TABLE_NAME"));
			}
			return tables;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	private void setTable(String tableName){
		List<ColumnInfo> columnInfoList = getColumnInfo("DMGY_3",tableName);
		//TODO 需要ui界面修改
		GenCodeContext.put("columns",columnInfoList);
		columnTable.setItems(FXCollections.observableArrayList(columnInfoList));
	}

	private static List<ColumnInfo> getColumnInfo(String schema,String tableName) {
		try {
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet columnResultSet = databaseMetaData.getColumns(null,schema,tableName,null);
			ResultSetMetaData resultSetMetaData = columnResultSet.getMetaData();
			ResultSet pkResultSet = databaseMetaData.getPrimaryKeys(null,schema,tableName);
			List<ColumnInfo> columnInfoList = new ArrayList<>();
			boolean hasPk = pkResultSet.next();
			if (!hasPk){
				System.out.println("表中没有主键约束");
				return new ArrayList<>();
			}
			final String pkColumnName = pkResultSet.getString("COLUMN_NAME");

			while (columnResultSet.next()){
				ColumnInfo columnInfo = new ColumnInfo();
				columnInfo.setColumnComment(columnResultSet.getString("REMARKS"));
				columnInfo.setColumnName(columnResultSet.getString("COLUMN_NAME"));
				columnInfo.setTypeCode(columnResultSet.getInt("DATA_TYPE"));
				columnInfo.setTypeName(JDBCType.valueOf(columnResultSet.getInt("DATA_TYPE")).getName());

				String javaType = DatabaseDataTypesUtils.getPreferredJavaType(
						columnInfo.getTypeCode(),columnResultSet.getInt("COLUMN_SIZE"),
						0
				);
				columnInfo.setJavaType(javaType);
				columnInfoList.add(columnInfo);
			}
			columnInfoList.forEach(e ->{
				if (e.getColumnName().equals(pkColumnName)){
					GenCodeContext.put("pkColumnName",e.getColumnName());
					GenCodeContext.put("pkFieldName",e.getFieldName());
					GenCodeContext.put("pkTypeName",e.getTypeName());
					GenCodeContext.put("pkJavaType",e.getJavaType());
				}
			});
			return columnInfoList;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	private void initTableColumn(){
		//TODO 可编辑，显示字段再确定
		columnTable.getVisibleLeafColumn(0).setCellValueFactory(new PropertyValueFactory("columnName"));
		columnTable.getVisibleLeafColumn(1).setCellValueFactory(new PropertyValueFactory("columnComment"));
		columnTable.getVisibleLeafColumn(2).setCellValueFactory(new PropertyValueFactory("fieldComment"));
	}

	public static void main(String[] args) {
		getConnection();
		List<ColumnInfo> columnInfoList = getColumnInfo("DMGY_3","TB_KEY_MASKING");
		System.out.println(File.separator);
	}

}
