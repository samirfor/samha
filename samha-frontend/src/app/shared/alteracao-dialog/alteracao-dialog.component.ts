import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'samha-alteracao-dialog',
  templateUrl: './alteracao-dialog.component.html'
})
export class AlteracaoDialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<AlteracaoDialogComponent>,
  ) {}

  ngOnInit(): void {
  }

  onDescartarClick() {
    this.dialogRef.close('descartar');
  }

  onSalvarClick() {
    this.dialogRef.close('salvar');
  }

  onCancelarClick() {
    this.dialogRef.close('cancelar');
  }
}
