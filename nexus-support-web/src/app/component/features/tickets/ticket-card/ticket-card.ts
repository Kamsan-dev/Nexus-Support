import { DatePipe, NgClass } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TicketTypeEnum, TicketTypeColor, TicketTypeLabel } from '../../../../enum/ticket-type';
import {
  TicketStatusEnum,
  TicketStatusColor,
  TicketStatusLabel,
} from '../../../../enum/ticket-status';
import { TicketPriority, TicketPriorityLabel } from '../../../../enum/ticket-priority';

@Component({
  selector: 'app-ticket-card',
  imports: [DatePipe, FontAwesomeModule, NgClass],
  templateUrl: './ticket-card.html',
  styleUrl: './ticket-card.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TicketCard {
  createdAt = input.required<string>();
  ticketPublicId = input.required<string>();
  title = input.required<string>();
  description = input.required<string>();
  progress = input.required<number>();
  status = input.required<TicketStatusEnum>();
  priority = input.required<TicketPriority>();
  type = input.required<TicketTypeEnum>();
  dueDate = input.required<string>();
  fileCount = input.required<number>();
  commentCount = input.required<number>();
  createdAtDate = computed(() => new Date(this.createdAt()));
  ticketNumber = computed(() => this.ticketPublicId().substring(0, 8).toUpperCase());

  protected readonly ticketTypeLabel = TicketTypeLabel;
  protected readonly ticketStatusLabel = TicketStatusLabel;
  protected readonly ticketPriorityLabel = TicketPriorityLabel;

  protected readonly ticketStatusColor = TicketStatusColor;
  protected readonly ticketTypeColor = TicketTypeColor;
}
