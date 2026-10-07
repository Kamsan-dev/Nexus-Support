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
import { RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { HotToastService } from '@ngxpert/hot-toast';
import { finalize } from 'rxjs';
import { ApiResponse } from '../../../core/model/response/api.response';
import { UserService } from '../../../service/user.service';
import { getFormData } from '../../../shared/utils/request';

@Component({
  selector: 'app-reset-password',
  imports: [FontAwesomeModule, RouterLink, ReactiveFormsModule],
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ResetPassword {
  private userService = inject(UserService);
  private destroyRef = inject(DestroyRef);
  private fb = inject(FormBuilder);
  private toastService = inject(HotToastService);

  loading = signal(false);
  message: WritableSignal<undefined | string> = signal(undefined);
  error: WritableSignal<undefined | string> = signal(undefined);

  resetForm!: FormGroup;

  ngOnInit(): void {
    this.initRegisterForm();
  }

  private initRegisterForm(): void {
    this.resetForm = this.fb.nonNullable.group({
      email: ['john.doe@gmail.com', [Validators.required, Validators.email]],
    });
  }

  public onSubmitResetForm(event: MouseEvent | TouchEvent): void {
    event.stopImmediatePropagation();
    this.loading.set(true);

    this.userService
      .resetPassword(getFormData(this.resetForm.value, null))
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => {
          this.loading.set(false);
        }),
      )
      .subscribe({
        next: (response: ApiResponse<null>) => {
          this.message.set(response.message);
          this.toastService.success(response.message);
          this.resetForm.reset;
          this.resetForm.markAsPristine();
        },
        error: (error: HttpErrorResponse) => {
          this.toastService.error(error.error.detail);
          this.error.set(error.error.detail);
        },
      });
  }

  public closeMessage(event: MouseEvent | TouchEvent): void {
    event.stopImmediatePropagation();
    this.message.set(undefined);
    this.error.set(undefined);
  }

  get f(): { [key: string]: AbstractControl } {
    return this.resetForm.controls;
  }
}
