import { ChangeDetectionStrategy, Component, DestroyRef, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { StorageService } from '../../../service/storage.service';
import { UserService } from '../../../service/user.service';
import { State } from '../../../core/model/state.model';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import Validation from '../../../shared/utils/validation';
import { HotToastService } from '@ngxpert/hot-toast';
import { ApiResponse } from '../../../core/response/api.response';
import { finalize } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { HttpErrorResponse } from '@angular/common/http';

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
  private destroyRef = inject(DestroyRef);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private toastService = inject(HotToastService);

  state = signal<State>({
    loading: false,
    message: undefined,
    error: undefined,
  });

  registerForm!: FormGroup;

  ngOnInit(): void {
    if (this.userService.isAuthenticated()) {
      this.storage.getRedirectUrl()
        ? this.router.navigate([this.storage.getRedirectUrl])
        : this.router.navigate(['/dashboard']);
    }

    this.initRegisterForm();
  }

  private initRegisterForm(): void {
    this.registerForm = this.fb.nonNullable.group(
      {
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', Validators.required],
        firstName: [''],
        lastName: ['', Validators.required],
      },
      {
        validators: [Validation.match('password', 'confirmPassword')],
      },
    );
  }

  public onSubmitRegisterForm(event: MouseEvent | TouchEvent): void {
    event.stopImmediatePropagation();
    this.state().loading = true;
    const { email, password, firstName, lastName } = this.registerForm.value;

    this.userService
      .register({ email, password, firstName, lastName })
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => {
          this.registerForm.reset;
          this.registerForm.markAsPristine();
          this.state().loading = false;
        }),
      )
      .subscribe({
        next: (response: ApiResponse<null>) => {
          this.state().message = response.message;
          this.toastService.success('Success !');
        },
        error: (error: HttpErrorResponse) => {
          console.log(error);
        },
      });
  }

  public closeMessage(event: MouseEvent | TouchEvent): void {
    event.stopImmediatePropagation();
    this.state.set({
      loading: false,
      message: undefined,
      error: undefined,
    });
  }

  get f(): { [key: string]: AbstractControl } {
    return this.registerForm.controls;
  }
}
