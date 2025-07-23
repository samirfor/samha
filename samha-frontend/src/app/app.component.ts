import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {Observable, of, Subscription} from 'rxjs';
import {MenuEnum} from './shared/menu-enum';
import {TableColumnModel} from './meta-model/table-column-model';
import {DataService} from './shared/service/data.service';
import {LocalStorageService} from './shared/service/local-storage.service';
import {Router} from '@angular/router';
import {AuthService} from './shared/service/auth.service';
import {catchError, map} from 'rxjs/operators';
import { locale } from "devextreme/localization";
import {SecurityService} from "./shared/service/security.service";

@Component({
  selector: 'samha-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy{
  columns: TableColumnModel[];
  opened = false;
  menus$: Observable<any>;
  showMenu$: Observable<boolean>;
  private userActiveTimer;
  private subscription: Subscription;
  constructor(private dataService: DataService,
              private localStorage: LocalStorageService,
              private router: Router,
              private authService: AuthService,
              private securityService: SecurityService) {
    locale(navigator.language);
  }

  ngOnInit(): void {
    this.securityService.initialize();
    this.showMenu$ = this.authService.isTokenValid().pipe(
      map( _ => {
        this.opened = true;
        this.authService.isLogado = true;
        this.menus$ = this.dataService.post('menu/list', null)
        return true;
      }),
      catchError(
        _ => {
          this.opened = false;
          this.menus$ = of([]);
          this.subscription = this.authService.loggedIn.subscribe(
            next => {
              if(next) this.ngOnInit();
            },
            error => {
              this.router.navigate(['/login']).then(_ => location.reload());
              throw error;
            }
          )
          return of(true);
        }
      )
    );
  }

  onSideBarClicked() {
    this.opened = !this.opened;
  }

  onMenuClick(menu: number) {
    switch (menu) {
      case MenuEnum.PROFESSORES:
        this.router.navigate(['professor']);
        break;
      case MenuEnum.COORDENADORES:
        this.router.navigate(['coordenador']);
        break;
      case MenuEnum.ALOCACOES:
        this.router.navigate(['alocacao']);
        break;
      case MenuEnum.DISCIPLINAS:
        this.router.navigate(['disciplina']);
        break;
      case MenuEnum.CADASTRARUSUARIOS:
        this.router.navigate(['usuario']);
        break;
      case MenuEnum.CURSO:
        this.router.navigate(['curso']);
        break;
      case MenuEnum.OFERTAS:
        this.opened = false;
        this.router.navigate(['oferta']);
        break;
      case MenuEnum.RELATORIOS:
        this.router.navigate(['relatorio']);
        break;
      case MenuEnum.TURMAS:
        this.router.navigate(['turma']);
        break;
      case MenuEnum.COORDENADORIA:
        this.router.navigate(['coordenadoria']);
        break;
      case MenuEnum.EIXO:
        this.router.navigate(['eixo']);
        break;
      case MenuEnum.MATRIZ:
        this.router.navigate(['matrizCurricular']);
        break;
      case MenuEnum.HORARIOS_AULA:
        this.router.navigate(['label']);
        break;
    }
  }

  loadMatIcon(menu: number) {
    switch (menu) {
      case MenuEnum.PROFESSORES:
        return 'person';
      case MenuEnum.COORDENADORES:
        return 'supervised_user_circle';
      case MenuEnum.ALOCACOES:
        return 'link';
      case MenuEnum.DISCIPLINAS:
        return 'library_books';
      case MenuEnum.CADASTRARUSUARIOS:
        return 'group_add';
      case MenuEnum.CURSO:
        return 'school';
      case MenuEnum.OFERTAS:
        return 'dashboard';
      case MenuEnum.RELATORIOS:
        return 'picture_as_pdf';
      case MenuEnum.TURMAS:
        return 'group_work';
      case MenuEnum.COORDENADORIA:
        return 'layers'
      case MenuEnum.EIXO:
        return '360';
      case MenuEnum.MATRIZ:
        return 'grain';
      case MenuEnum.HORARIOS_AULA:
        return 'label';
    }
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
    this.securityService.subs?.unsubscribe()
  }

  protected readonly open = open;

  getMenuOrdenado(menusSource: any[]) {
    const menuOferta = menusSource.find(m => m.id === MenuEnum.OFERTAS);
    const menuAlocacoes = menusSource.find(m => m.id === MenuEnum.ALOCACOES);
    const menuRelatorios = menusSource.find(m => m.id === MenuEnum.RELATORIOS);
    let menus: any[] = [];
    if (menusSource.length > 0) {
      menus.push(...[menuOferta, menuAlocacoes, menuRelatorios]);
      let menusRestantes = menusSource.filter(m => m.id !== MenuEnum.OFERTAS && m.id !== MenuEnum.RELATORIOS && m.id !== MenuEnum.ALOCACOES).sort(
        (a, b) => {
          if (a.nome > b.nome) return 1;
          else if (a.nome < b.nome) return -1;
          else return 0;
        }
      );
      menus.push(...menusRestantes);
    }
    return menus;
  }
}

export const APIPREFIX = 'api/';
