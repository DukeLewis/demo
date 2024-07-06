import java.io.*;
import java.sql.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @description:
 * @author：dukelewis
 * @date: 2024/7/6
 * @Copyright： https://github.com/DukeLewis
 */
public class Main {
    public static void main(String[] args) {
        String excelFilePath = "data.xlsx";
        String zipFilePath = "./file-process/data.zip";

        String jdbcURL = "jdbc:mysql://localhost:3306/test";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            String sql = "SELECT * FROM mqtt_data";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // 创建Excel文件
            // Excel工作簿
            Workbook workbook = new XSSFWorkbook();
            // Excel工作表
            Sheet sheet = workbook.createSheet("Sheet1");

            // Excel中的行
            Row headerRow = sheet.createRow(0);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                // Excel中的单元格
                Cell cell = headerRow.createCell(i - 1);
                cell.setCellValue(metaData.getColumnName(i));
            }

            int rowIndex = 1;
            while (resultSet.next()) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 1; i <= columnCount; i++) {
                    Cell cell = row.createCell(i - 1);
                    cell.setCellValue(resultSet.getString(i));
                }
            }

            // 将Excel文件写入字节输出流（缓冲区）
            ByteArrayOutputStream excelOutputStream = new ByteArrayOutputStream();
            workbook.write(excelOutputStream);
            workbook.close();

            // 创建ZIP文件并将Excel文件写入ZIP文件
            ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
            // 创建 zip 文件
            ZipOutputStream zipOut = new ZipOutputStream(zipOutputStream);
            // 创建 excel 文件条目
            ZipEntry zipEntry = new ZipEntry(excelFilePath);
            // 添加文件条目
            zipOut.putNextEntry(zipEntry);
            // 将数据的 excel 文件写入 zip 文件条目
            zipOut.write(excelOutputStream.toByteArray());
            // 关闭 zip 文件条目添加
            zipOut.closeEntry();
            // 关闭 zip 文件写入流
            zipOut.close();

            // 将ZIP文件写入服务器，字节流写入文件
            try (FileOutputStream fos = new FileOutputStream(zipFilePath)) {
                zipOutputStream.writeTo(fos);
            }

            System.out.println("Excel and ZIP files have been created successfully.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
