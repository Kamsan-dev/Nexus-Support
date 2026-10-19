import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, effect, ElementRef, inject, signal, TemplateRef, ViewChild } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { HotToastService } from '@ngxpert/hot-toast';
import { Comment, Task, TicketDetails } from '../../../../core/model/ticket.model';
import { TicketPriorityEnum, TicketPriorityLabel } from '../../../../enum/ticket-priority';
import { TicketStatusColor, TicketStatusEnum, TicketStatusLabel } from '../../../../enum/ticket-status';
import { TicketTypeEnum, TicketTypeLabel } from '../../../../enum/ticket-type';
import { AuthStore } from '../../../../store/auth.store';
import { TicketDetailsStore } from '../../../../store/ticket-details.store';
import { DialogService } from '@ngneat/dialog';
import { TaskFormModal } from '../../modals/task-form-modal/task-form-modal';
import { RoleEnum } from '../../../../enum/role';

@Component({
  selector: 'app-ticket-detail',
  imports: [CommonModule, ReactiveFormsModule, FontAwesomeModule, FormsModule],
  templateUrl: './ticket-detail.html',
  styleUrl: './ticket-detail.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TicketDetail {
  protected readonly ticketStore = inject(TicketDetailsStore);
  protected readonly authStore = inject(AuthStore);
  private activatedRoute = inject(ActivatedRoute);
  private fb = inject(FormBuilder);
  private toastService = inject(HotToastService);

  private dialogService = inject(DialogService);
  private readonly TASK_FORM_MODAL_ID = 'taskFormModal';

  protected readonly ticketTypeLabel = TicketTypeLabel;
  protected readonly ticketPriorityLabel = TicketPriorityLabel;
  protected readonly ticketStatusLabel = TicketStatusLabel;
  protected readonly ticketPriorities = Object.values(TicketPriorityEnum);
  protected readonly ticketTypes = Object.values(TicketTypeEnum);
  protected readonly ticketStatus = Object.values(TicketStatusEnum);
  protected readonly ticketStatusColor = TicketStatusColor;
  protected readonly Role = RoleEnum;

  ticketPublicIdParam = signal<string | null>(null);
  ticketNumber = computed(() => this.ticketStore.ticket()?.ticket.ticketPublicId.substring(0, 8).toUpperCase());
  issuer = computed(() => this.ticketStore.issuerTicket());
  ticket = computed(() => this.ticketStore.ticketDetail());

  //form
  ticketForm!: FormGroup;
  commentForm!: FormGroup;
  initialTicket!: TicketDetails;

  //comment
  editingCommentId = signal<string | null>(null);
  editingValueComment = '';

  constructor() {
    effect(() => {
      this.initFormAfterTicketLoading();
      this.listenToUpdateTicket();
      this.listenToErrorStore();
      this.listenToUpdateComment();
    });
  }

  ngOnInit(): void {
    this.ticketPublicIdParam.set(this.activatedRoute.snapshot.paramMap.get('id'));
    if (this.ticketPublicIdParam() != null) {
      this.ticketStore.getTicket(this.ticketPublicIdParam()!);
    }
  }

  // MAIN FORM
  onEditTicketFormClick = (event: MouseEvent | TouchEvent, type: 'edit' | 'cancel'): void => {
    event.stopImmediatePropagation();
    if (type === 'edit') this.ticketForm.enable();
    else {
      this.initTicketForm(this?.initialTicket);
    }
  };

  onTicketFormSubmitClick = (event: MouseEvent | TouchEvent): void => {
    event.stopImmediatePropagation();
    if (this.ticketForm.invalid) {
      this.ticketForm.markAllAsDirty();
      return;
    } else {
      this.ticketStore.updateTicket({
        ticketPublicId: this.ticket()!.ticketPublicId,
        issuerPublicId: this.ticket()!.issuerPublicId,
        ...this.ticketForm.value,
      });
    }
  };

  // ASSIGNEE

  onAssigneeSelectClick = (event: string): void => {
    if (event === 'no_selection') return;
    this.ticketStore.updateTicketAssignee({ ticketPublicId: this.ticket()?.ticketPublicId!, assigneePublicId: event });
  };

  // TASK

  onAddNewTaskClick = (event: TouchEvent | MouseEvent): void => {
    event.stopImmediatePropagation();
    this.dialogService.open(TaskFormModal, {
      id: this.TASK_FORM_MODAL_ID,
      data: {
        mode: 'create',
        ticketPublicId: this.ticket()?.ticketPublicId,
      },
    });
  };
  onEditTaskClick = (event: TouchEvent | MouseEvent, task: Task): void => {
    event.stopImmediatePropagation();
    console.log(task);
    this.dialogService.open(TaskFormModal, {
      id: this.TASK_FORM_MODAL_ID,
      data: {
        mode: 'edit',
        task: task,
      },
    });
  };

  onDeleteTaskClick = (event: TouchEvent | MouseEvent, task: Task): void => {
    event.stopImmediatePropagation();
    this.ticketStore.deleteTask(task.taskPublicId);
  };

  // COMMENT

  onSubmitComment(event: MouseEvent | TouchEvent): void {
    event.stopImmediatePropagation();
    console.log('toast');
    if (this.commentForm.invalid) {
      return;
    }

    const comment = this.commentForm.getRawValue().comment as string;

    this.ticketStore.addComment({
      ticketPublicId: this.ticket()!.ticketPublicId,
      comment: comment.trim(),
    });

    this.commentForm.reset();
  }

  onEditCommentClick = (event: TouchEvent | MouseEvent, comment: Comment): void => {
    event.stopImmediatePropagation();
    this.editingCommentId.set(comment.commentPublicId);
    this.editingValueComment = comment.comment;
  };

  onCancelCommentClick = (event: TouchEvent | MouseEvent): void => {
    event.stopImmediatePropagation();
    this.editingCommentId.set(null);
    this.editingValueComment = '';
  };

  onSaveCommentClick = (event: TouchEvent | MouseEvent, comment: Comment): void => {
    event.stopImmediatePropagation();
    this.ticketStore.editComment({
      commentPublicId: comment.commentPublicId,
      ownerCommentPublicId: comment.userPublicId,
      ticketPublicId: this.ticket()!.ticketPublicId,
      comment: this.editingValueComment,
    });
  };

  onDeleteCommentClick = (event: TouchEvent | MouseEvent, commentPublicId: string): void => {
    event.stopImmediatePropagation();
    this.ticketStore.deleteComment(commentPublicId);
  };

  // PRIVATE METHOD

  private initTicketForm(ticket: TicketDetails): void {
    this.initialTicket = ticket;
    this.ticketForm = this.fb.group({
      title: [ticket.title, Validators.required],
      description: [ticket.description, Validators.required],
      priority: [ticket.priority, Validators.required],
      type: [ticket.type, Validators.required],
      status: [ticket.status, Validators.required],
      progress: [ticket.progress, [Validators.required, Validators.min(0), Validators.max(100)]],
      dueDate: [ticket.dueDate ? ticket.dueDate.toString().substring(0, 10) : '', Validators.required],
    });
    this.ticketForm.disable();

    // Keep comment value if user cancel any edit on main form.
    let comment = null;
    if (this.commentForm) {
      comment = this.commentForm.getRawValue().comment as string;
    }

    this.commentForm = this.fb.nonNullable.group({
      comment: [comment == null ? '' : comment, [Validators.required, Validators.maxLength(2000)]],
    });
  }

  private initFormAfterTicketLoading(): void {
    const ticket = this.ticketStore.ticketDetail();
    if (ticket && !this.ticketForm) {
      this.initTicketForm(ticket);
    }
  }

  private listenToUpdateTicket(): void {
    if (this.ticketStore.ticketUpdated()) {
      this.toastService.success('Ticket modifié avec succès !');
      this.ticketStore.resetTicketUpdated();
      this.ticketForm.disable();
    }
  }

  private listenToUpdateComment(): void {
    if (this.ticketStore.commentUpdated()) {
      this.editingCommentId.set(null);
      this.editingValueComment = '';
    }
  }

  private listenToErrorStore(): void {
    const error = this.ticketStore.error();
    if (error) {
      const message = error.error.detail || error.message;
      this.toastService.error(message);
    }
  }

  get f(): { [key: string]: AbstractControl } {
    return this.ticketForm.controls;
  }
}
