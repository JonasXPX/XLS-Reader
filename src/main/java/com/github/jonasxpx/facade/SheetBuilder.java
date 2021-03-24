package com.github.jonasxpx.facade;

import com.github.jonasxpx.anotations.ColumnIdentify;
import com.github.jonasxpx.anotations.SheetObject;
import com.github.jonasxpx.reader.CellLocation;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static org.apache.log4j.config.PropertyPrinter.capitalize;

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
    public <T> T read(Class<T> clazz) {
        WorkBuilder workBuilder = WorkBuilder.getInstance();
        Set<Sheet> sheets = workBuilder.getSheet();
        Sheet sheet = sheets.iterator().next();
        T entity = clazz.getConstructor().newInstance();
        Class<?> entityClass = entity.getClass();
        SheetObject annotation = entityClass.getAnnotation(SheetObject.class);

        for (Field declaredField : entityClass.getDeclaredFields()) {
            ColumnIdentify columnIdentify = declaredField.getAnnotation(ColumnIdentify.class);
            if (Objects.nonNull(columnIdentify)) {
                CellLocation cellLocation = searchColumnIdentify(sheet, annotation, columnIdentify);

                String methodName = format("get%s", capitalize(declaredField.getName()));

                Method setPlaca = entityClass.getDeclaredMethod(methodName, cellLocation.getType());
                setPlaca.setAccessible(true);

                readCellLocation(sheet, cellLocation);

            }
        }


        return entity;
    }

    private void readCellLocation(Sheet sheet, CellLocation cellLocation) {

    }

    private CellLocation searchColumnIdentify(Sheet sheet, SheetObject sheetObject, ColumnIdentify columnIdentify) {
        int currentRow = sheetObject.startAtRow();
        int currentColumn = sheetObject.startAtColumn();

        while (currentRow != Math.min(sheet.getLastRowNum(), sheetObject.endAtRow())) {
            while (currentColumn != sheetObject.endAtColumn()) {
                Optional<Cell> cell = Optional.ofNullable(sheet.getRow(currentRow).getCell(currentColumn));
                if (cell.isPresent()) {
                    boolean founded = cell.get().getStringCellValue().equalsIgnoreCase(columnIdentify.value());
                    if (founded) {
                        return new CellLocation(currentRow, currentColumn, columnIdentify, String.class);
                    }
                }
                currentColumn++;
            }
            currentColumn = sheetObject.startAtColumn();
            currentRow++;
        }

        throw new RuntimeException(format("Não foi possível encontrar o identificador %s", columnIdentify.value()));
    }

}
