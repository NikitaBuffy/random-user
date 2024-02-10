package ru.pominov.randomuser.service.export;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Класс, реализующий фабричный шаблон проектирования,
 * позволяющий выбрать создаваемый объект интерфейса ExportStrategy
 * в зависимости от необходимого метода экспорта.
 */
@Component
@Slf4j
public class ExportStrategyFactory {

    /**
     * @param exportMethod Строковое значение метода для экспорта (Excel, CSV и др.)
     * @return Нужную имплементацию интерфейса ExportStrategy в зависимости от выбранного метода экспорта,
     * либо пробрасывает исключение IllegalArgumentException, когда метод еще не поддерживается
     */
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
