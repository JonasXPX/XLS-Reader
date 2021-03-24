package com.github.jonasxpx.reader;

import com.github.jonasxpx.anotations.ColumnIdentify;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CellLocation {
    private int row;
    private int column;
    private ColumnIdentify columnIdentify;
    private Class<?> type;
}
