import { ChangeDetectionStrategy, Component, inject, OnInit } from '@angular/core';
import { RouterOutlet, RouterLinkActive, RouterLink } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fontAwesomeIcons } from '../../shared/icons/font-awesome-icon';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-auth',
  imports: [RouterOutlet, FontAwesomeModule, RouterLinkActive, RouterLink],
  templateUrl: './auth.html',
  styleUrl: './auth.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Auth {
  private authService = inject(AuthService);
  public onLoginButtonClick(event: TouchEvent | MouseEvent): void {
    this.authService.login();
  }
}
