import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, effect, inject } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { HotToastService } from '@ngxpert/hot-toast';
import { StorageService } from '../../service/storage.service';
import { Footer } from './footer/footer';
import { Navbar } from './navbar/navbar';
import { AuthStore } from '../../store/auth.store';
import { AuthService } from '../../auth/auth.service';
import { TicketStore } from '../../store/ticket.store';

@Component({
  selector: 'app-layout',
  imports: [RouterOutlet, Navbar, RouterLink, Footer],
  templateUrl: './layout.html',
  styleUrl: './layout.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Layout {
  private authService = inject(AuthService);
  private storage = inject(StorageService);
  private router = inject(Router);

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/']);
    }
  }
}
