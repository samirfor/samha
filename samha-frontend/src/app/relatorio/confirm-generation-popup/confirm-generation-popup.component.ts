import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'samha-confirm-generation-popup',
  templateUrl: './confirm-generation-popup.component.html'
})
export class ConfirmGenerationPopupComponent implements OnInit {
  @Input() showPopup: boolean;
  @Input() warnMessage: string;
  @Output() showPopupChange = new EventEmitter<boolean>();
  @Output() onOptionChoosen = new EventEmitter<boolean>();

  constructor() { }

  ngOnInit(): void {
  }

  onConfirmarClick() {
    this.showPopup = false;
    this.onOptionChoosen.emit(true);
  }

  onCancelarClick() {
    this.showPopup = false;
    this.onOptionChoosen.emit(false);
  }

}
