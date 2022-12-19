import { Component, Inject } from '@angular/core';
import { MeetingsComponent } from '../meetings/meetings.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Meetings } from '../meetings/models/meetings.model';

@Component({
  selector: 'app-meetings-dialog',
  templateUrl: './meetings-dialog.component.html',
  styleUrls: ['./meetings-dialog.component.scss'],
})
export class MeetingsDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<MeetingsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Meetings
  ) {}
}
