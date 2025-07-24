import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {DataService} from '../shared/service/data.service';
import {NotificationService} from "../shared/service/notification.service";
import {Observable} from "rxjs";

@Component({
  selector: 'samha-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit{
  public cursoControl = new FormControl();
  public formGroup: FormGroup;
  public formPeriodo: FormGroup;

  loading$: Observable<any>;

  constructor(private dataService: DataService,
              private notification: NotificationService,
              private formBuilder: FormBuilder
          ) {
    this.formGroup = formBuilder.group({
      curso: [''],
      ano: [],
      semestre: []
    });
  }

  ngOnInit(): void {
    const cursoSelecionadoRaw = localStorage.getItem('curso');
    const anoSelecionado = localStorage.getItem('ano');
    const semestreSelecionado = localStorage.getItem('semestre');
    
    if (cursoSelecionadoRaw && cursoSelecionadoRaw.trim() !== '') {
      const cursoSelecionado = JSON.parse(cursoSelecionadoRaw);
      this.cursoControl.setValue(cursoSelecionado);
    }

    if (anoSelecionado && anoSelecionado !== 'undefined') {
      this.formGroup.get('ano').setValue(anoSelecionado);
    }
    else {
      this.formGroup.get('ano').setValue(new Date().getUTCFullYear());
      localStorage.setItem('ano', this.formGroup.get('ano').value);
    }

    if (semestreSelecionado && semestreSelecionado !== 'undefined') {
      this.formGroup.get('semestre').setValue(semestreSelecionado);
    }
    else {
      this.formGroup.get('semestre').setValue(new Date().getMonth() < 6 ? 1 : 2);
      localStorage.setItem('semestre', this.formGroup.get('semestre').value);
    }
  }

  onCursoChange(_) {
    localStorage.setItem('curso', JSON.stringify(this.cursoControl.value));
  }

  onCursoLoaded($event: any[]) {
    const cursoSelecionadoRaw = localStorage.getItem('curso');
    if (cursoSelecionadoRaw && cursoSelecionadoRaw.trim() !== '') {
      const cursoSelecionado = JSON.parse(cursoSelecionadoRaw);
      this.cursoControl.setValue(cursoSelecionado);
    }
    else{
      if ($event.length > 0) {
        this.cursoControl.setValue($event[0]);
      } else {
        this.notification.error('Não há cursos cadastrados!');
      }
    }
  }

  onCursoSelectionOpened = () => {
    localStorage.setItem('curso', JSON.stringify(this.cursoControl.value));
  }

  onAnoChange(value: any) {
    localStorage.setItem('ano', value);
  }

  onSemestreChange(value: any) {
    localStorage.setItem('semestre', value);
  }
}
