export class PagedList {
  list: any[];
  page: Page;
  listMap: any;

  constructor(public pagedList: any) {
    this.list = pagedList.list;
    this.listMap = pagedList.listMap;
    this.page = pagedList.page;
  }
}

export interface Page {
  size: number;
  skip: number;
  number?: number;
  totalItems?: number;
}
