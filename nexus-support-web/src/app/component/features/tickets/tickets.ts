import { ChangeDetectionStrategy, Component, DestroyRef, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { debounceTime, distinct, filter, Subject } from 'rxjs';
import { TicketFilters } from '../../../core/model/ticket.model';
import { TicketPriorityLabel } from '../../../enum/ticket-priority';
import { TicketStatusEnum, TicketStatusLabel } from '../../../enum/ticket-status';
import { TicketTypeEnum, TicketTypeLabel } from '../../../enum/ticket-type';
import { parseEnum } from '../../../shared/utils/validation';
import { TicketStore } from '../../../store/ticket.store';
import { TicketCard } from './ticket-card/ticket-card';

@Component({
  selector: 'app-tickets',
  imports: [TicketCard, FaIconComponent],
  templateUrl: './tickets.html',
  styleUrl: './tickets.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Tickets {
  protected readonly ticketStore = inject(TicketStore);
  private destroyRef = inject(DestroyRef);
  private readonly inputSubject: Subject<string> = new Subject();
  private activatedRoute = inject(ActivatedRoute);
  private router = inject(Router);

  // options, value
  protected readonly ticketStatuses = Object.values(TicketStatusEnum);
  protected readonly ticketTypes = Object.values(TicketTypeEnum);
  protected readonly ticketTypeLabel = TicketTypeLabel;
  protected readonly ticketStatusLabel = TicketStatusLabel;
  protected readonly ticketPriorityLabel = TicketPriorityLabel;

  ngOnInit(): void {
    //this.ticketStore.getTickets(this.defaultTicketQueryFilter);

    this.activatedRoute.queryParamMap
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((params) => {
        const page = Number(params.get('page') ?? this.ticketStore.query.pageRequest.page());
        const size = Number(params.get('size') ?? this.ticketStore.query.pageRequest.size());
        const statusParam = params.get('status') ?? this.ticketStore.statusQuery();
        const typeParam = params.get('type') ?? this.ticketStore.typeQuery();
        const filter = params.get('filter') ?? undefined;

        const status =
          statusParam && statusParam !== 'all'
            ? parseEnum(TicketStatusEnum, statusParam)
            : undefined;
        const type =
          typeParam && typeParam !== 'all' ? parseEnum(TicketTypeEnum, typeParam) : undefined;
        this.ticketStore.getTickets({
          pageRequest: {
            page,
            size,
          },
          filters: {
            status: status,
            type: type,
            filter: filter ? filter : undefined,
          },
        });
      });

    this.inputSubject
      .pipe(debounceTime(500), distinct(), takeUntilDestroyed(this.destroyRef))
      .subscribe((filter) => {
        //this.ticketStore.setCurrentPage(query.pageRequest.page);
        this.router.navigate([], {
          relativeTo: this.activatedRoute,
          queryParams: { filter },
          queryParamsHandling: 'merge',
        });
      });
  }

  searchTickets = (filter: string) => {
    const currentQuery = this.ticketStore.currentQuery();
    this.inputSubject.next(filter);
  };

  filterByStatus = (filter: TicketStatusEnum | '') => {
    //this.ticketStore.setCurrentPage(query.pageRequest.page);
    this.updateFilter({ status: filter || undefined }, 'status');
  };
  filterByType = (filter: TicketTypeEnum | '') => {
    //this.ticketStore.setCurrentPage(query.pageRequest.page);
    this.updateFilter({ type: filter || undefined }, 'type');
  };

  onResetFilters = () => {
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams: { status: 'all', type: 'all', filter: undefined },
      queryParamsHandling: 'merge',
    });
  };

  private updateFilter(filter: Partial<TicketFilters>, field: 'type' | 'status'): void {
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams: { [field]: filter[field] },
      queryParamsHandling: 'merge',
    });
  }
}
