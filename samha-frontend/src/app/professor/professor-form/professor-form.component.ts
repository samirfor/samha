import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Observable, Subscription} from 'rxjs';
import {DataService} from '../../shared/service/data.service';
import {restricaoProfessorColumns} from '../../meta-model/restricao-professor';
import {IFormComponent} from '../../meta-model/iform-component';
import {ActivatedRoute, Router} from '@angular/router';
import {first} from 'rxjs/operators';
import {NotificationService} from "../../shared/service/notification.service";

@Component({
  selector: 'samha-professor-form',
  templateUrl: './professor-form.component.html'
})
export class ProfessorFormComponent implements OnInit, IFormComponent, OnDestroy {
  form: FormGroup;
  coordenadoria$: Observable<any>;
  professor$: Observable<any>;
  restricaoColumns = restricaoProfessorColumns;
  professor: any = null;
  subscription: Subscription;
  showRestrictions: boolean = false;

  constructor(private dataService: DataService,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private notification: NotificationService,
              private router: Router) { }

  ngOnInit(): void {
    this.subscription = this.route.data.subscribe(
      data => {
        this.professor = data.professor;
        this.loadForm();
      }
    )
  }


  salvar() {
    if(this.form.invalid){
      this.form.markAllAsTouched();
      return;
    }

    this.setProfessorData();
    if(this.professor?.id){
     this.dataService.update('professor', this.professor.id, this.professor).pipe(first()).subscribe(
       next => {
         this.form.markAsUntouched();
         this.form.markAsPristine();
         this.notification.success('Professor atualizado com sucesso!');
         this.router.navigate(['../', next.id], {relativeTo: this.route})
       }, error => this.notification.handleError(error)
     )
    }else {
      this.dataService.save('professor', this.professor).pipe(first()).subscribe(
        next => {
          this.notification.success('Professor salvo com sucesso!');
          this.router.navigate(['../' + next.id], {relativeTo: this.route})
        }, error => this.notification.handleError(error)
      );
    }
  }

  private setProfessorData() {
    this.professor = {
      id: this.professor?.id,
      nome: this.form.get('nome').value,
      coordenadoria:{
        id: this.form.get('coordenadoria').value
      },
      matricula: this.form.get('matricula').value,
      email: this.form.get('email').value,
      cargaHoraria: this.form.get('carga_horaria').value,
      ativo: this.form.get('ativo').value
    }
  }

  canDeactivateRoute(): boolean {
    let exit = true;

    if(this.form.dirty && this.form.invalid){
      exit = confirm('Descartar alterações?')
    };

    return exit;
  }

  private loadForm() {
    if(this.professor !== undefined) this.showRestrictions = true;
    this.form = this.formBuilder.group({
      nome: [this.professor?.nome, [Validators.required, Validators.maxLength(255)]],
      email: [this.professor?.email, [Validators.required, Validators.email, Validators.maxLength(255)]],
      matricula: [this.professor?.matricula, [Validators.required, Validators.maxLength(255)]],
      carga_horaria: [this.professor?.cargaHoraria, [Validators.required, Validators.maxLength(2), Validators.minLength(1), Validators.max(99), Validators.min(1)]],
      coordenadoria: [this.professor?.coordenadoria?.id, Validators.required],
      ativo: [this.professor?.ativo !== undefined ? this.professor.ativo : true, Validators.required]
    });
    this.coordenadoria$ = this.dataService.getAll('coordenadoria');
  }

  ngOnDestroy() {
    this.subscription.unsubscribe()
  }

  goBack() {
    this.router.navigate(['../'], {relativeTo: this.route});
  }

  delete() {
    this.dataService.delete('professor', this.professor.id).pipe(first()).subscribe(_ => {
      this.notification.success('Registro excluído com sucesso!');
      this.router.navigate(['../'], {relativeTo: this.route})
    }, error => {
      this.notification.handleError(error);
    })
  }
}
