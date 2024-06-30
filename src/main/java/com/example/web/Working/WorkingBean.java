package com.example.web.Working;

import com.example.domain.*;
import com.example.ejb.EjbWorkingHourRepository;
import com.example.ejb.EjbUserRepository;
import com.example.ejb.EjbProjectRepository;
import com.example.ejb.EjbTaskRepository;
import com.example.web.Task.TaskForm;
import com.example.web.User.LoginBean;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
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
import java.time.LocalTime;
import java.util.*;

@ViewScoped
@Named("workingHoursBean")
public class WorkingBean implements Serializable {

    @Inject
    private FacesContext facesContext;

    @Inject
    private EjbWorkingHourRepository workingHourRepository;

    @Inject
    private EjbUserRepository userRepository;

    @Inject
    private EjbProjectRepository projectRepository;

    @Inject
    private EjbTaskRepository taskRepository;

    @Inject
    private LoginBean loginBean; // Inject LoginBean

    private WorkingForm form;
    private List<WorkingHour> workingHours;
    private List<Task> filteredTasks;
    private int currentPage = 1;
    private int itemsPerPage = 10;
    private List<WorkingHour> employeeReport; // Filtered list for employee report
    private List<WorkingHour> projectReport;
    private Date startDate;
    private Date endDate;

    @PostConstruct
    public void init() {
        this.form = new WorkingForm();
        loadWorkingHours();
        filteredTasks = new ArrayList<>();
    }

    public void loadWorkingHours() {

        if(loginBean.getCurrentUser().getRole() == UserRole.EMPLOYEE){
            this.workingHours = taskRepository.findWorkingHoursByCustomer(loginBean.getCurrentUser().getId());
        }
        this.workingHours = workingHourRepository.findAll();
    }

    public void saveWorkingHour() {
        UUID userId = UUID.fromString(form.getUser());
        UUID projectId = UUID.fromString(form.getProject());
        UUID taskId = UUID.fromString(form.getTask());

        User user = userRepository.findById(userId);
        Project project = projectRepository.findById(projectId);
        Task task = taskRepository.findById(taskId);

        if (user == null || project == null || task == null) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid data.", null));
            return;
        }

        WorkingHour workingHour;

        if (this.form.getId() == null) {
            // Create new WorkingHour
            workingHour = new WorkingHour();
        } else {
            // Update existing WorkingHour
            UUID workingHourId = (this.form.getId());
            workingHour = workingHourRepository.findById(workingHourId);

            if (workingHour == null) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Working hour not found.", null));
                return;
            }
        }

        workingHour.setUser(user);
        workingHour.setProject(project);
        workingHour.setTask(task);
        workingHour.setDayOfWeek(DayOfWeek.valueOf(form.getDayOfWeek()));
        workingHour.setStartTime(form.getStartTime());
        workingHour.setEndTime(form.getEndTime());

        workingHourRepository.save(workingHour);

        facesContext.addMessage(null, new FacesMessage("Working hour saved successfully."));
        loadWorkingHours();
        resetForm();
    }
    public void editWork(UUID id) {
        WorkingHour task = workingHourRepository.findById(id);
        this.form = new WorkingForm(task.getUser().getId().toString(), task.getProject().getId().toString(),task.getTask().getId().toString(),task.getDayOfWeek().toString(),task.getStartTime(),task.getEndTime());

    }



    public void deleteWork(UUID id) {
        workingHourRepository.deleteById(id);
        loadWorkingHours();
        resetForm();
        facesContext.addMessage(null, new FacesMessage("Task was deleted successfully."));
    }



    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return Arrays.asList(DayOfWeek.values());
    }
    public void onProjectChange(AjaxBehaviorEvent event ) {
        if (form.getProject() != null && !form.getProject().isEmpty()) {
            UUID projectId = UUID.fromString(form.getProject());
            filteredTasks = taskRepository.findByProject(projectId);
        } else {
            filteredTasks = new ArrayList<>();
        }
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

            // Retrieve data from repository
            List<WorkingHour> workingHours = workingHourRepository.findAll();
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

    public List<WorkingHour> getPaginatedTask() {
        int fromIndex = (currentPage - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, workingHours.size());
        return workingHours.subList(fromIndex, toIndex);
    }

    public List<Integer> getPageNumbers() {
        List<Integer> pageNumbers = new ArrayList<>();
        int totalPages = (int) Math.ceil((double) workingHours.size() / itemsPerPage);
        for (int i = 1; i <= totalPages; i++) {
            pageNumbers.add(i);
        }
        return pageNumbers;
    }

    public void changePage(int pageNumber) {
        currentPage = pageNumber;
    }


    private void resetForm() {
        this.form = new WorkingForm();
    }

    public WorkingForm getForm() {
        return form;
    }

    public void setForm(WorkingForm form) {
        this.form = form;
    }

    public List<WorkingHour> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(List<WorkingHour> workingHours) {
        this.workingHours = workingHours;
    }

    public List<Task> getFilteredTasks() {
        return filteredTasks;
    }

    public void setFilteredTasks(List<Task> filteredTasks) {
        this.filteredTasks = filteredTasks;
    }
}