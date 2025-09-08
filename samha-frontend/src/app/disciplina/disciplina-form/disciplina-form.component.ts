import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {DataService} from '../../shared/service/data.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Observable, Subscription} from 'rxjs';
import {first} from 'rxjs/operators';
import {Filter, QueryMirror} from '../../shared/query-mirror';
import {matrizColumns} from '../../meta-model/matriz-curricular';
import {NotificationService} from "../../shared/service/notification.service";

@Component({
  selector: 'samha-disciplina-form',
  templateUrl: './disciplina-form.component.html'
})
export class DisciplinaFormComponent implements OnInit, OnDestroy {
  form: FormGroup;
  cursoControl = new FormControl(null, [Validators.required]);

  matriz$: Observable<any>;
  disciplina: any = {};
  private subscription: Subscription;
  private subscription2: Subscription;

  constructor(private formBuilder: FormBuilder,
              private notification: NotificationService,
              private dataService: DataService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {
    this.route.data.pipe(first()).subscribe(
      next => {
        this.disciplina = next.disciplina;
        this.setCursoData();
        this.loadForm();
      }
    )

    this.subscription = this.cursoControl.valueChanges.subscribe(
      value => {
        this.setMatriz(value.id);
      }
    )

    this.subscription2 = this.form.get('matriz').valueChanges.subscribe(
      data => {
        this.form.get('periodo').setValidators([
          Validators.required,
          Validators.min(1),
          Validators.max(data.curso.qtPeriodos)
        ])
      }
    )
  }

  private loadForm() {
    this.form = this.formBuilder.group({
      matriz: [this.disciplina?.matriz, Validators.required],
      nome: [this.disciplina?.nome, [Validators.required, Validators.maxLength(255)]],
      sigla: [this.disciplina?.sigla, Validators.required],
      cargaHoraria: [this.disciplina?.cargaHoraria ? this.disciplina?.cargaHoraria : 60, [Validators.required]],
      qtAulas: [this.disciplina?.qtAulas, [Validators.required]],
      periodo: [this.disciplina?.periodo],
      tipo: [this.disciplina?.tipo, Validators.required],
      qtVagas: [this.disciplina?.qtVagas, [Validators.required]]
    });

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

  compareFunction(o1: string, o2: string) {
    return (o1 != null && o2 != null && o1.toUpperCase() == o2.toUpperCase());
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
    this.subscription2.unsubscribe();
  }

  salvar() {
    if(this.form.invalid){
      this.form.markAllAsTouched();
      return;
    }

    this.buildEntityData();

    if(this.disciplina?.id){
      this.dataService.update('disciplina', this.disciplina.id, this.disciplina).pipe(first()).subscribe(
        next => {
          this.notification.success('Disciplina salva com sucesso!');
          this.router.navigate(['../', next.id], {relativeTo: this.route});
        }, error => {
          this.notification.handleError(error);
        }
      )
    }else{
      this.dataService.save('disciplina', this.disciplina).pipe(first()).subscribe(
        next => {
          this.notification.success('Disciplina salva com sucesso!');
          this.router.navigate(['../', next.id], {relativeTo: this.route});
        },
        error => {
          this.notification.handleError(error);
        }
      )
    }

  }

  goBack = () => this.router.navigate(['../'], {relativeTo: this.route});

  private buildEntityData() {
    this.disciplina = { 
      id: this.disciplina?.id,
      nome: this.form.get('nome').value,
      qtAulas: this.form.get('qtAulas').value,
      periodo: this.form.get('periodo').value,
      sigla: this.form.get('sigla').value,
      tipo: this.form.get('tipo').value,
      cargaHoraria: this.form.get('cargaHoraria').value,
      matriz: this.form.get('matriz').value,
      qtVagas : this.form.get('qtVagas').value
    }
  }

  private setCursoData() {
    if(this.disciplina?.id){
      let query = new QueryMirror('matrizCurricular');
      let projections = matrizColumns.map(matriz => matriz.columnDef);
      let filter: Filter;

      filter = {
        and: {
          'id': {equals: this.disciplina.matriz.id}
        }
      }

      query.selectList(projections);
      query.where(filter)

      this.dataService.query(query).pipe(first()).subscribe(
        data => {
          this.cursoControl.setValue(data.listMap[0].curso);
          this.form.get('matriz').setValue(data.listMap[0])
        }
      )
    }
  }

  compareMatrizFunction(o1: any, o2: any){
    return (o1 != null && o2 != null) && o1.id == o2.id;
  }

  delete() {
    this.dataService.delete('disciplina', this.disciplina.id).pipe(first()).subscribe(_ => {
      this.notification.success('Registro excluído com sucesso!');
      this.router.navigate(['../'], {relativeTo: this.route})
    }, error => {
      this.notification.handleError(error);
    })
  }
}
