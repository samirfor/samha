/**
 * Esta interface é responsável por mapear as colunas que uma classe no servidor irá possuir no componente TableComponent
 * @param columnDef representa a definição do nome da classe no JSON retornado pelo servidor
 * @param header representa o Header da coluna
 * @param visible autoindicativo
 *
 * @author Breno Leal -- 19/04/2022
 */
export interface TableColumnModel {
  columnDef: string;
  header: string;
  visible: boolean;
  type?: string;
}
