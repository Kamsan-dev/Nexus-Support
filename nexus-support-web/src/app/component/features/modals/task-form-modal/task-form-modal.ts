import { ChangeDetectionStrategy, Component, inject, input } from '@angular/core';
import { DialogRef } from '@ngneat/dialog';
import { TicketStatusEnum, TicketStatusLabel } from '../../../../enum/ticket-status';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';
import { Task } from '../../../../core/model/ticket.model';
import { CommonModule } from '@angular/common';
import { TicketDetailsStore } from '../../../../store/ticket-details.store';

@Component({
  selector: 'app-task-form-modal',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './task-form-modal.html',
  styleUrl: './task-form-modal.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskFormModal {
  public readonly dialogRef = inject(DialogRef);
  private fb = inject(FormBuilder);
  readonly data = this.dialogRef.data;
  protected readonly ticketStatusLabel = TicketStatusLabel;
  protected readonly ticketStatus = Object.values(TicketStatusEnum);
  readonly ticketStore = inject(TicketDetailsStore);

  taskForm!: FormGroup;

  ngOnInit(): void {
    this.initTaskForm(this.data.task);
  }

  private initTaskForm(task: Task | null): void {
    this.taskForm = this.fb.group({
      name: [task?.name ?? '', Validators.required],
      description: [task?.description ?? '', Validators.required],
      status: [task?.status ?? 'no_selection', [Validators.required, this.statusValidator]],
    });
  }

  onFormSubmit = (event: TouchEvent | MouseEvent): void => {
    event.stopImmediatePropagation();

    if (this.taskForm.invalid) return;

    if (this.data.mode === 'create') {
      this.ticketStore.createTask({ ticketPublicId: this.data.ticketPublicId, ...this.taskForm.value });
    }
  };

  get f(): { [key: string]: AbstractControl } {
    return this.taskForm.controls;
  }

  private readonly statusValidator: ValidatorFn = (control) => (control.value === 'no_selection' ? { required: true } : null);
}
