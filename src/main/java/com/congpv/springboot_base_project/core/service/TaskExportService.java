package com.congpv.springboot_base_project.core.service;

import com.congpv.springboot_base_project.shared.dto.common.PageResponse;
import com.congpv.springboot_base_project.shared.dto.task.TaskResponseDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskExportService {

    private final TaskService taskService;

    public File generateTasksCsv(Long projectId) {
        String fileName = "mini_jira_tasks_" + projectId + "_" + System.currentTimeMillis() + ".csv";
        File csvFile = new File(System.getProperty("java.io.tmpdir"), fileName);

        String[] headers = {"Task ID", "Title", "Status", "Assignee", "Created Date"};

        log.info("Starting CSV generation at: {}", csvFile.getAbsolutePath());

        // Use FileOutputStream and OutputStreamWriter to support UTF-8 with BOM for Excel compatibility
        try (FileOutputStream fos = new FileOutputStream(csvFile);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(osw, CSVFormat.DEFAULT.withHeader(headers))) {

            // Write UTF-8 BOM to help Excel recognize the encoding correctly
            fos.write(0xef);
            fos.write(0xbb);
            fos.write(0xbf);

            int pageNumber = 0;
            int pageSize = 50;
            PageResponse<TaskResponseDto> page;

            do {
                page = taskService.getTasksByProject(projectId, pageNumber, pageSize);

                for (TaskResponseDto task : page.getContent()) {
                    printer.printRecord(
                            task.id(),
                            task.title(),
                            task.status(),
                            task.assigneeUsername() != null ? task.assigneeUsername() : "Not assigned",
                            task.createdAt()
                    );
                }

                log.info("Wrote page {}/{}", pageNumber + 1, page.getTotalPages());
                pageNumber++;

            } while (page != null && !page.isLast() && pageNumber < page.getTotalPages());

            printer.flush();
            log.info("CSV file created successfully: {}", csvFile.getAbsolutePath());
            return csvFile;

        } catch (IOException e) {
            log.error("I/O error while creating CSV for Project: {}", projectId, e);
            throw new RuntimeException("Failed to create CSV file", e);
        }
    }
}