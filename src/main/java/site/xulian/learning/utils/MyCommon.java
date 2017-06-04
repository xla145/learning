package site.xulian.learning.utils;

import com.jfinal.ext.render.csv.CsvRender;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class MyCommon {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/**
	 * 导出大数据文件
	 * */
	public static CsvRender exportCsvRender(String[] header, String [] column, List<Record> list, String fileName){
		List<String> headers = new ArrayList<String>();//excel头部信息
		List<Object> newsList = new ArrayList<Object> ();
 	    for(String s:header){
 	    	headers.add(s);
 	    }
 	    List<String> clomuns = new ArrayList<String>();
 		for(String s:column){
 			clomuns.add(s);
 		}
 		for (Record s: list){
            newsList.add(s);
		}
		return CsvRender.me(headers,newsList).clomuns(clomuns).fileName(fileName);
	}
	public static List<String []> readExcel(File excelFile, String excelFileName)
	{
		List<String []> list = new ArrayList<String []>();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DecimalFormat df = new DecimalFormat("0");
		try {
			FileInputStream is = new FileInputStream(excelFile); //文件流
			Workbook workbook = createWorkBook(is, excelFileName);
			int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量
			//遍历每个Sheet
			for (int s = 0; s < sheetCount; s++) {
				Sheet sheet = workbook.getSheetAt(s);
				if (sheet == null) {
					continue;
				}
				int rowCount= sheet.getPhysicalNumberOfRows(); //获取总行数
				//遍历每一行
				for (int r = 0; r < rowCount; r++) {
					Row row = sheet.getRow(r);
					if (row == null) {
						continue;
					}
					int cellCount = row.getPhysicalNumberOfCells(); //获取总列数
					//遍历每一列
					String[] datas = new String[cellCount];
					for (int c = 0; c < cellCount; c++) {
						Cell cell = row.getCell(c);
						if(cell != null){
							int cellType = cell.getCellType();
							String cellValue = null;
							switch(cellType) {
								case Cell.CELL_TYPE_STRING: //文本
									cellValue = cell.getStringCellValue();
									break;
								case Cell.CELL_TYPE_NUMERIC: //数字、日期
									if(DateUtil.isCellDateFormatted(cell)) {
										cellValue = fmt.format(cell.getDateCellValue()); //日期型
									}else{
										cellValue=df.format(cell.getNumericCellValue());
									}
									break;
								case Cell.CELL_TYPE_BOOLEAN: //布尔型
									cellValue = String.valueOf(cell.getBooleanCellValue());
									break;
								case Cell.CELL_TYPE_BLANK: //空白
									//cellValue = cell.getStringCellValue();
									continue;
								case Cell.CELL_TYPE_ERROR: //错误
									cellValue = "错误";
									break;
								case Cell.CELL_TYPE_FORMULA: //公式
									cellValue = "错误";
									break;
								default:
									cellValue = "错误";
							}
							if(sheetCount > 1){
								datas[c+1] = cellValue;
							}else{
								datas[c] = cellValue;
							}
						}
					}
					if(StringUtils.join(datas, "").trim().length() != 0){
						list.add(datas);
					}
					System.out.println();
				}

			}
			System.out.println("list"+ JsonKit.toJson(list));
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}finally{
			excelFile.delete();
		}
		return list;
	}

    public static Workbook createWorkBook(InputStream is, String excelFileName) throws IOException {
        if(excelFileName.toLowerCase().endsWith("xls")){
            return new HSSFWorkbook(is);
        }
        if(excelFileName.toLowerCase().endsWith("xlsx")){
            return new XSSFWorkbook(is);
        }
        return null;
    }
}
