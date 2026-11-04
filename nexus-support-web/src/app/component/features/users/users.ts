import { ChangeDetectionStrategy, Component, DestroyRef, effect, inject, signal } from '@angular/core';
import { UserStore } from '../../../store/user.store';
import { AuthStore } from '../../../store/auth.store';
import { RoleEnum, RoleLabel } from '../../../enum/role';
import { Pagination } from '../../../core/model/request.model';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-users',
  imports: [CommonModule, RouterLink, FontAwesomeModule],
  templateUrl: './users.html',
  styleUrl: './users.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Users {
  readonly store = inject(UserStore);
  readonly authStore = inject(AuthStore);
  private location = inject(Location);
  private destroyRef = inject(DestroyRef);
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
  readonly Roles = RoleEnum;
  readonly RoleLabel = RoleLabel;
  loading = signal(false);

  defaultFilter: Pagination = { page: 0, size: 8, sort: [] };

  constructor() {
    effect(() => {
      if (this.authStore.profile()) {
        this.loading.set(false);
        if (this.authStore.hasAnyRole([RoleEnum.ADMIN, RoleEnum.SUPER_ADMIN])) {
          this.listenToQueryParams();
        }
      }
    });
  }

  goBack = () => {
    this.location.back();
  };

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

  private listenToQueryParams(): void {
    this.activatedRoute.queryParamMap.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((params) => {
      const pageParam = Number(params.get('page'));
      const size = Number(params.get('size') ?? this.defaultFilter.size);
      const page = isNaN(pageParam) ? this.defaultFilter.page : pageParam - 1;
      this.store.getUsers({
        page: page < 0 ? 0 : page,
        size,
      });
    });
  }
}
