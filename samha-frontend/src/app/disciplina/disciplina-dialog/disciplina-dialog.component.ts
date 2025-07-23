import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DialogData} from "../../meta-model/dialog-data";

@Component({
  selector: 'samha-disciplina-dialog',
  templateUrl: './disciplina-dialog.component.html'
})
export class DisciplinaDialogComponent implements OnInit {

  entityId: number = null;

  constructor(
    public dialogRef: MatDialogRef<DisciplinaDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
  ) {}

  onClose(): void {
    this.entityId = null;
    this.dialogRef.close();
  }

  ngOnInit(): void {
  }

  setEntityId($event: number) {
    this.entityId = $event;
  }

  onSave(): void{
    this.dialogRef.close(this.entityId);
  }

  onSelectedRow(idDisciplina: number) {
      this.entityId = idDisciplina;
  }

}
