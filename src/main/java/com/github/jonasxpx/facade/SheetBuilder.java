package com.github.jonasxpx.facade;

import com.github.jonasxpx.anotations.ColumnIdentify;
import com.github.jonasxpx.anotations.SheetObject;
import com.github.jonasxpx.anotations.Transformer;
import com.github.jonasxpx.anotations.TransformerRunner;
import com.github.jonasxpx.exception.ReaderException;
import com.github.jonasxpx.reader.CellLocation;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static org.apache.log4j.config.PropertyPrinter.capitalize;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

public class SheetBuilder {

    private static SheetBuilder sheetBuilder;
    private final Map<CellLocation, Transformer> transformers;
    private final Set<CellLocation> cellLocations;

    private SheetBuilder() {
        transformers = new HashMap<>();
        cellLocations = new HashSet<>();
    }

    public static SheetBuilder getInstance() {
        return Objects.isNull(sheetBuilder)
                ? createInstance() : sheetBuilder;
    }

    private static SheetBuilder createInstance() {
        try {
            WorkBuilder.getInstance();
            return sheetBuilder = new SheetBuilder();
        } catch (RuntimeException exc) {
            throw new ReaderException("Precisa ser criado um builder do Work para instanciar um SheetBuilder");
        }

    }

    @SneakyThrows
    public <T> List<T> read(Class<T> clazz) {
        WorkBuilder workBuilder = WorkBuilder.getInstance();
        Set<Sheet> sheets = workBuilder.getSheet();
        Sheet sheet = sheets.iterator().next(); // need future implementation

        T entity = clazz.getConstructor().newInstance();
        Class<?> entityClass = entity.getClass();

        SheetObject sheetAnnotation = entityClass.getAnnotation(SheetObject.class);

        for (Field declaredField : entityClass.getDeclaredFields()) {
            ColumnIdentify columnIdentify = declaredField.getAnnotation(ColumnIdentify.class);
            Transformer transformer = declaredField.getAnnotation(Transformer.class);

            if (Objects.isNull(columnIdentify)) continue;

            CellLocation cellLocation = searchColumnIdentify(sheet, sheetAnnotation, columnIdentify, declaredField);
            cellLocations.add(cellLocation);

            if (Objects.nonNull(transformer)) {
                transformers.put(cellLocation, transformer);
            }
        }

        List<T> entities = new ArrayList<>();

        for (int row = sheetAnnotation.startAtRow(); row <= Math.min(sheetAnnotation.endAtRow(), sheet.getLastRowNum()); row++) {
            T newEntity = clazz.getConstructor().newInstance();

            for (CellLocation cellLocation : cellLocations) {
                readAllFieldsFromRow(sheet, row, newEntity, cellLocation, true);
            }

            entities.add(newEntity);

        }

        clearEntries();
        return entities;
    }

    private <T> void readAllFieldsFromRow(Sheet sheet, int row, T newEntity, CellLocation cellLocation, boolean isFixedRun) {
        if(cellLocation.isFixed() && isFixedRun) {
            readAllFieldsFromRow(sheet, 0, newEntity, cellLocation, false);
            return;
        }
        Optional<Row> nullableRow = Optional.ofNullable(sheet.getRow(cellLocation.getRow() + row));
        if (nullableRow.isEmpty()) {
            return;
        }

        Cell cell = nullableRow.get().getCell(cellLocation.getColumn());
        Object valueFromCell = null;

        if (cell.getCellType().equals(NUMERIC)) {
            valueFromCell = cell.getNumericCellValue();
        }

        if (cell.getCellType().equals(STRING)) {
            valueFromCell = cell.getStringCellValue();
        }

        Transformer transformer = transformers.get(cellLocation);
        defineMethodValue(
                transformer,
                newEntity,
                newEntity.getClass(),
                cellLocation.getField(),
                valueFromCell);
    }

    @SneakyThrows
    private <T> void defineMethodValue(Transformer transformer, T entity, Class<?> entityClzz, Field field, Object value) {
        if (Objects.nonNull(transformer)) {
            TransformerRunner transformerRunner = transformer.value()
                    .getConstructor()
                    .newInstance();
            value = transformerRunner.run(value);
        }

        String methodName = format("set%s", capitalize(field.getName()));

        Method method = entityClzz.getDeclaredMethod(methodName, field.getType());
        method.invoke(entity, value);
    }

    private CellLocation searchColumnIdentify(Sheet sheet, SheetObject sheetObject, ColumnIdentify columnIdentify, Field field) {
        if (!columnIdentify.cellLocation().isBlank()) {
           return readCellValue(columnIdentify, sheet, field);
        }

        int currentRow = sheetObject.startAtRow();
        int currentColumn = sheetObject.startAtColumn();

        while (currentRow != Math.min(sheet.getLastRowNum(), sheetObject.endAtRow())) {
            CellLocation innerRow = cellInnerFinder(
                    sheet,
                    sheetObject,
                    columnIdentify,
                    field,
                    currentRow,
                    currentColumn
            );
            if (Objects.nonNull(innerRow)) return innerRow;
            currentColumn = sheetObject.startAtColumn();
            currentRow++;
        }

        throw new ReaderException(format("Não foi possível encontrar o identificador %s", columnIdentify.cellName()));
    }

    private CellLocation readCellValue(ColumnIdentify columnIdentify, Sheet sheet, Field field) {
        return cellInnerFinder(sheet, columnIdentify, field);
    }

    private CellLocation cellInnerFinder(Sheet sheet, SheetObject sheetObject,
                                         ColumnIdentify columnIdentify, Field field, int currentRow, int currentColumn) {
        while (currentColumn != sheetObject.endAtColumn()) {
            Optional<Cell> cell = Optional.ofNullable(sheet.getRow(currentRow).getCell(currentColumn));

            if (cell.isEmpty()) {
                continue;
            }

            Cell innerCell = cell.get();
            boolean founded = false;
            if (innerCell.getCellType().equals(STRING)) {
                founded = innerCell.getStringCellValue().equalsIgnoreCase(columnIdentify.cellName());
            }

            if (founded) {
                return new CellLocation(currentRow, currentColumn, columnIdentify, field, false);
            }

            currentColumn++;
        }
        return null;
    }

    private CellLocation cellInnerFinder(Sheet sheet, ColumnIdentify columnIdentify, Field field) {
        String[] rowColumn = columnIdentify.cellLocation()
                .split(";");

        Cell value = sheet.getRow(Integer.parseInt(rowColumn[0]))
                .getCell(Integer.parseInt(rowColumn[1]));

        return new CellLocation(value.getRowIndex(), value.getColumnIndex(), columnIdentify, field, true);
    }

    private void clearEntries() {
        transformers.clear();
        cellLocations.clear();
    }
}
