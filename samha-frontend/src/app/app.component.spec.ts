import {of, Subject} from 'rxjs';
import {AppComponent} from './app.component';
import {MenuEnum} from './shared/menu-enum';
import {DataService} from './shared/service/data.service';
import {LocalStorageService} from './shared/service/local-storage.service';
import {Router} from '@angular/router';
import {AuthService} from './shared/service/auth.service';
import {SecurityService} from './shared/service/security.service';

describe('AppComponent', () => {
  let component: AppComponent;
  const router = { navigate: jasmine.createSpy('navigate') } as unknown as Router;
  const authService = {
    isTokenValid: jasmine.createSpy('isTokenValid').and.returnValue(of(true)),
    isLogado: false,
    loggedIn: new Subject<boolean>()
  } as unknown as AuthService;
  const securityService = {
    initialize: jasmine.createSpy('initialize'),
    subs: undefined
  } as unknown as SecurityService;

  beforeEach(() => {
    component = new AppComponent(
      {} as DataService,
      {} as LocalStorageService,
      router,
      authService,
      securityService
    );
  });

  it('orders menus keeping key items first', () => {
    const menus = component.getMenuOrdenado([
      {id: MenuEnum.OFERTAS, nome: 'Ofertas'},
      {id: MenuEnum.COORDENADORES, nome: 'Coordenadores'},
      {id: MenuEnum.RELATORIOS, nome: 'Relatórios'},
      {id: MenuEnum.PROFESSORES, nome: 'Professores'},
      {id: MenuEnum.ALOCACOES, nome: 'Alocações'}
    ]);

    expect(menus.map(menu => menu.id)).toEqual([
      MenuEnum.OFERTAS,
      MenuEnum.ALOCACOES,
      MenuEnum.RELATORIOS,
      MenuEnum.COORDENADORES,
      MenuEnum.PROFESSORES
    ]);
  });

  it('toggles the sidebar state', () => {
    component.opened = false;

    component.onSideBarClicked();

    expect(component.opened).toBeTrue();
  });

  it('maps menu ids to material icons', () => {
    expect(component.loadMatIcon(MenuEnum.CADASTRARUSUARIOS)).toBe('group_add');
    expect(component.loadMatIcon(MenuEnum.RELATORIOS)).toBe('picture_as_pdf');
  });
});
