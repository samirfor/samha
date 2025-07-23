import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DataService} from './service/data.service';
import {LocalStorageService} from './service/local-storage.service';
import {ToolbarComponent} from './toolbar/toolbar.component';
import {TableComponent} from './table/table.component';
import {MaterialModule} from './material/material.module';
import {TableDialogComponent} from './table-dialog/table-dialog.component';
import {HttpClientModule} from '@angular/common/http';
import {AuthService} from './service/auth.service';
import {RouterModule} from '@angular/router';
import {ConfirmDialogComponent} from './confirm-dialog/confirm-dialog.component';
import {AutocompleteFieldComponent} from './fields/autocomplete-field/autocomplete-field.component';
import {LogComponent} from './log/log.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NotificationService} from "./service/notification.service";
import { AlteracaoDialogComponent } from './alteracao-dialog/alteracao-dialog.component';
import {DxButtonModule, DxPopupModule, DxTextAreaModule, DxTextBoxModule} from "devextreme-angular";


@NgModule({
  declarations: [
    ToolbarComponent,
    TableComponent,
    TableDialogComponent,
    ConfirmDialogComponent,
    AutocompleteFieldComponent,
    LogComponent,
    AlteracaoDialogComponent
  ],
    imports: [
        CommonModule,
        MaterialModule,
        ReactiveFormsModule,
        HttpClientModule,
        RouterModule,
        FormsModule,
        DxButtonModule,
        DxTextAreaModule,
        DxTextBoxModule,
        DxPopupModule
    ],
  exports: [
    ToolbarComponent,
    TableComponent,
    ReactiveFormsModule,
    HttpClientModule,
    AutocompleteFieldComponent,
    LogComponent,

  ],
  providers: [
    DataService,
    LocalStorageService,
    AuthService,
    NotificationService
  ],
})
export class SharedModule {
}
