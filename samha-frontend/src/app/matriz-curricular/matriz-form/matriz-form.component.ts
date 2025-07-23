import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {DataService} from "../../shared/service/data.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {first} from "rxjs/operators";
import {NotificationService} from "../../shared/service/notification.service";
import {error} from "protractor";

@Component({
  selector: 'samha-matriz-form',
  templateUrl: './matriz-form.component.html'
})
export class MatrizFormComponent implements OnInit {
  form: FormGroup;
  cursoControl = new FormControl();
  matriz: any = {};
  private subscription: Subscription;

  constructor(private formBuilder: FormBuilder,
              private notification: NotificationService,
              private dataService: DataService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {
    this.subscription = this.route.data.subscribe(
      next => {
        this.matriz = next.matriz;
        this.loadForm();
      }
    )
  }

  private loadForm() {
    this.form = this.formBuilder.group({
      nome: [this.matriz?.nome, [Validators.required, Validators.maxLength(255)]],
      ano: [this.matriz?.ano, [Validators.required, Validators.maxLength(4)]],
      semestre: [this.matriz?.semestre, [Validators.required, Validators.max(2), Validators.min(1)]]
    })
    this.cursoControl.setValue(this.matriz?.curso);
    this.cursoControl.addValidators([Validators.required]);
    this.form.addControl('curso', this.cursoControl);
  }

  salvar() {
    if(this.form.invalid){
      this.form.markAllAsTouched();
      return;
    }

    this.buildEntityData();

    if(this.matriz?.id){
      this.dataService.update('matrizCurricular', this.matriz.id, this.matriz).pipe(first()).subscribe(
        next => {
          this.notification.success('Matriz salva com sucesso!');
          this.router.navigate(['../', next.id], {relativeTo: this.route});
        }, error => this.notification.handleError(error)
      )
    }else {
      this.dataService.save('matrizCurricular', this.matriz).pipe(first()).subscribe(
        next => {
          this.notification.success('Matriz salva com sucesso!');
          this.router.navigate(['../', next.id], {relativeTo: this.route});
        }, error => this.notification.handleError(error)
      )
    }
  }

  goBack = () => this.router.navigate(['../'], {relativeTo: this.route});

  private buildEntityData() {
    this.matriz = {
      id: this.matriz?.id,
      nome: this.form.get('nome').value,
      ano: this.form.get('ano').value,
      semestre: this.form.get('semestre').value,
      curso: this.form.get('curso').value
    }
  }

  delete() {
    this.dataService.delete('matrizCurricular', this.matriz.id).pipe(first()).subscribe(_ => {
      this.notification.success('Registro excluído com sucesso!');
      this.router.navigate(['../'], {relativeTo: this.route})
    }, error => {
      this.notification.handleError(error);
    })
  }
}
