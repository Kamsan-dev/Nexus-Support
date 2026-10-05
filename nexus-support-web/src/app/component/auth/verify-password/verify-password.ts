import { HttpErrorResponse } from '@angular/common/http';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  inject,
  signal,
  WritableSignal,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { HotToastService } from '@ngxpert/hot-toast';
import { delay, finalize, forkJoin, timer } from 'rxjs';
import { ApiResponse } from '../../../core/response/api.response';
import { StorageService } from '../../../service/storage.service';
import { UserService } from '../../../service/user.service';
import Validation from '../../../shared/utils/validation';
import { DoResetPasswordRequest } from '../../../core/request/change-password.request';

@Component({
  selector: 'app-verify-password',
  imports: [ReactiveFormsModule, FontAwesomeModule, RouterLink],
  templateUrl: './verify-password.html',
  styleUrl: './verify-password.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class VerifyPassword {
  private storage = inject(StorageService);
  private userService = inject(UserService);
  private destroyRef = inject(DestroyRef);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private toastService = inject(HotToastService);
  private activatedRoute = inject(ActivatedRoute);

  loadingVerifyToken = signal(false);
  errorVerifyToken: WritableSignal<undefined | string> = signal(undefined);
  messageVerifyToken: WritableSignal<undefined | string> = signal(undefined);

  loading = signal(false);
  message: WritableSignal<undefined | string> = signal(undefined);
  error: WritableSignal<undefined | string> = signal(undefined);

  token: WritableSignal<string | null> = signal(null);
  typeForm: 'reset' | 'verify' = 'verify';
  isReseted = false;

  resetPasswordForm!: FormGroup;

  ngOnInit(): void {
    this.initForm();
    this.token.set(this.readTokenFromQueryParams());
    if (this.token()) {
      this.verifyResetPasswordToken(this.token()!);
    } else {
      this.error.set('Invalid link. Please try again.');
    }
  }

  private initForm(): void {
    this.resetPasswordForm = this.fb.nonNullable.group(
      {
        password: ['', [Validators.required]],
        confirmPassword: ['', Validators.required],
      },
      {
        validators: [Validation.match('password', 'confirmPassword')],
      },
    );
  }

  private verifyResetPasswordToken(token: string) {
    this.loadingVerifyToken.set(true);
    this.userService
      .verifyResetPasswordToken(token)
      .pipe(
        delay(3000),
        takeUntilDestroyed(this.destroyRef),
        finalize(() => {
          this.loadingVerifyToken.set(false);
        }),
      )
      .subscribe({
        next: (response: ApiResponse<null>) => {
          this.messageVerifyToken.set(response.message);
          this.toastService.success(response.message);
          this.typeForm = 'reset';
        },
        error: (error: HttpErrorResponse) => {
          this.toastService.error(error.error.detail);
          this.errorVerifyToken.set(error.error.detail);
        },
      });
  }

  public onSubmitForm(event: MouseEvent | TouchEvent): void {
    this.messageVerifyToken.set(undefined);
    this.message.set(undefined);
    this.error.set(undefined);

    const request: DoResetPasswordRequest = {
      token: this.token()!,
      password: this.resetPasswordForm.value['password'],
      confirmPassword: this.resetPasswordForm.value['confirmPassword'],
    };

    this.loading.set(true);
    this.userService
      .resetPasswordConfirm(request)
      .pipe(
        delay(3000),
        takeUntilDestroyed(this.destroyRef),
        finalize(() => {
          this.loading.set(false);
        }),
      )
      .subscribe({
        next: (response: ApiResponse<null>) => {
          this.message.set(response.message);
          this.toastService.success(response.message);
          this.resetPasswordForm.reset();
          this.isReseted = true;
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

  get f(): { [key: string]: AbstractControl } {
    return this.resetPasswordForm.controls;
  }

  public closeMessage(event: TouchEvent | MouseEvent): void {
    event.stopImmediatePropagation();
    this.messageVerifyToken.set(undefined);
  }
}
