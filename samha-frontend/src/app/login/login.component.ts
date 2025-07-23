import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {LocalStorageService} from '../shared/service/local-storage.service';
import {DataService} from '../shared/service/data.service';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {TokenResponseModel} from '../meta-model/token-model';
import {AuthService} from '../shared/service/auth.service';
import {NotificationService} from "../shared/service/notification.service";
import {first} from "rxjs/operators";
import {SecurityService} from "../shared/service/security.service";

@Component({
  selector: 'samha-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  public trocarSenha: boolean = false;
  private subscription: Subscription;
  public senhasInvalidas: boolean = false;

  constructor(private formBuilder: FormBuilder,
              private localStorage: LocalStorageService,
              private securityService: SecurityService,
              private authService: AuthService,
              private router: Router,
              private notification: NotificationService) {
  }

  form: FormGroup;
  hide: boolean = true;

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      login: [null, Validators.required],
      senha: [null, [Validators.required, Validators.minLength(3)]],
      novaSenha: [null],
      confirmarSenha: [null]
    });
  }

  login(): void {
    if (this.form.valid){
        let body = new URLSearchParams();
        body.set('login', this.form.value.login);
        body.set('senha', this.form.value.senha);
        this.sign(body);
    }else{
      this.form.markAllAsTouched();
    }
  }

  private sign(body: URLSearchParams): void {
    this.subscription = this.authService.login(body.toString()).subscribe(
      (result: TokenResponseModel)=> {
        const regex = new RegExp('^(?=.*[!@#$%^&*(),.?":{}|<>])(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$');
        const isSenhaValida = regex.test(this.form.value.senha);
        if (isSenhaValida) {
          this.localStorage.set("access_token", result.access_token);
          this.localStorage.set("refresh_token", result.refresh_token);
          //todo: verificar se irá utilizar
          this.authService.loggedIn.emit(true);
          this.authService.isLogado = true;
          this.router.navigate(['home']);
        } else {
          this.localStorage.clearTokens();
          const novaSenha = this.form.get('novaSenha');
          const confirmarSenha = this.form.get('confirmarSenha');
          novaSenha.addValidators([Validators.required, Validators.pattern('^(?=.*[!@#$%^&*(),.?":{}|<>])(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$')])
          confirmarSenha.addValidators([Validators.required, Validators.pattern('^(?=.*[!@#$%^&*(),.?":{}|<>])(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$')])
          novaSenha.markAsUntouched();
          confirmarSenha.markAsUntouched();
          novaSenha.markAsPristine();
          confirmarSenha.markAsPristine();
          this.trocarSenha = true;
        }
      },
      (error) => {
        this.authService.loggedIn.emit(false);
        this.authService.isLogado = false;
        this.notification.error("Usuário não encontrado");
        throw error;
      }
    );
  }

  ngOnDestroy() {
    this.subscription?.unsubscribe();
  }

  goToRelatorios() {
    this.router.navigateByUrl('relatorio');
  }

  onTrocarSenhaClicked() {
    const novaSenha = this.form.get('novaSenha');
    const confirmarSenha = this.form.get('confirmarSenha');
    if (novaSenha.value != confirmarSenha.value || !novaSenha.valid || !confirmarSenha.valid) {
      this.senhasInvalidas = true;
      return;
    } else {
      this.form.get('senha').setValue(novaSenha.value);
      let body = {
        login: this.form.get('login').value,
        senha: novaSenha.value
      }
      this.authService.changePassword(body).pipe(first()).subscribe(
        _ => {
          let body = new URLSearchParams();
          body.set('login', this.form.value.login);
          body.set('senha', novaSenha.value);
          this.sign(body);
        }
      );
    }
  }

  onKeyDown() {
    this.senhasInvalidas = false
  }
}
