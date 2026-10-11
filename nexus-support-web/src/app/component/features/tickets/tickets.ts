import { ChangeDetectionStrategy, Component, DestroyRef, effect, inject } from '@angular/core';
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
import { HotToastService } from '@ngxpert/hot-toast';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-tickets',
  imports: [TicketCard, FaIconComponent, CommonModule],
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
  private readonly toast = inject(HotToastService);

  // options, value
  protected readonly ticketStatuses = Object.values(TicketStatusEnum);
  protected readonly ticketTypes = Object.values(TicketTypeEnum);
  protected readonly ticketTypeLabel = TicketTypeLabel;
  protected readonly ticketStatusLabel = TicketStatusLabel;
  protected readonly ticketPriorityLabel = TicketPriorityLabel;

  constructor() {
    effect(() => {
      const errorPageTickets = this.ticketStore.ticketPageError();
      if (errorPageTickets) {
        const message = errorPageTickets.message ?? errorPageTickets.error.detail;
        this.toast.error(message);
      }
    });
  }

  ngOnInit(): void {
    this.listenToQueryParams();
    this.listenToFilterChanges();
  }

  searchTickets = (filter: string) => {
    this.inputSubject.next(filter ?? undefined);
  };

  filterByStatus = (filter: TicketStatusEnum) => {
    //this.ticketStore.setCurrentPage(query.pageRequest.page);
    this.updateFilter({ status: filter }, 'status');
  };
  filterByType = (filter: TicketTypeEnum) => {
    //this.ticketStore.setCurrentPage(query.pageRequest.page);
    this.updateFilter({ type: filter }, 'type');
  };

  onResetFilters = () => {
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams: { page: 1, size: 10, status: 'all', type: 'all', filter: undefined },
      queryParamsHandling: 'merge',
    });
  };

  onPaginationClick = (
    event: MouseEvent | TouchEvent,
    index: number,
    buttonType: 'previous' | 'next' | undefined = undefined,
  ): void => {
    event.stopImmediatePropagation();
    let page = this.ticketStore.currentPage();
    if (buttonType == 'previous') page--;
    else if (buttonType == 'next') page++;
    else {
      page = index;
    }
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams: { page: page + 1 },
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

  private listenToQueryParams(): void {
    this.activatedRoute.queryParamMap
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((params) => {
        const pageParam = params.get('page');
        const size = Number(params.get('size') ?? this.ticketStore.query.pageRequest.size());
        const statusParam = params.get('status') ?? this.ticketStore.statusQuery();
        const typeParam = params.get('type') ?? this.ticketStore.typeQuery();
        const filter = params.get('filter') ?? undefined;

        const page =
          pageParam !== null ? Number(pageParam) - 1 : this.ticketStore.query.pageRequest.page();

        const status =
          statusParam && statusParam !== 'all'
            ? parseEnum(TicketStatusEnum, statusParam)
            : undefined;

        const type =
          typeParam && typeParam !== 'all' ? parseEnum(TicketTypeEnum, typeParam) : undefined;
        this.ticketStore.getTickets({
          pageRequest: {
            page: page < 0 ? 0 : page,
            size,
          },
          filters: {
            status: status,
            type: type,
            filter: filter ? filter : undefined,
          },
        });
      });
  }

  private listenToFilterChanges(): void {
    this.inputSubject
      .pipe(debounceTime(500), distinct(), takeUntilDestroyed(this.destroyRef))
      .subscribe((filter) => {
        //this.ticketStore.setCurrentPage(query.pageRequest.page);
        this.router.navigate([], {
          relativeTo: this.activatedRoute,
          queryParams: { filter: filter.trim() || null },
          queryParamsHandling: 'merge',
        });
      });
  }
}
