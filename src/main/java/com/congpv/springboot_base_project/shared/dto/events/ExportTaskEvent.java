package com.congpv.springboot_base_project.shared.dto.events;

import java.io.File;

public record ExportTaskEvent(File csvFile, Long projectId) {
}
