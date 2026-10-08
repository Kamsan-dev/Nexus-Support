import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, effect, inject } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { HotToastService } from '@ngxpert/hot-toast';
import { StorageService } from '../../service/storage.service';
import { Footer } from './footer/footer';
import { Navbar } from './navbar/navbar';
import { AuthStore } from '../../auth/auth.store';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-layout',
  imports: [RouterOutlet, Navbar, RouterLink, Footer],
  templateUrl: './layout.html',
  styleUrl: './layout.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Layout {
  readonly authStore = inject(AuthStore);
  private readonly toast = inject(HotToastService);
  private authService = inject(AuthService);
  private storage = inject(StorageService);
  private router = inject(Router);

  constructor() {
    effect(() => {
      const error = this;
      this.authStore.profileError();
      if (error && error instanceof HttpErrorResponse) {
        this.toast.error(error.error.detail);
      }
    });
  }

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/']);
    }
  }
}
