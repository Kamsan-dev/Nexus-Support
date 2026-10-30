import { DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, effect, inject, input, output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { UpdateProfileRequest } from '../../../../core/model/request/update-profile.request';
import { User } from '../../../../core/model/user.model';

@Component({
  selector: 'app-profile-form',
  imports: [FontAwesomeModule, ReactiveFormsModule, DatePipe],
  templateUrl: './profile-form.html',
  styleUrl: './profile-form.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProfileForm {
  private fb = inject(FormBuilder);
  user = input.required<User>();
  loading = input.required<boolean>();
  profileUpdated = input.required<boolean>();
  submitEvent = output<UpdateProfileRequest>();
  resetProfileUpdate = output<void>();
  form!: FormGroup;
  initialUserData!: User;

  constructor() {
    effect(() => {
      this.listenToProfileUpdate();
    });
  }

  ngOnInit(): void {
    this.initForm(this.user());
  }

  // MAIN FORM
  onEditFormClick = (event: MouseEvent | TouchEvent, type: 'edit' | 'cancel'): void => {
    event.stopImmediatePropagation();
    if (type === 'edit') this.form.enable();
    else {
      this.initForm(this?.initialUserData);
    }
  };

  onFormSubmit = (event: MouseEvent | TouchEvent): void => {
    event.stopImmediatePropagation();
    console.log('here');
    if (this.form.invalid) {
      this.form.markAllAsDirty;
      return;
    } else {
      this.submitEvent.emit(this.form.value);
    }
  };

  private initForm(user: User): void {
    this.initialUserData = user;
    this.form = this.fb.group({
      firstName: [this.initialUserData.firstName, Validators.required],
      lastName: [this.initialUserData.lastName, Validators.required],
      email: [this.initialUserData.email, Validators.required],
      address: [this.initialUserData.address, Validators.required],
      phone: [this.initialUserData.phone, Validators.required],
      bio: [this.initialUserData.bio],
    });
    this.form.disable();
  }

  private listenToProfileUpdate(): void {
    if (this.profileUpdated()) {
      this.form.disable();
      this.resetProfileUpdate.emit();
    }
  }
}
