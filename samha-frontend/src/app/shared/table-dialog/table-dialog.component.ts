import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DialogData} from "../../meta-model/dialog-data";

@Component({
  selector: 'samha-table-dialog',
  templateUrl: './table-dialog.component.html',
  styleUrls: ['./table-dialog.component.css']
})
export class TableDialogComponent implements OnInit {

  entityId: number = null;

  constructor(
    public dialogRef: MatDialogRef<TableDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
  ) {}

  onNoClick(): void {
    this.entityId = null;
    this.dialogRef.close();
  }

  ngOnInit(): void {
  }

  setEntityId($event: number) {
    this.entityId = $event;
  }
  salvar(): void{
    this.dialogRef.close(this.entityId);
  }
}
