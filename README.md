# xls-reader-fatura
programa para ler o xls

## Começando

O sistema foi desenvolvimento com o intuito de transformar arquivos xls em Objetos Java, para implementações.

Basicamente, um arquivo XLS, são colunas e linhas, com uma identificação de cada coluna. Ex:

```
| Mês           | IPCA |
| 01/2020	| 0,21 |
| 02/2020	| 0,25 |
| 03/2020	| 0,07 |
| 04/2020	| -0,31|
| 05/2020	| -0,38|
| 06/2020	| 0,26 |
| 07/2020	| 0,36 |
| 08/2020	| 0,24 |
| 09/2020	| 0,64 |
| 10/2020	| 0,86 |
| 11/2020	| 0,89 |
| 12/2020	| 1,35 |

```

Tradezindo essa planilha, temos o seguinte Classe a ser definida:

```java
@SheetObject(
        startAtColumn = 0,
        startAtRow = 0,
        endAtColumn = 2, // limite de 2 colunas
        endAtRow = 5000 // limite de 5000 linhas
)
class Inflacao {

 @ColumnIdentify(cellName = "mês")
 private String mes;
 
 @ColumnIdentify(cellName = "ipca")
 private String ipca;
 
 //Getters and Setters
}
```
