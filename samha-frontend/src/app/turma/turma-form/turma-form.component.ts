import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {DataService} from '../../shared/service/data.service';
import {Filter, QueryMirror} from '../../shared/query-mirror';
import {matrizColumns} from '../../meta-model/matriz-curricular';
import {Observable, of} from 'rxjs';
import {PagedList} from '../../shared/paged-list';
import {first} from 'rxjs/operators';
import {NotificationService} from "../../shared/service/notification.service";

@Component({
  selector: 'samha-turma-form',
  templateUrl: './turma-form.component.html'
})
export class TurmaFormComponent implements OnInit {
  form: FormGroup;
  cursoControl = new FormControl();
  matriz$: Observable<PagedList>;
  turma: any;

  constructor(private formBuilder: FormBuilder,
              private notificationService: NotificationService,
              private router: Router,
              private route: ActivatedRoute,
              private dataService: DataService) {}

  ngOnInit(): void {
    this.route.data.pipe(first()).subscribe(
      next => {
        this.turma = next.turma;
        this.loadForm();
      }
    )
  }


  compareFunction(o1: string, o2: string) {
    return (o1 != null && o2 != null && o1.toUpperCase() == o2.toUpperCase());
  }
  compareMatrizFunction(o1: any, o2: any){
    return (o1 != null && o2 != null) && o1.id == o2.id;
  }

  private setMatriz(id: number) {
    let query = new QueryMirror('matrizCurricular');
    let projections = matrizColumns.map(column => column.columnDef);
    let filter: Filter;
    filter = {
      and: {
        'curso.id': {equals: id}
      }
    }
    query.selectList(projections);
    query.where(filter)

    this.matriz$ = this.dataService.query(query);
  }

  onCursoChange() {
    this.setMatriz(this.cursoControl.value.id);
  }

  private loadForm() {
    this.form = this.formBuilder.group({
      nome: [this.turma?.nome, [Validators.required, Validators.maxLength(255)]],
      ano: [this.turma?.ano, [Validators.required, Validators.maxLength(4)]],
      semestre: [this.turma?.semestre, [Validators.required, Validators.max(2)]],
      turno: [this.turma?.turno, Validators.required],
      matriz: [this.turma?.matriz, Validators.required],
      ativa: [this.turma?.ativa !== undefined ? this.turma?.ativa : true]
    });
    this.cursoControl.setValue(this.turma?.matriz.curso);
    this.matriz$ = of(new PagedList({listMap: [this.turma?.matriz]}));
    this.cursoControl.addValidators([Validators.required]);
    this.form.addControl('curso', this.cursoControl);
  }

  salvar() {
    if(this.form.invalid){
      this.form.markAllAsTouched();
      return;
    }

    this.buildEntityData();

    if(this.turma?.id){
      this.dataService.update('turma', this.turma.id, this.turma).pipe(first()).subscribe(
        next => {
          this.notificationService.success('Turma salva com sucesso!');
          this.router.navigate(['../', next.id], {relativeTo: this.route});
        }, error => this.notificationService.handleError(error)
      )
    }else{
      this.dataService.save('turma', this.turma).pipe(first()).subscribe(
        next => {
          this.notificationService.success('Turma salva com sucesso!');
          this.router.navigate(['../' + next.id], {relativeTo: this.route});
        }, error => this.notificationService.handleError(error)
      )
    }

  }

  private buildEntityData() {
    this.turma = {
      id: this.turma?.id,
      nome: this.form.get('nome').value,
      ano: this.form.get('ano').value,
      turno: this.form.get('turno').value,
      matriz: this.form.get('matriz').value,
      ativa: this.form.get('ativa').value,
      semestre: this.form.get('semestre').value
    }
  }

  public goBack() {
    this.router.navigate(['../'], {relativeTo: this.route});
  }

  delete() {
    this.dataService.delete('turma', this.turma.id).pipe(first()).subscribe(_ => {
      this.notificationService.success('Registro excluído com sucesso!')
      this.router.navigate(['../'], {relativeTo: this.route})
    }, error => {
      this.notificationService.error(error);
    })
  }
}
