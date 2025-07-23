import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {AuthService} from "../../shared/service/auth.service";

@Component({
  selector: 'samha-relatorio',
  templateUrl: './relatorio.component.html'
})
export class RelatorioComponent implements OnInit, OnDestroy {
  public semestreControl: FormControl;
  public anoControl: FormControl;
  public enviarEmailControl = new FormControl(false);
  public senhaControl = new FormControl(null, Validators.required);
  public authenticated: boolean = false;
  private loggedSub: Subscription
  public tabs = [
    {
      text: 'Turmas',
      id: 0
    },
    {
      text: 'Professores',
      id: 1
    },
    {
      text: 'Disciplinas',
      id: 2
    }
  ];
  public selectedTab: number = 0;

  constructor(private authService: AuthService) {
    this.anoControl = new FormControl(new Date().getFullYear());
    this.semestreControl = new FormControl(new Date().getMonth() < 6 ? 1 : 2);
  }

  ngOnInit(): void {
    this.loggedSub = this.authService.isTokenValid().subscribe(
      () => this.authenticated = true,
      _ => this.authenticated = false
    )
  }

  selectTab = (e: any) => this.selectedTab = e.itemIndex;

  ngOnDestroy(): void {
    this.loggedSub?.unsubscribe();
  }
}
