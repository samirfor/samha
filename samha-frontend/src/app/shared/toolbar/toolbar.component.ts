import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Router} from '@angular/router';
import {LocalStorageService} from '../service/local-storage.service';
import {AuthService} from "../service/auth.service";

@Component({
  selector: 'samha-toolbar',
  templateUrl: './toolbar.component.html'
})
export class ToolbarComponent implements OnInit {
  @Output() sideBarClicked: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Input() opened: boolean;
  public showInfo: boolean = false;
  public showEmail: boolean = false;

  constructor(private router: Router,
              private localStorage: LocalStorageService,
              private authService: AuthService) { }

  ngOnInit(): void {
  }

  onClick() {
    this.sideBarClicked.emit(null);
  }

  onExitClick() {
    this.authService.isLogado = false;
    this.authService.loggedIn.emit(false);
    this.localStorage.clearTokens();
    window.location.reload();
  }

  isLogado() {
    return this.authService.isLogado;
  }

  onSignInClick() {
    this.router.navigate(['login']);
  }

  onInfoClick() {
    this.showInfo = true;
  }

  onShowEmail() {
    this.showEmail = true;
    setTimeout(() => {
      this.showEmail = false;
    }, 10000)

  }
}
