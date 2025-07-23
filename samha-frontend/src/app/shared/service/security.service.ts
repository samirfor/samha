import {HostListener, Injectable, OnDestroy} from "@angular/core";
import {AuthService} from "./auth.service";
import {first} from "rxjs/operators";
import {Router} from "@angular/router";
import {error} from "protractor";
import {NotificationService} from "./notification.service";
import {Subscription} from "rxjs";
import {LocalStorageService} from "./local-storage.service";
import {time} from "html2canvas/dist/types/css/types/time";


/**
 * Caso o usuário nao mexa o mouse durante 30 minutos, ele é considerado inativo.
 * Caso ele esteja inativo no momento da renovação do access_token, sua sessão é terminada.
 * O access token está programado para vencer em 5 horas, e o refresh_token em 10 horas.
 * Se no final dessa sessão de 5 horas o usuário continuar ativo, sua sessão é renovada para 10 horas.
 * Ao atingir 9 horas e 50 minutos, o sistema enviará uma mensagem para o usuário perguntando se ele deseja manter sua sessão ativa,
 * Caso ele não responda ou cancele a mensagem, sua sessão terminará em 10 minutos.
 * Caso a resposta seja positiva, seu access_token é renovado para mais 5 horas e seu refresh token para mais 10 horas.
 * @variable isUserActive é processada pelo app.component.ts;
 */
@Injectable({providedIn: 'root'})
export class SecurityService {
  private accessTokenTimer;
  private refreshTokenTimer;
  private refreshTokenWarnTimer;
  public subs: Subscription;

  constructor(private authService: AuthService,
              private router: Router,
              private notification: NotificationService,
              private localStorageService: LocalStorageService) {
  }

  private getExpirationTime(token: string): Date {
    const tokenParts = token.split('.');

    if (tokenParts.length !== 3) {
      // Invalid token format
      return new Date();
    }

    const [, payloadBase64] = tokenParts;
    const payloadJson = atob(payloadBase64);
    const payload = JSON.parse(payloadJson);

    if (!payload.exp) {
      // Expiration time not found in the payload
      return new Date();
    }

    return new Date(payload.exp * 1000); // Convert expiration time to milliseconds
  }

  private handleAccessTokenExpiration(): void {
    this.authService.refreshToken().pipe(first()).subscribe(response => {
      this.localStorageService.set("access_token", response.access_token);
    })
  }

  private handleRefreshTokenExpiration() {
    let stay = confirm('Sua sessão expira em 1000 minutos! Clique em OK para renovar sua sessão.');
    const timeout = setTimeout(() => {
      this.logout();
    }, 9 * 60 * 1000)
    if (stay) {
      clearInterval(timeout);
      this.authService.refreshSession().pipe(first()).subscribe(response => {
        this.localStorageService.clearTokens();
        this.localStorageService.set('access_token', response.access_token);
        this.localStorageService.set('refresh_token', response.refresh_token);
        this.initialize();
      }, error => this.notification.handleError(error))
    }
  }

  public initialize() {
    const access_token = this.localStorageService.get('access_token');
    const refresh_token = this.localStorageService.get('refresh_token');
    this.subs = this.authService.isTokenValid().subscribe(
      valid => {
        if (access_token && refresh_token && valid) {
          const currentDate = new Date();
          const accessTokenExpiration = this.getExpirationTime(access_token);
          const refreshTokenExpiration = this.getExpirationTime(refresh_token);
          const differenceBetweenAccess = this.getDifferenceBetweenHours(accessTokenExpiration, currentDate);
          const differenceBetweenRefresh = this.getDifferenceBetweenHours(refreshTokenExpiration, currentDate);

          if (differenceBetweenAccess <= 0) this.localStorageService.clearTokens();
          if (differenceBetweenAccess <= 0 && differenceBetweenRefresh > 0) {
            this.handleAccessTokenExpiration();
          }else {
            const refreshTokenWarningTime = differenceBetweenRefresh * 60 * 60 * 1000;
            this.clearTimers();
            this.accessTokenTimer = setTimeout(() => {
              this.handleAccessTokenExpiration();
            }, differenceBetweenAccess * 60 * 60 * 1000)
            this.refreshTokenWarnTimer = setTimeout(() => {
              this.handleRefreshTokenExpiration();
            }, refreshTokenWarningTime)
            this.refreshTokenTimer = setTimeout(() => {
              this.logout();
            }, differenceBetweenRefresh * 60 * 60 * 1000)
          }
        }
      }
    )
  }

  private logout() {
    this.localStorageService.clearTokens();
    this.router.navigate(['login']).then(_ => window.location.reload());
  }

  private clearTimers() {
    if (this.accessTokenTimer) clearTimeout(this.accessTokenTimer);
    if (this.refreshTokenTimer) clearTimeout(this.refreshTokenTimer);
    if (this.refreshTokenWarnTimer) clearTimeout(this.refreshTokenWarnTimer);
  }

  private getDifferenceBetweenHours(tokenDate: Date, currentDate: Date): number {
    if (tokenDate.getDay() > currentDate.getDay()) return tokenDate.getHours() + 24 - currentDate.getHours();
    else if (tokenDate.getDay() === currentDate.getDay()) return tokenDate.getHours() - currentDate.getHours();
    else return -1;
  }
}
