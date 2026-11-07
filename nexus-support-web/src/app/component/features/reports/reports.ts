import { CommonModule, Location } from '@angular/common';
import { ChangeDetectionStrategy, Component, DestroyRef, ElementRef, HostListener, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Pagination } from '../../../core/model/request.model';
import { TicketReportRequest } from '../../../core/model/request/report-request';
import { RoleEnum } from '../../../enum/role';
import { TicketPriorityColors, TicketPriorityEnum, TicketPriorityLabel } from '../../../enum/ticket-priority';
import { TicketStatusColor, TicketStatusEnum, TicketStatusLabel } from '../../../enum/ticket-status';
import { TicketTypeColor, TicketTypeEnum, TicketTypeLabel } from '../../../enum/ticket-type';
import { AuthStore } from '../../../store/auth.store';
import { ReportStore } from '../../../store/report.store';

enum Menu {
  STATUS = 'STATUS',
  TYPE = 'TYPE',
  PRIORIY = 'PRIORITY',
}

@Component({
  selector: 'app-reports',
  imports: [CommonModule, FormsModule, FontAwesomeModule, ReactiveFormsModule, RouterLink],
  templateUrl: './reports.html',
  styleUrl: './reports.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Reports {
  readonly store = inject(ReportStore);
  readonly authStore = inject(AuthStore);
  private router = inject(Router);
  private location = inject(Location);
  private activatedRoute = inject(ActivatedRoute);
  private element = inject(ElementRef);
  private fb = inject(FormBuilder);
  private destroyRef = inject(DestroyRef);

  readonly roles = RoleEnum;

  reportForm!: FormGroup;
  formInit = false;

  pageRequest: Pagination = { page: 0, size: 8, sort: [] };
  reportRequest: TicketReportRequest = {
    statuses: [],
    priorities: [],
    types: [],
  };

  constructor() {
    this.initForm();
  }

  ngOnInit(): void {
    this.listenToQueryParams();
  }

  // menu
  statusMenuOpen = signal(false);
  typeMenuOpen = signal(false);
  priorityMenuOpen = signal(false);

  protected readonly ticketStatuses = Object.values(TicketStatusEnum);
  protected readonly ticketTypes = Object.values(TicketTypeEnum);
  protected readonly ticketPriorities = Object.values(TicketPriorityEnum);

  protected readonly ticketTypeLabel = TicketTypeLabel;
  protected readonly ticketStatusLabel = TicketStatusLabel;
  protected readonly ticketPriorityLabel = TicketPriorityLabel;

  protected readonly ticketStatusColor = TicketStatusColor;
  protected readonly ticketTypeColor = TicketTypeColor;
  protected readonly ticketPriorityColor = TicketPriorityColors;

  menu = Menu;
  request = {};

  goBack = () => this.location.back();

  toggleMenu = (menu: Menu) => {
    switch (menu) {
      case Menu.STATUS:
        this.statusMenuOpen.update((open) => !open);
        this.priorityMenuOpen.set(false);
        this.typeMenuOpen.set(false);
        break;
      case Menu.TYPE:
        this.typeMenuOpen.update((open) => !open);
        this.statusMenuOpen.set(false);
        this.priorityMenuOpen.set(false);
        break;
      case Menu.PRIORIY:
        this.priorityMenuOpen.update((open) => !open);
        this.statusMenuOpen.set(false);
        this.typeMenuOpen.set(false);
        break;
      default:
        this.statusMenuOpen.set(false);
        this.priorityMenuOpen.set(false);
        this.typeMenuOpen.set(false);
        break;
    }
  };

  closeMenu = () => {
    this.statusMenuOpen.set(false);
    this.priorityMenuOpen.set(false);
    this.typeMenuOpen.set(false);
  };

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    const status = this.element.nativeElement.querySelector('.status');
    const type = this.element.nativeElement.querySelector('.type');
    const priority = this.element.nativeElement.querySelector('.priority');

    if (!status.contains(target) && this.statusMenuOpen()) {
      this.statusMenuOpen.update(() => false);
    }
    if (!type.contains(target) && this.typeMenuOpen()) {
      this.typeMenuOpen.update(() => false);
    }
    if (!priority.contains(target) && this.priorityMenuOpen()) {
      this.priorityMenuOpen.update(() => false);
    }
  }

  // report
  report = () => {
    this.closeMenu();
  };

  downloadPDF = () => {
    this.closeMenu();
    const request = this.store.reportRequest();
    this.store.downloadReport(request);
  };

  //form

  onSubmitForm = () => {
    const { statuses, priorities, types, filter, fromDate, toDate } = this.reportForm.getRawValue();

    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams: {
        page: 1,
        size: this.store.pageRequest().size,
        statuses: statuses.length ? statuses : null,
        priorities: priorities.length ? priorities : null,
        types: types.length ? types : null,
        filter: filter || null,
        fromDate: fromDate || null,
        toDate: toDate || null,
      },
      queryParamsHandling: 'merge',
    });
  };
  toggleSelection<T extends string>(control: T[], value: T, checked: boolean, menu: Menu): void {
    const updatedValues = checked ? [...control, value] : control.filter((item) => item !== value);
    switch (menu) {
      case Menu.STATUS:
        this.reportForm.controls['statuses'].setValue(updatedValues);
        break;
      case Menu.TYPE:
        this.reportForm.controls['types'].setValue(updatedValues);
        break;
      case Menu.PRIORIY:
        this.reportForm.controls['priorities'].setValue(updatedValues);
        break;
      default:
        break;
    }
  }

  // pagination
  onPaginationClick = (event: MouseEvent | TouchEvent, index: number, buttonType: 'previous' | 'next' | undefined = undefined): void => {
    event.stopImmediatePropagation();
    let page = this.store.currentPage();
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

  private initForm(): void {
    this.reportForm = this.fb.group({
      statuses: this.fb.control<TicketStatusEnum[]>([], { nonNullable: true }),
      priorities: this.fb.control<TicketPriorityEnum[]>([], { nonNullable: true }),
      types: this.fb.control<TicketTypeEnum[]>([], { nonNullable: true }),
      fromDate: this.fb.control<string>(''),
      toDate: this.fb.control<string>(''),
      filter: this.fb.control<string>(''),
    });
  }

  get f(): { [key: string]: AbstractControl } {
    return this.reportForm.controls;
  }

  private listenToQueryParams(): void {
    this.activatedRoute.queryParamMap.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((params) => {
      const pageParam = Number(params.get('page'));

      const size = Number(params.get('size') ?? this.store.pageRequest().size);

      const statuses = params.getAll('statuses') as TicketStatusEnum[];
      const types = params.getAll('types') as TicketTypeEnum[];
      const priorities = params.getAll('priorities') as TicketPriorityEnum[];
      const page = isNaN(pageParam) ? this.store.pageRequest().page : pageParam - 1;

      const filter = params.get('filter') ?? undefined;
      const fromDate = params.get('fromDate') ?? undefined;
      const toDate = params.get('toDate') ?? undefined;

      this.store.getReport({
        pageRequest: {
          page: page < 0 ? 0 : page,
          size: size,
        },
        filters: {
          statuses: statuses,
          priorities: priorities,
          types: types,
          filter: filter,
          fromDate: fromDate,
          toDate: toDate,
        },
      });

      // populate form when component init

      if (!this.formInit) {
        this.reportForm.controls['statuses'].setValue(statuses);
        this.reportForm.controls['priorities'].setValue(priorities);
        this.reportForm.controls['types'].setValue(types);
        this.reportForm.controls['filter'].setValue(filter);
        this.reportForm.controls['fromDate'].setValue(fromDate);
        this.reportForm.controls['toDate'].setValue(toDate);
        this.formInit = true;
      }
    });
  }
}
