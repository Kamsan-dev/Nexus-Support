import { ChangeDetectionStrategy, Component, computed, effect, Inject, inject, signal } from '@angular/core';
import { TicketStore } from '../../../../store/ticket.store';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TicketStatusColor, TicketStatusEnum, TicketStatusLabel } from '../../../../enum/ticket-status';
import { AuthStore } from '../../../../store/auth.store';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { TicketDetails } from '../../../../core/model/ticket.model';
import { TicketPriorityEnum, TicketPriorityLabel } from '../../../../enum/ticket-priority';
import { TicketTypeEnum, TicketTypeLabel } from '../../../../enum/ticket-type';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { HotToastService } from '@ngxpert/hot-toast';
import { TicketDetailsStore } from '../../../../store/ticket-details.store';

@Component({
  selector: 'app-ticket-detail',
  imports: [CommonModule, ReactiveFormsModule, FontAwesomeModule],
  templateUrl: './ticket-detail.html',
  styleUrl: './ticket-detail.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [TicketDetailsStore],
})
export class TicketDetail {
  protected readonly ticketStore = inject(TicketDetailsStore);
  protected readonly authStore = inject(AuthStore);
  private activatedRoute = inject(ActivatedRoute);
  private fb = inject(FormBuilder);
  private toastService = inject(HotToastService);

  protected readonly ticketTypeLabel = TicketTypeLabel;
  protected readonly ticketPriorityLabel = TicketPriorityLabel;
  protected readonly ticketStatusLabel = TicketStatusLabel;
  protected readonly ticketPriorities = Object.values(TicketPriorityEnum);
  protected readonly ticketTypes = Object.values(TicketTypeEnum);
  protected readonly ticketStatus = Object.values(TicketStatusEnum);
  protected readonly ticketStatusColor = TicketStatusColor;

  ticketPublicIdParam = signal<string | null>(null);
  ticketNumber = computed(() => this.ticketStore.ticket()?.ticket.ticketPublicId.substring(0, 8).toUpperCase());
  issuer = computed(() => this.ticketStore.issuerTicket());
  ticket = computed(() => this.ticketStore.ticketDetail());

  //form
  ticketForm!: FormGroup;
  initialTicket!: TicketDetails;

  constructor() {
    effect(() => {
      this.initFormAfterTicketLoading();
      this.listenToUpdateTicket();
      this.listenToErrorStore();
    });
  }

  ngOnInit(): void {
    this.ticketPublicIdParam.set(this.activatedRoute.snapshot.paramMap.get('id'));
    if (this.ticketPublicIdParam() != null) {
      this.ticketStore.getTicket(this.ticketPublicIdParam()!);
    }
  }

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
