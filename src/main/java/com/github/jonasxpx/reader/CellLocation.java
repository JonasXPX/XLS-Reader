package com.github.jonasxpx.reader;

import com.github.jonasxpx.anotations.ColumnIdentify;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Field;

@Getter
@Setter
@AllArgsConstructor
@ToString(of = {"row", "column", "columnIdentify"})
public class CellLocation {
    private int row;
    private int column;
    private ColumnIdentify columnIdentify;
    private Field field;
}
