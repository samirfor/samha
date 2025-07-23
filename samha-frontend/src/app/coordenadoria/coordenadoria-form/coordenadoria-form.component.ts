import { Component, OnInit } from '@angular/core';
import {Observable} from 'rxjs';
import {DataService} from '../../shared/service/data.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {first} from 'rxjs/operators';
import {NotificationService} from "../../shared/service/notification.service";

@Component({
  selector: 'samha-coordenadoria',
  templateUrl: './coordenadoria-form.component.html'
})
export class CoordenadoriaFormComponent implements OnInit {
  eixos$: Observable<any>;
  coord: any = {};

  form: FormGroup;

  constructor(private dataService: DataService,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private notification: NotificationService) { }

  ngOnInit(): void {
    this.eixos$ = this.dataService.getAll('eixo');
    this.route.data.pipe(first()).subscribe(
      next => {
        this.coord = next.coord;
        this.loadForm();
      }
    )
  }

  goBack = () => this.router.navigate(['../'], {relativeTo: this.route});

  salvar() {
    if(this.form.invalid){
      this.form.markAllAsTouched();
      return;
    }

    this.buildEntityData();
    if(this.coord?.id){
      this.dataService.update('coordenadoria', this.coord.id, this.coord).pipe(first()).subscribe(
        next => {
          this.coord = next;
          this.notification.success('Coordenadoria atualizada com sucesso!');
          this.router.navigate(['../', next.id], {relativeTo: this.route});
        },
        error => this.notification.handleError(error)
      )
    }else{
      this.dataService.save('coordenadoria', this.coord).pipe(first()).subscribe(
        next => {
          this.notification.success('Coordenadoria criada com sucesso!');
          this.router.navigate(['../', next.id], {relativeTo: this.route});
        },
        error => this.notification.handleError(error)
      )
    }
  }

  private loadForm() {
    this.form = this.formBuilder.group({
      nome: [this.coord?.nome, [Validators.required, Validators.maxLength(255)]],
      eixo: [this.coord?.eixo, [Validators.required]]
    })
  }

  compareFunction(o1: any, o2: any) {
    return (o1 != null && o2 != null && o1.id == o2.id);
  }

  private buildEntityData() {
    let entity = {
      id: this.coord?.id,
      nome: this.form.get('nome').value,
      eixo: this.form.get('eixo').value
    }
    this.coord = entity;
  }

  delete() {
    this.dataService.delete('coordenadoria', this.coord.id).pipe(first()).subscribe(_ => {
      this.notification.success('Registro excluído com sucesso!');
      this.router.navigate(['../'], {relativeTo: this.route})
    }, error => {
      this.notification.handleError(error);
    })
  }
}
