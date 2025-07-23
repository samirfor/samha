import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'samha-confirm-dialog',
  templateUrl: './confirm-dialog.component.html'
})
export class ConfirmDialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
  ) {}

  ngOnInit(): void {
  }

  onNoClick() {
    this.dialogRef.close(false);
  }

  onSimClick() {
    this.dialogRef.close(true);
  }
}
