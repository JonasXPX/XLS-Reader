package com.github.jonasxpx.facade;

import com.github.jonasxpx.anotations.ColumnIdentify;
import com.github.jonasxpx.anotations.SheetObject;
import com.github.jonasxpx.exception.ReaderException;
import com.github.jonasxpx.reader.CellLocation;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static java.lang.String.format;
import static org.apache.log4j.config.PropertyPrinter.capitalize;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

public class SheetBuilder {

    private static SheetBuilder sheetBuilder;

    private SheetBuilder() {
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
            throw new RuntimeException("Precisa ser criado um builder do Work para instanciar um SheetBuilder");
        }

    }

    @SneakyThrows
    public <T> List<T> read(Class<T> clazz) {
        WorkBuilder workBuilder = WorkBuilder.getInstance();
        Set<Sheet> sheets = workBuilder.getSheet();
        Sheet sheet = sheets.iterator().next();

        T entity = clazz.getConstructor().newInstance();
        Class<?> entityClass = entity.getClass();

        SheetObject annotation = entityClass.getAnnotation(SheetObject.class);
        Set<CellLocation> cellLocations = new HashSet<>();

        for (Field declaredField : entityClass.getDeclaredFields()) {
            ColumnIdentify columnIdentify = declaredField.getAnnotation(ColumnIdentify.class);

            if (Objects.isNull(columnIdentify)) continue;

            CellLocation cellLocation = searchColumnIdentify(sheet, annotation, columnIdentify, declaredField);
            cellLocations.add(cellLocation);
        }

        List<T> entities = new ArrayList<>();

        for (int row = annotation.startAtRow(); row <= annotation.endAtRow(); row++) {
            T newEntity = clazz.getConstructor().newInstance();

            for (CellLocation cellLocation : cellLocations) {
                readAllFieldsFromRow(sheet, row, newEntity, cellLocation);
            }

            entities.add(newEntity);

        }

        return entities;
    }

    private <T> void readAllFieldsFromRow(Sheet sheet, int row, T newEntity, CellLocation cellLocation)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (row < cellLocation.getRow()) {
            return;
        }

        Optional<Row> nullableRow = Optional.ofNullable(sheet.getRow(cellLocation.getRow() + row));
        if (nullableRow.isEmpty()) {
            return;
        }

        Cell cell = nullableRow.get().getCell(cellLocation.getColumn());
        Object valueFromCell = null;

        if(cell.getCellType().equals(NUMERIC)) {
            valueFromCell = cell.getNumericCellValue();
        }

        if(cell.getCellType().equals(STRING)) {
            valueFromCell = cell.getStringCellValue();
        }

        defineMethodValue(newEntity, newEntity.getClass(), cellLocation.getField(), valueFromCell);
    }

    private <T> void defineMethodValue(T entity, Class<?> entityClzz, Field field, Object value)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String methodName = format("set%s", capitalize(field.getName()));

        Method method = entityClzz.getDeclaredMethod(methodName, field.getType());

        method.invoke(entity, value);
    }

    private CellLocation searchColumnIdentify(Sheet sheet, SheetObject sheetObject, ColumnIdentify columnIdentify, Field field) {
        int currentRow = sheetObject.startAtRow();
        int currentColumn = sheetObject.startAtColumn();

        while (currentRow != Math.min(sheet.getLastRowNum(), sheetObject.endAtRow())) {
            while (currentColumn != sheetObject.endAtColumn()) {
                Optional<Cell> cell = Optional.ofNullable(sheet.getRow(currentRow).getCell(currentColumn));

                if (cell.isEmpty()) {
                    continue;
                }

                boolean founded = cell.get().getStringCellValue().equalsIgnoreCase(columnIdentify.cellName());
                if (founded) {
                    return new CellLocation(currentRow, currentColumn, columnIdentify, field);
                }

                currentColumn++;
            }
            currentColumn = sheetObject.startAtColumn();
            currentRow++;
        }

        throw new ReaderException(format("Não foi possível encontrar o identificador %s", columnIdentify.cellName()));
    }

}
