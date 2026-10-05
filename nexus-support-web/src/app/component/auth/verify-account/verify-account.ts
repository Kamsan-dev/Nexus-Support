import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  inject,
  signal,
  WritableSignal,
} from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { UserService } from '../../../service/user.service';
import { StorageService } from '../../../service/storage.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { delay, finalize } from 'rxjs';
import { ApiResponse } from '../../../core/response/api.response';
import { HotToastService } from '@ngxpert/hot-toast';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-verify-account',
  imports: [RouterLink, FontAwesomeModule],
  templateUrl: './verify-account.html',
  styleUrl: './verify-account.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class VerifyAccount {
  loading = signal(false);
  message: WritableSignal<undefined | string> = signal(undefined);
  error: WritableSignal<undefined | string> = signal(undefined);
  private userService = inject(UserService);
  private storage = inject(StorageService);
  private router = inject(Router);
  private toastService = inject(HotToastService);
  private activatedRoute = inject(ActivatedRoute);
  private destroyRef = inject(DestroyRef);

  ngOnInit(): void {
    if (this.userService.isAuthenticated()) {
      this.storage.getRedirectUrl()
        ? this.router.navigate([this.storage.getRedirectUrl])
        : this.router.navigate(['/dashboard']);
    }

    const token = this.readTokenFromQueryParams();
    if (token) {
      this.verifyAccount(token);
    } else {
      this.error.set('Invalid link. Please try again.');
    }
  }

  public closeMessage(event: MouseEvent | TouchEvent): void {
    event.stopImmediatePropagation();
    this.message.set(undefined);
    this.error.set(undefined);
  }

  private verifyAccount(token: string): void {
    this.loading.set(true);

    this.userService
      .verifyAccount(token)
      .pipe(
        delay(4000),
        takeUntilDestroyed(this.destroyRef),
        finalize(() => {
          this.loading.set(false);
        }),
      )
      .subscribe({
        next: (response: ApiResponse<null>) => {
          this.message.set(response.message);
          this.toastService.success(response.message);
        },
        error: (error: HttpErrorResponse) => {
          this.toastService.error(error.error.detail);
          this.error.set(error.error.detail);
        },
      });
  }

  private readTokenFromQueryParams(): string | null {
    return this.activatedRoute.snapshot.queryParamMap.get('token') ?? null;
  }
}
