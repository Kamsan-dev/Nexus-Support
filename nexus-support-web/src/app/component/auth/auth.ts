import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
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
    event.stopImmediatePropagation();
    this.authService.login();
  }
}
