package com.example.testingmyskills.JavaClasses;
//import android.content.Context;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;


public class ExcelReaderUtil {

//    public static List<String[]> readExcelFile(Context context) {
//        List<String[]> data = new ArrayList<>();
//        try {
//            InputStream inputStream = context.getAssets().open("data");
//            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//            XSSFSheet sheet = workbook.getSheetAt(0);
//
//            int rows = sheet.getLastRowNum();
//            int cols = sheet.getRow(0).getLastCellNum();
//
//            for (int r = 0; r <= rows; r++) {
//                XSSFRow row = sheet.getRow(r);
//                if (row != null) {
//                    String[] rowData = new String[cols];
//                    for (int c = 0; c < cols; c++) {
//                        XSSFCell cell = row.getCell(c);
//                        if (cell != null) {
//                            switch (cell.getCellType()) {
//                                case STRING:
//                                    rowData[c] = cell.getStringCellValue();
//                                    break;
//                                case NUMERIC:
//                                    rowData[c] = String.valueOf(cell.getNumericCellValue());
//                                    break;
//                                case BOOLEAN:
//                                    rowData[c] = String.valueOf(cell.getBooleanCellValue());
//                                    break;
//                                case FORMULA:
//                                    rowData[c] = cell.getCellFormula();
//                                    break;
//                                case BLANK:
//                                    rowData[c] = "";
//                                    break;
//                                default:
//                                    rowData[c] = "Unsupported Cell Type";
//                                    break;
//                            }
//                        } else {
//                            rowData[c] = "";
//                        }
//                    }
//                    data.add(rowData);
//                }
//            }
//
//            workbook.close();
//            inputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return data;
//    }
}
