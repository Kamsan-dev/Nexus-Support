import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { StorageService } from '../../../service/storage.service';
import { UserService } from '../../../service/user.service';

@Component({
  selector: 'app-register',
  imports: [FontAwesomeModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Register {
  private storage = inject(StorageService);
  private UserService = inject(UserService);
}
