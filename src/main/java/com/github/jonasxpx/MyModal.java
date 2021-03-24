package com.github.jonasxpx;

import com.github.jonasxpx.anotations.ColumnIdentify;
import com.github.jonasxpx.anotations.SheetObject;
import lombok.*;

@SheetObject(
        startAtColumn = 0,
        startAtRow = 6,
        endAtColumn = 10,
        endAtRow = 50
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MyModal {

    @ColumnIdentify("placa")
    private String placa;

    @ColumnIdentify("concessionária")
    private String concesionaria;

    private String pracaId;

}
