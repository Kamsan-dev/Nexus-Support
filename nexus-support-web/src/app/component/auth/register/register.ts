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
import { Router, RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { HotToastService } from '@ngxpert/hot-toast';
import { finalize } from 'rxjs';
import { ApiResponse } from '../../../core/response/api.response';
import { StorageService } from '../../../service/storage.service';
import { UserService } from '../../../service/user.service';
import Validation from '../../../shared/utils/validation';
import { AuthService } from '../../../service/auth.service';

@Component({
  selector: 'app-register',
  imports: [FontAwesomeModule, RouterLink, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Register {
  private storage = inject(StorageService);
  private userService = inject(UserService);
  private authService = inject(AuthService);
  private destroyRef = inject(DestroyRef);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private toastService = inject(HotToastService);

  loading = signal(false);
  message: WritableSignal<undefined | string> = signal(undefined);
  error: WritableSignal<undefined | string> = signal(undefined);

  registerForm!: FormGroup;

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.storage.getRedirectUrl()
        ? this.router.navigate([this.storage.getRedirectUrl])
        : this.router.navigate(['dashboard']);
    }

    this.initRegisterForm();
  }

  private initRegisterForm(): void {
    this.registerForm = this.fb.nonNullable.group(
      {
        email: ['john.doe@gmail.com', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', Validators.required],
        firstName: ['john'],
        lastName: ['Doe', Validators.required],
      },
      {
        validators: [Validation.match('password', 'confirmPassword')],
      },
    );
  }

  public onSubmitRegisterForm(event: MouseEvent | TouchEvent): void {
    event.stopImmediatePropagation();
    this.loading.set(true);
    const { email, password, firstName, lastName } = this.registerForm.value;

    this.userService
      .register({ email, password, firstName, lastName })
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
          this.registerForm.reset;
          this.registerForm.markAsPristine();
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
    return this.registerForm.controls;
  }
}
