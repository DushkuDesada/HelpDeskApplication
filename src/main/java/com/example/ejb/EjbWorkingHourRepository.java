package com.example.ejb;

import com.example.domain.Project;
import com.example.domain.WorkingHour;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Stateless
@TransactionAttribute
public class EjbWorkingHourRepository extends EntityRepository<WorkingHour, UUID> implements Serializable {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public List<WorkingHour> findByTask(UUID taskId) {
        return entityManager.createQuery("SELECT t FROM WorkingHour t WHERE t.task.id = :taskId", WorkingHour.class)
                .setParameter("taskId", taskId)
                .getResultList();
    }

    public List<WorkingHour> findByUserIdAndDateRange(UUID userId, Date startDate, Date endDate) {
        TypedQuery<WorkingHour> query = entityManager.createQuery(
                "SELECT wh FROM WorkingHour wh WHERE wh.user.id = :userId AND wh.startTime BETWEEN :startDate AND :endDate",
                WorkingHour.class
        );
        query.setParameter("userId", userId);

        List<WorkingHour> result = query.getResultList();
        System.out.println("findByUserIdAndDateRange: " + result.size() + " results found.");
        return result;
    }

    public List<WorkingHour> findByProjectIdAndDateRange(UUID projectId, Date startDate, Date endDate) {
        TypedQuery<WorkingHour> query = entityManager.createQuery(
                "SELECT wh FROM WorkingHour wh WHERE wh.project.id = :projectId AND wh.startTime BETWEEN :startDate AND :endDate",
                WorkingHour.class
        );
        query.setParameter("projectId", projectId);

        List<WorkingHour> result = query.getResultList();
        System.out.println("findByProjectIdAndDateRange: " + result.size() + " results found.");
        return result;
    }



    public List<WorkingHour> findByUserIdAndProjectIdAndDateRange(UUID userId, UUID projectId, Date startDate, Date endDate) {
        TypedQuery<WorkingHour> query = entityManager.createQuery(
                "SELECT wh FROM WorkingHour wh WHERE wh.user.id = :userId AND wh.project.id = :projectId AND wh.startTime BETWEEN :startDate AND :endDate",
                WorkingHour.class
        );
        query.setParameter("userId", userId);
        query.setParameter("projectId", projectId);

        List<WorkingHour> result = query.getResultList();
        System.out.println("findByUserIdAndProjectIdAndDateRange: " + result.size() + " results found.");
        return result;
    }

    public void exportToExcel(String userId, String projectId, Date startDate, Date endDate, OutputStream out) throws IOException {
        List<WorkingHour> workingHours;

        if (userId != null && projectId != null) {
            workingHours = findByUserIdAndProjectIdAndDateRange(UUID.fromString(userId), UUID.fromString(projectId), startDate, endDate);
        } else if (userId != null) {
            workingHours = findByUserIdAndDateRange(UUID.fromString(userId), startDate, endDate);
        } else if (projectId != null) {
            workingHours = findByProjectIdAndDateRange( UUID.fromString(projectId), startDate, endDate);
        } else {
            throw new IllegalArgumentException("Either userId or projectId must be provided");
        }

        System.out.println("Exporting " + workingHours.size() + " working hours to Excel.");

        try (Workbook workbook = new HSSFWorkbook()) { // Create an Excel workbook
            Sheet sheet = workbook.createSheet("Working Hours"); // Create a sheet named "Working Hours"

            // Create the header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Task Name");
            headerRow.createCell(1).setCellValue("Project Name");
            headerRow.createCell(2).setCellValue("Start Time");
            headerRow.createCell(3).setCellValue("End Time");

            // Populate the data rows
            int rowNum = 1;
            for (WorkingHour workingHour : workingHours) {
                Row row = sheet.createRow(rowNum++);
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
            workbook.write(out);
        }
    }
}