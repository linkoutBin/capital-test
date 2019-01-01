package com.testfork.wechat_gate.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.testfork.wechat_gate.domain.BankTemplate;

public class ExcelTool<T> {
	private static Logger logger = LoggerFactory.getLogger(ExcelTool.class);
	private static final String SUFFIX_2003 = ".xls";
	private static final String SUFFIX_2007 = ".xlsx";
	private String filePath;
	private Class<T> clazz;
	private Map<String, String> fieldMap;

	/**
	 * 
	 * @param fieldMap
	 *            表格首行标题与内存中实例的set方法映射关系
	 * @param path
	 *            需要读入的表格文件全路径
	 * @param clazz
	 *            表格中数据行对应的内存实例类型
	 */
	public ExcelTool(Map<String, String> fieldMap, String path, Class<T> clazz) {
		this.fieldMap = fieldMap;
		this.filePath = path;
		this.clazz = clazz;
	}

	/**
	 * description: 初始化工作簿
	 * 
	 * @return workbook
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	private Workbook initWorkBook() throws IOException {
		File file = new File(filePath);
		InputStream is = new FileInputStream(file);

		Workbook workbook = null;
		// 根据后缀，得到不同的Workbook子类，即HSSFWorkbook或XSSFWorkbook
		if (filePath.endsWith(SUFFIX_2003)) {
			workbook = new HSSFWorkbook(is);
		} else if (filePath.endsWith(SUFFIX_2007)) {
			workbook = new XSSFWorkbook(is);
		} else {
			throw new RuntimeException("不支持的文件类型");
		}
		logger.info("工作簿初始化結束，待解析文件:{}\n实例类型:{}", filePath, clazz.getName());
		return workbook;
	}

	/**
	 * description: 读取工作簿中的内容行并输出到指定集合
	 * 
	 * @param modelList
	 * @return
	 * @throws IOException
	 */
	void parseWorkbook(List<T> modelList) throws IOException {
		Workbook workbook = initWorkBook();
		int numOfSheet = workbook.getNumberOfSheets();
		if (numOfSheet > 0) {
			Sheet sheet = workbook.getSheetAt(0);
			parseSheet(sheet, modelList);
		}
	}

	private List<Method> set_methods;

	private void parseSheet(Sheet sheet, List<T> modelList) {
		Row row;
		int count = 0;
		boolean isTitle = true;

		// 利用迭代器，取出每一个Row
		Iterator<Row> iterator = sheet.iterator();
		while (iterator.hasNext()) {
			row = iterator.next();

			// 由于第一行是标题，因此这里单独处理
			if (isTitle) {
				set_methods = new ArrayList<>();
				parseRowAndFindMethod(row, clazz);
				isTitle = false;
			} else if (row.getCell(0).getCellTypeEnum().equals(CellType.BLANK)) {
				// 判断空行
				continue;
			} else {
				// 其它行都在这里处理
				parseRowAndFillData(row, modelList, clazz);
				count++;
			}
		}
		logger.info("最终处理的数据行数为:{}", count);
	}

	private void parseRowAndFindMethod(Row row, Class<T> clazz) {
		// 利用parseRow处理每一行，得到每个cell中的String
		List<String> rst = parseRow(row);

		String methodName;
		try {
			// 根据String得到需要调用的model中的方法
			logger.info("###########开始解析excel标题############");
			for (String str : rst) {
				methodName = "set" + fieldMap.get(str);
				// 反射拿到method
				logger.info("####表格标题【{}】:实例类方法【{}】", str, methodName);
				set_methods.add(clazz.getDeclaredMethod(methodName, String.class));
			}
			logger.info("###########excel标题解析完成############");
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("解析表格首行标题异常,未找到标题对应的set方法", e);
		}

	}

	// 开始解析具体的数据
	private void parseRowAndFillData(Row row, List<T> modelList, Class<T> clazz) {
		// 同样利用parseRow得到具体每一行的数据
		List<String> rst = parseRow(row);

		T model = null;
		try {
			model = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			// TODO Auto-generated catch block
			throw new RuntimeException("实例化表格行对象失败", e1);
		}

		// 防止最後一個单元格数据为空
		int num = set_methods.size();
		if (set_methods.size() > rst.size()) {
			num = rst.size();
		}
		// 利用反射，将数据填充到具体的model
		try {
			for (int i = 0; i < num; ++i) {
				set_methods.get(i).invoke(model, rst.get(i));
			}

			// 保存到输出结果中
			modelList.add(model);
		} catch (Exception e) {
			throw new RuntimeException("反射方法参数设置异常", e);
		}
	}

	// 这里是解析每一行的代码
	private List<String> parseRow(Row row) {
		List<String> rst = new ArrayList<>();

		Cell cell;
		// 利用迭代器得到每一个cell
		Iterator<Cell> iterator = row.iterator();
		while (iterator.hasNext()) {
			cell = iterator.next();

			// 定义每一个cell的数据类型
			cell.setCellType(CellType.STRING);

			// 取出cell中的value
			rst.add(cell.getStringCellValue());
		}

		return rst;
	}

	public static void main(String[] args) {

		String path = "E:/Data/workbook/2018/wechat-gate/src/main/resources/BlackList_template.xls";
		ExcelTool tool = new ExcelTool<>(FieldMapUtil.TEST_FIELD, path, BankTemplate.class);
		List<BankTemplate> list = new ArrayList<>();
		try {
			tool.parseWorkbook(list);
			for (BankTemplate bank : list) {
				System.out.println(bank);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("解析失敗");
		}
	}
}