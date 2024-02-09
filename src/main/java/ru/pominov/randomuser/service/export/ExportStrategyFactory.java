package ru.pominov.randomuser.service.export;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExportStrategyFactory {

    public ExportStrategy createExportStrategy(String exportMethod) {
        return switch (exportMethod) {
            case "Excel" -> new ExcelExporter();
            case "CSV" -> new CsvExporter();
            default -> {
                log.error("Unsupported export method: {}", exportMethod);
                throw new IllegalArgumentException();
            }
        };
    }
}
