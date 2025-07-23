import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl} from '@angular/forms';
import {Observable} from 'rxjs';
import {first, map, startWith, tap} from 'rxjs/operators';
import {DataService} from '../../service/data.service';

@Component({
  selector: 'samha-autocomplete-field',
  templateUrl: './autocomplete-field.component.html'
})
export class AutocompleteFieldComponent implements OnInit {
  @Input() resource: string;
  @Input() control: FormControl;
  @Input() label: string;
  @Output() onChange = new EventEmitter<any>();
  @Output() loaded = new EventEmitter<any>();
  @Output() onOpened = new EventEmitter<void>();
  @Output() public clearButtonClicked = new EventEmitter<any>();
  filteredOptions: Observable<any[]>;
  list: any[];


  constructor(private dataService: DataService) {}

  ngOnInit() {
    this.dataService.getAll(this.resource).pipe(
      first(),
      tap(
        (next: any[]) => {
          next.sort((a, b) => {
            if (a?.nome > b?.nome) return 1;
            else if(a?.nome < b?.nome) return -1;
            else return 0;
          });
        }
      )
    ).subscribe(
      next =>{
        this.list = next;
        this.loaded.emit(next);
        this.filteredOptions = this.control.valueChanges.pipe(
          startWith(''),
          map(value => (typeof value === 'string' ? value : value?.name)),
          map(name => (name ? this._filter(name) : this.list.slice())),
        );
      }
    )

  }

  displayFn(entity: any): string {
    return entity && entity.nome ? entity.nome : '';
  }

  private _filter(nome: string): any[] {
    const filterValue = nome.toLowerCase();

    return this.list.filter(entity => entity.nome.toLowerCase().includes(filterValue));
  }

  onAutocompleteChange($event: any) {
    this.onChange.emit($event);
  }

  onSelectionOpened() {
    this.onOpened.emit()
  }

  onClearButtonClicked() {
    this.control.setValue('');
    this.clearButtonClicked.emit();
  }
}
