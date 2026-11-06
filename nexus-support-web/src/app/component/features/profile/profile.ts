import { ChangeDetectionStrategy, Component, DestroyRef, effect, inject, ViewContainerRef } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ProfileForm } from './profile-form/profile-form';
import { AuthStore } from '../../../store/auth.store';
import { RoleEnum, RoleLabel } from '../../../enum/role';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { UpdateProfileRequest } from '../../../core/model/request/update-profile.request';
import { ModalService } from '../../../service/modal.service';
import { AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import Validation from '../../../shared/utils/validation';
import { HotToastService } from '@ngxpert/hot-toast';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
@Component({
  selector: 'app-profile',
  imports: [CommonModule, ProfileForm, FontAwesomeModule, ReactiveFormsModule, FormsModule],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Profile {
  private location = inject(Location);
  private modalService = inject(ModalService);
  private readonly toast = inject(HotToastService);
  private viewRef = inject(ViewContainerRef);
  private destroyRef = inject(DestroyRef);
  private fb = inject(FormBuilder);
  readonly store = inject(AuthStore);
  readonly roleLabel = RoleLabel;
  readonly RoleEnum = RoleEnum;
  readonly roles = Object.values(RoleEnum);

  // form password
  formPassword!: FormGroup;

  constructor() {
    effect(() => {
      const errorAuthStore = this.store.error();
      if (errorAuthStore) {
        const message = errorAuthStore.error.detail || errorAuthStore.message;
        this.toast.error(message);
      }
    });
  }
  ngOnInit(): void {
    if (!this.store.profile()) {
      this.store.loadProfile();
    }

    this.initFormPassword();
  }

  onUpdateProfile = (event: UpdateProfileRequest) => {
    this.store.updateProfile({
      userPublicId: this.store.user()!.userPublicId,
      request: event,
    });
  };

  onResetProfileUpdate = () => {
    this.store.resetProfileUpdated();
  };

  goBack = () => {
    this.location.back();
  };

  onFormPasswordClick = (event: MouseEvent | TouchEvent): void => {
    event.stopImmediatePropagation();
    if (this.formPassword.invalid) {
      this.formPassword.markAllAsDirty();
      return;
    }
    this.store.updatePassword(this.formPassword.value);
    this.formPassword.reset();
  };

  onPasswordFormEditClick = (event: MouseEvent | TouchEvent, type: 'edit' | 'cancel'): void => {
    event.stopImmediatePropagation();
    if (type === 'edit') this.formPassword.enable();
    else {
      this.store.resetProfileError();
      this.formPassword.reset();
      this.formPassword.disable();
    }
  };

  clearError = (event: MouseEvent | TouchEvent): void => {
    event.stopImmediatePropagation();
    this.store.resetProfileError();
  };

  // settings

  onToggleAccountLocked = (event: MouseEvent | TouchEvent): void => {
    event.stopImmediatePropagation();
    const concat = this.store.profile()?.user.isAccountLocked ? 'unlock' : 'lock';
    this.modalService
      .open(this.viewRef, {
        title: 'Verification',
        message: `Are you sure you want to ${concat} this account ?`,
        subtitle: 'Updating account settings may change user access',
        type: 'warning',
      })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        if (!confirmed) {
          return;
        }
        this.store.toggleAccountLocked(this.store.profile()!.user.userPublicId);
      });
  };

  onToggleAccountExpired = (event: MouseEvent | TouchEvent): void => {
    event.stopImmediatePropagation();
    const concat = this.store.profile()?.user.isAccountExpired ? 'renew' : 'expire';
    this.modalService
      .open(this.viewRef, {
        title: 'Verification',
        message: `Are you sure you want to ${concat} this account ?`,
        subtitle: 'Updating account settings may change user access',
        type: 'warning',
      })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        if (!confirmed) {
          return;
        }
        this.store.toggleAccountExpired(this.store.profile()!.user.userPublicId);
      });
  };

  onToggleAccountEnabled = (event: MouseEvent | TouchEvent): void => {
    event.stopImmediatePropagation();
    const concat = this.store.profile()?.user.isAccountEnabled ? 'disable' : 'enable';
    this.modalService
      .open(this.viewRef, {
        title: 'Verification',
        message: `Are you sure you want to ${concat} this account ?`,
        subtitle: 'Updating account settings may change user access',
        type: 'warning',
      })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        if (!confirmed) {
          return;
        }
        this.store.toggleAccountEnabled(this.store.profile()!.user.userPublicId);
      });
  };

  // mfa

  toggleMfa = (action: 'enable' | 'disable', event: MouseEvent | TouchEvent): void => {
    event.stopImmediatePropagation();
    const concat = this.store.profile()?.user.isUsingMfa ? 'disable' : 'enable';
    this.modalService
      .open(this.viewRef, {
        title: 'Verification',
        message: `Are you sure you want to ${concat} MFA for this account ?`,
        subtitle: 'Updating account settings may change user access',
        type: 'warning',
      })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        if (!confirmed) {
          return;
        }

        if (action === 'enable') {
          this.store.enableMfa();
        } else {
          this.store.disableMfa();
        }
      });
  };

  // roles

  onRoleUserChange = (role: RoleEnum): void => {
    if (this.store.profile()) {
      this.store.updateUserRole({ userPublicId: this.store.profile()!.user.userPublicId, roleName: role });
    }
  };

  private initFormPassword(): void {
    this.formPassword = this.fb.nonNullable.group(
      {
        currentPassword: ['', Validators.required],
        newPassword: ['', Validators.required],
        confirmNewPassword: ['', Validators.required],
      },
      {
        validators: [Validation.match('newPassword', 'confirmNewPassword')],
      },
    );
    this.formPassword.disable();
  }

  get f(): { [key: string]: AbstractControl } {
    return this.formPassword.controls;
  }
}
