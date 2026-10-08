import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { StorageService } from '../../service/storage.service';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-guest',
  imports: [RouterOutlet, FontAwesomeModule, RouterLinkActive, RouterLink],
  templateUrl: './guest.html',
  styleUrl: './guest.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Guest {
  private authService = inject(AuthService);
  private storage = inject(StorageService);
  private router = inject(Router);

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.storage.getRedirectUrl()
        ? this.router.navigate([this.storage.getRedirectUrl])
        : this.router.navigate(['/app/dashboard']);
    }
  }
  public onLoginButtonClick(event: TouchEvent | MouseEvent): void {
    event.stopImmediatePropagation();
    this.authService.login();
  }
}
