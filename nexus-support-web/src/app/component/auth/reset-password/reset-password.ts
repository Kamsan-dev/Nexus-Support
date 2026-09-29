import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-reset-password',
  imports: [FontAwesomeModule, RouterLink],
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ResetPassword {}
