import {Page} from './paged-list';

export class QueryMirror {
  public projections: (string | {[key: string]: string})[] = [];
  public predicates: Predicate;
  public orders: (string | number)[] = [];
  public page: Page;
  public groups: (string | { [key: string]: string })[] = [];

  constructor(public entityPath: string) {
  }

  public select(projection: string | { [key: string]: string }, condition = true): QueryMirror {
    if (projection && this.projections.indexOf(projection) < 0 && condition)
      this.projections.push(projection);
    return this;
  }

  public group(group: string | { [key: string]: string }): QueryMirror {
    this.groups.push(group);
    return this;
  }

  public groupList(groups: Array<string | { [key: string]: string }>): QueryMirror {
    this.groups = this.groups.concat(groups);
    return this;
  }

  public selectList(projections: (string | { [key: string]: string })[]): QueryMirror {
    if (projections) this.projections = this.projections.concat(projections);
    return this;
  }

  public where(predicate: Filter): QueryMirror {
    if (predicate) {
      this.predicates = Object.assign(this.predicates || {}, predicate);
    }
    return this;
  }

  public whereList(...predicates: Filter[]): QueryMirror {
    const p = predicates.filter(predicate => predicate);
    this.predicates = p.length <= 1 ? p[0] : {and: p};
    return this;
  }

  public orderBy(order: string): QueryMirror {
    if (this.orders.indexOf(order) < 0 )
      this.orders.push(order);

    return this;
  }

  public orderByList(orders: string[]): QueryMirror {
    if (orders) {
      this.orders = this.orders.concat(orders);
    }
    return this;
  }

  public pageItem(page: Page): QueryMirror {
    this.page = page;
    return this;
  }
}



export interface Filter{
  equals?: any;
  notEquals?: any;
  contains?: any;
  notContains?: any;
  in?: any[];
  notIn?: any[];
  greaterThan?: any;
  lessThan?: any;
  between?: any;
  and?: Predicate;
  or?: Predicate;
}

export interface SimplePredicate {
  [key: string]: Filter;
}

export interface LogicalPredicate {
  and?: Predicate | Predicate[];
  or?: Predicate | Predicate[];
}

export type Predicate = SimplePredicate | LogicalPredicate;
