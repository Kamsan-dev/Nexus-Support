import { ChangeDetectionStrategy, Component, inject, input } from '@angular/core';
import { DialogRef } from '@ngneat/dialog';

@Component({
  selector: 'app-task-form-modal',
  imports: [],
  templateUrl: './task-form-modal.html',
  styleUrl: './task-form-modal.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskFormModal {
  public readonly dialogRef = inject(DialogRef);
  public test = input<number>();
}
