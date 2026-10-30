import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ProfileForm } from './profile-form/profile-form';
import { AuthStore } from '../../../store/auth.store';
import { RoleLabel } from '../../../enum/role';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { UpdateProfileRequest } from '../../../core/model/request/update-profile.request';
@Component({
  selector: 'app-profile',
  imports: [CommonModule, ProfileForm, FontAwesomeModule],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Profile {
  private location = inject(Location);
  readonly store = inject(AuthStore);

  readonly roleLabel = RoleLabel;

  ngOnInit(): void {
    if (!this.store.profile()) {
      this.store.loadProfile();
    }
  }

  onUpdateProfile = (event: UpdateProfileRequest) => {
    this.store.updateProfile(event);
  };

  onResetProfileUpdate = () => {
    this.store.resetProfileUpdated();
  };

  goBack = () => {
    this.location.back();
  };
}
