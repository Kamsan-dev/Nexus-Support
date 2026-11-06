import { ChangeDetectionStrategy, Component, DestroyRef, effect, inject, signal, ViewContainerRef } from '@angular/core';
import { UserStore } from '../../../../store/user.store';
import { ActivatedRoute } from '@angular/router';
import { AuthStore } from '../../../../store/auth.store';
import { ModalService } from '../../../../service/modal.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { FormBuilder, FormsModule } from '@angular/forms';
import { RoleEnum, RoleLabel } from '../../../../enum/role';
import { CommonModule, Location } from '@angular/common';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ProfileForm } from '../../profile/profile-form/profile-form';
import { UpdateProfileRequest } from '../../../../core/model/request/update-profile.request';

@Component({
  selector: 'app-user-detail',
  imports: [CommonModule, FontAwesomeModule, FormsModule, ProfileForm],
  templateUrl: './user-detail.html',
  styleUrl: './user-detail.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserDetail {
  protected readonly store = inject(UserStore);
  protected readonly authStore = inject(AuthStore);
  private activatedRoute = inject(ActivatedRoute);

  private location = inject(Location);
  private modalService = inject(ModalService);
  private readonly toast = inject(HotToastService);
  private viewRef = inject(ViewContainerRef);
  private destroyRef = inject(DestroyRef);
  private fb = inject(FormBuilder);
  readonly roleLabel = RoleLabel;
  readonly RoleEnum = RoleEnum;
  readonly roles = Object.values(RoleEnum);

  userId = signal<string | null>(null);
  ngOnInit(): void {
    this.userId.set(this.activatedRoute.snapshot.paramMap.get('id'));
    if (this.userId() != null) {
      this.store.getUser(this.userId()!);
    }
  }

  constructor() {
    effect(() => {
      const error = this.store.error();
      if (error) {
        const message = error.error.detail || error.message;
        this.toast.error(message);
      }
    });
  }
  goBack = () => {
    this.location.back();
  };

  onUpdateProfile = (event: UpdateProfileRequest) => {
    this.store.updateProfile({
      userPublicId: this.userId()!,
      request: event,
    });
  };

  onResetProfileUpdate = () => {
    this.store.resetCurrentUserUpdate();
  };

  // settings

  onToggleAccountLocked = (event: MouseEvent | TouchEvent): void => {
    event.stopImmediatePropagation();
    const concat = this.store.currentUser()?.isAccountLocked ? 'unlock' : 'lock';
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
        if (this.userId()) this.store.toggleAccountLocked(this.userId()!);
      });
  };

  onToggleAccountExpired = (event: MouseEvent | TouchEvent): void => {
    event.stopImmediatePropagation();
    const concat = this.store.currentUser()?.isAccountExpired ? 'renew' : 'expire';
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
        if (this.userId()) this.store.toggleAccountExpired(this.userId()!);
      });
  };

  onToggleAccountEnabled = (event: MouseEvent | TouchEvent): void => {
    event.stopImmediatePropagation();
    const concat = this.store.currentUser()?.isAccountEnabled ? 'disable' : 'enable';
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
        if (this.userId()) this.store.toggleAccountEnabled(this.userId()!);
      });
  };

  // roles

  onRoleUserChange = (role: RoleEnum): void => {
    if (this.store.currentUser() && this.userId()) {
      this.store.updateUserRole({ userPublicId: this.userId()!, roleName: role });
    }
  };
}
