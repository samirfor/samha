import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {SharedModule} from './shared/shared.module';
import {LoginComponent} from './login/login.component';
import {HomeComponent} from './home/home.component';
import {AuthGuard} from './guards/auth-guard';
import {AppRoutingModule} from './app-routing.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {CanDeactivateGuard} from './guards/can-deactivate-guard';
import {FormResolver} from './guards/form-resolver';
import {MaterialModule} from './shared/material/material.module';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {DxButtonModule} from "devextreme-angular";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent
  ],
    imports: [
        MaterialModule,
        BrowserModule,
        SharedModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        FontAwesomeModule,
        DxButtonModule
    ],
  providers: [
    AuthGuard,
    CanDeactivateGuard,
    FormResolver
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
