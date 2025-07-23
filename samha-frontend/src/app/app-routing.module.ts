import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from './home/home.component';
import {LoginComponent} from './login/login.component';
import {AuthGuard} from './guards/auth-guard';

const routes: Routes = [
  { path: 'relatorio', loadChildren: () => import('./relatorio/relatorio.module').then(m => m.ProfessorModule)},
  { path: '', pathMatch: 'full', redirectTo: 'home'},
  { path: 'login', component: LoginComponent},
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard], canDeactivate: [AuthGuard]},
  { path: 'label', canActivate: [AuthGuard], canDeactivate: [AuthGuard], loadChildren: () => import('./label/label.module').then(m => m.LabelModule) },
  { path: 'professor', canActivate: [AuthGuard], canDeactivate: [AuthGuard], loadChildren: () => import('./professor/professor.module').then(m => m.ProfessorModule) },
  { path: 'coordenador', canActivate: [AuthGuard], canDeactivate: [AuthGuard], loadChildren: () => import('./coordenador/coordenador.module').then(m => m.CoordenadorModule)},
  { path: 'alocacao', canActivate: [AuthGuard], canDeactivate: [AuthGuard], loadChildren: () => import('./alocacao/alocacao.module').then(m => m.AlocacaoModule)},
  { path: 'usuario', canActivate: [AuthGuard], canDeactivate: [AuthGuard], loadChildren: () => import('./usuario/usuario.module').then(m => m.UsuarioModule)},
  { path: 'curso', canActivate: [AuthGuard], canDeactivate: [AuthGuard], loadChildren: () => import('./curso/curso.module').then(m => m.CursoModule)},
  { path: 'coordenadoria', canActivate: [AuthGuard], canDeactivate: [AuthGuard], loadChildren: () => import('./coordenadoria/coordenadoria.module').then(m => m.CoordenadoriaModule)},
  { path: 'eixo', canActivate: [AuthGuard], canDeactivate: [AuthGuard], loadChildren: () => import('./eixo/eixo.module').then(m => m.EixoModule)},
  { path: 'disciplina', canActivate: [AuthGuard], canDeactivate: [AuthGuard], loadChildren: () => import('./disciplina/disciplina.module').then(m => m.DisciplinaModule)},
  { path: 'matrizCurricular', canActivate: [AuthGuard], canDeactivate: [AuthGuard], loadChildren: () => import('./matriz-curricular/matriz.module').then(m => m.MatrizModule)},
  { path: 'turma', canActivate: [AuthGuard], canDeactivate: [AuthGuard], loadChildren: () => import('./turma/turma.module').then(m => m.TurmaModule)},
  { path: 'oferta', canActivate: [AuthGuard], canDeactivate: [AuthGuard], loadChildren: () => import('./oferta/oferta.module').then(m => m.OfertaModule)},
  { path: '**', redirectTo: '/home'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes,  { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
