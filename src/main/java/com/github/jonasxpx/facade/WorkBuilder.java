package com.github.jonasxpx.facade;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Log4j
public class WorkBuilder {

    private static WorkBuilder workBuilder;

    @Getter
    @Setter
    private Set<Sheet> sheet;

    private WorkBuilder() {}

    public static WorkBuilder getInstance() {
        if (Objects.isNull(workBuilder)) {
            throw new RuntimeException("Você deve fazer o build da fatura antes");
        }

        return workBuilder;
    }

    public static WorkBuilder create(String fileLocation) {
        try (Workbook workbook = WorkbookFactory.create(new File(fileLocation))) {
            workBuilder = new WorkBuilder();

            readSheets(workbook);


        } catch (IOException exc) {
            log.error("Não foi possível ler o arquivo", exc);
        } catch (EncryptedDocumentException exc) {
            log.error("Permissão para acessar o arquivo inválida", exc);
        }

        return workBuilder;
    }

    private static void readSheets(Workbook workbook) {
        Set<Sheet> sheets = new HashSet<>();
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            sheets.add(workbook.getSheetAt(sheetIndex));
        }
        workBuilder.setSheet(Collections.unmodifiableSet(sheets));
    }

}
