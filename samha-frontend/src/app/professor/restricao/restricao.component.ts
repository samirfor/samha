import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ThemePalette} from '@angular/material/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {DataService} from '../../shared/service/data.service';
import {first} from 'rxjs/operators';
import {IFormComponent} from '../../meta-model/iform-component';
import {NotificationService} from "../../shared/service/notification.service";

@Component({
  selector: 'samha-restricao',
  templateUrl: './restricao.component.html',
  styleUrls: ['./restricao.component.css']
})
export class RestricaoComponent implements OnInit, IFormComponent, OnDestroy {
  private subscription: Subscription;
  public restricao: any = {};
  professorId: string;
  form: FormGroup;
  aula: Aula = {
    name: 'Aulas',
    completed: false,
    color: 'primary',
    aulaItems: [
      {name: 'Aula 1', completed: false, color: 'accent'},
      {name: 'Aula 2', completed: false, color: 'accent'},
      {name: 'Aula 3', completed: false, color: 'accent'},
      {name: 'Aula 4', completed: false, color: 'accent'},
      {name: 'Aula 5', completed: false, color: 'accent'},
      {name: 'Aula 6', completed: false, color: 'accent'},
    ],
  };

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private dataService: DataService,
              private router: Router,
              private notification: NotificationService) {

  }

  ngOnInit(): void {
    this.professorId = this.route.snapshot.params['entity'];
    this.subscription = this.route.data.subscribe(
      data => {
        if (data.restricao !== undefined) {
          this.restricao = data.restricao;
        } else {
          this.restricao = {
            aula1: false,
            aula2: false,
            aula3: false,
            aula4: false,
            aula5: false,
            aula6: false,
          };
        }
        this.loadForm();
      }
    )
  }

  allComplete: boolean = false;

  updateAllComplete() {
    this.allComplete = this.aula.aulaItems != null && this.aula.aulaItems.every(t => t.completed);
  }

  someComplete(): boolean {
    if (this.aula.aulaItems == null) {
      return false;
    }
    return this.aula.aulaItems.filter(t => t.completed).length > 0 && !this.allComplete;
  }

  setAll(completed: boolean) {
    this.allComplete = completed;
    if (this.aula.aulaItems == null) {
      return;
    }
    this.aula.aulaItems.forEach(t => (t.completed = completed));
  }

  salvar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.form.markAsPristine();
      return;
    }
    this.setNewResourceData();
    if (this.restricao.id) {
      this.dataService.update('restricaoProfessor', this.restricao.id, this.restricao).pipe(
        first()
      ).subscribe(
        data => {
          this.notification.success('A Restrição foi atualizada com sucesso!');
          this.router.navigate(['../', data.id], {relativeTo: this.route})
        },
        error => this.notification.handleError(error)
      )
    } else {
      this.dataService.save('restricaoProfessor', this.restricao).pipe(first()).subscribe(
        next => {
          this.notification.success('A Restrição foi criada com sucesso!');
          this.router.navigate(['../', next.id], {relativeTo: this.route})
        },
        error => this.notification.handleError(error)
      );
    }
  }

  private loadForm() {
    this.form = this.formBuilder.group({
      nome: [this.restricao?.nome, [Validators.required, Validators.maxLength(255)]],
      turno: [this.restricao?.turno, Validators.required],
      dia: [this.restricao?.dia, Validators.required],
      aula: [null],
      prioridade: [this.restricao?.prioridade, Validators.required],
      descricao: [this.restricao?.descricao, Validators.required],
    });

    this.loadCheckboxes();
  }

  compareFunction(o1: string, o2: string) {
    return (o1 != null && o2 != null && o1.toUpperCase() == o2.toUpperCase());
  }

  private loadCheckboxes() {
    this.aula.aulaItems.forEach(aula => {
      switch (aula.name) {
        case 'Aula 1':
          aula.completed = this.restricao?.aula1;
          break;
        case 'Aula 2':
          aula.completed = this.restricao?.aula2;
          break;
        case 'Aula 3':
          aula.completed = this.restricao?.aula3;
          break;
        case 'Aula 4':
          aula.completed = this.restricao?.aula4;
          break;
        case 'Aula 5':
          aula.completed = this.restricao?.aula5;
          break;
        case 'Aula 6':
          aula.completed = this.restricao?.aula6;
          break;
      }
    })
  }

  updateEntity(aula: Aula) {
    switch (aula.name) {
      case 'Aula 1':
        this.restricao.aula1 = aula.completed;
        break;
      case 'Aula 2':
        this.restricao.aula2 = aula.completed;
        break;
      case 'Aula 3':
        this.restricao.aula3 = aula.completed;
        break;
      case 'Aula 4':
        this.restricao.aula4 = aula.completed;
        break;
      case 'Aula 5':
        this.restricao.aula5 = aula.completed;
        break;
      case 'Aula 6':
        this.restricao.aula6 = aula.completed;
        break;
    }
  }

  private setNewResourceData() {
    this.restricao.id = null;
    this.restricao.nome = this.form.get('nome').value;
    this.restricao.descricao = this.form.get('descricao').value;
    this.restricao.professor = {
      id: this.professorId
    }
    this.restricao.turno = this.form.get('turno').value;
    this.restricao.dia = this.form.get('dia').value;
    this.restricao.prioridade = this.form.get('prioridade').value;
  }

  canDeactivateRoute(): boolean {
    return true;
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  goBack() {
    this.router.navigate(['../../'], {relativeTo: this.route});
  }

  delete() {
    this.dataService.delete('restricaoProfessor', this.restricao.id).pipe(first()).subscribe(_ => {
      this.notification.success('Registro excluído com sucesso!');
      this.router.navigate(['../../'], {relativeTo: this.route})
    }, error => {
      this.notification.handleError(error);
    })
  }
}

export interface Aula {
  name: string;
  completed: boolean;
  color: ThemePalette;
  aulaItems?: Aula[];
}
