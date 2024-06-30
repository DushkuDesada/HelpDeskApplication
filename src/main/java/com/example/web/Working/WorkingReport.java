package com.example.web.Working;

import com.example.domain.WorkingHour;
import com.example.ejb.EjbWorkingHourRepository;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Named("WorkingReport")
@SessionScoped
public class WorkingReport implements Serializable {
    private String projectId;
    private String userId;
    private Date stDate;
    private Date edDate;

    public Date getStDate() {
        return stDate;
    }

    public void setStDate(Date stDate) {
        this.stDate = stDate;
    }

    public Date getEdDate() {
        return edDate;
    }

    public void setEdDate(Date edDate) {
        this.edDate = edDate;
    }

    // Inject your repository or service
    @Inject
    private EjbWorkingHourRepository workingHourRepository;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void exportToExcel() throws IOException {
        // Retrieve HTTP response object
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        // Set response headers for Excel file
        response.addHeader("Content-disposition", "attachment; filename=working_hours.xls");
        response.setContentType("application/vnd.ms-excel");

        try (HSSFWorkbook workbook = new HSSFWorkbook()) { // Create an Excel workbook
            HSSFSheet sheet = workbook.createSheet("Working Hours"); // Create a sheet named "Working Hours"

            // Create the header row
            HSSFRow headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Task Name");
            headerRow.createCell(1).setCellValue("Project Name");
            headerRow.createCell(2).setCellValue("Start Time");
            headerRow.createCell(3).setCellValue("End Time");

            // Set header cell style
            HSSFCellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setAlignment(HorizontalAlignment.RIGHT);

            for (int i = 0; i < 4; i++) {
                headerRow.getCell(i).setCellStyle(headerCellStyle);
            }

            // Retrieve data from repository based on project ID or user ID
            List<WorkingHour> workingHours = workingHourRepository.findAll();

            if (projectId != null && !projectId.isEmpty()) {
                System.out.println("project");
                System.out.println(projectId);
                workingHours = workingHours.stream()
                        .filter(wh -> wh.getProject().getId().equals(UUID.fromString(projectId)))
                        .collect(Collectors.toList());
            }

            // Filter by userId if it's not null or empty
            if (userId != null && !userId.isEmpty()) {
                System.out.println("user");
                System.out.println(userId);
                workingHours = workingHours.stream()
                        .filter(wh -> wh.getUser().getId().equals(UUID.fromString(userId)))
                        .collect(Collectors.toList());
            }

            // Filter by date range if both dates are provided
            if (stDate != null && edDate != null) {
                System.out.println("date range");
                System.out.println(stDate);
                System.out.println(edDate);
                LocalDate startLocalDate = stDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endLocalDate = edDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                workingHours = workingHours.stream()
                        .filter(wh -> {
                            LocalDate whStartDate = wh.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            LocalDate whEndDate = wh.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            return (whStartDate.isEqual(startLocalDate) || whStartDate.isAfter(startLocalDate)) &&
                                    (whEndDate.isEqual(endLocalDate) || whEndDate.isBefore(endLocalDate));
                        })
                        .collect(Collectors.toList());
            }

            if (workingHours == null || workingHours.isEmpty()) {
                System.out.println("No working hours data found.");
            } else {
                // Populate the data rows
                int rowNum = 1;
                for (WorkingHour workingHour : workingHours) {
                    HSSFRow row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(workingHour.getTask().getTitle());
                    row.createCell(1).setCellValue(workingHour.getProject().getName());
                    row.createCell(2).setCellValue(workingHour.getStartTime().toString());
                    row.createCell(3).setCellValue(workingHour.getEndTime().toString());
                }

                // Auto-size the columns
                for (int i = 0; i < 4; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Write the workbook to the output stream
                OutputStream out = response.getOutputStream();
                workbook.write(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.getOutputStream().flush();
        FacesContext.getCurrentInstance().responseComplete();
    }
}