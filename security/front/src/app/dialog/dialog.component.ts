import { Component,Inject  } from '@angular/core';
import { MatDialogRef,MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.css']
})
export class DialogComponent {
  message: string;

  constructor(
    public dialogRef: MatDialogRef<DialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any // Pristupate prosleđenim podacima ovde
  ) {
    this.message = data.message; // Postavite poruku iz prosleđenih podataka
  }

  close(): void {
    this.dialogRef.close();
  }
}