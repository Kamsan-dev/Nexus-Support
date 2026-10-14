import { ChangeDetectionStrategy, Component, effect, inject } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { HotToastService } from '@ngxpert/hot-toast';
import { AuthService } from '../../auth/auth.service';
import { StorageService } from '../../service/storage.service';
import { TicketStore } from '../../store/ticket.store';
import { Footer } from './footer/footer';
import { Navbar } from './navbar/navbar';

@Component({
  selector: 'app-layout',
  imports: [RouterOutlet, Navbar, RouterLink, Footer],
  templateUrl: './layout.html',
  styleUrl: './layout.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Layout {
  private authService = inject(AuthService);
  private ticketStore = inject(TicketStore);
  private readonly toast = inject(HotToastService);
  private storage = inject(StorageService);
  private router = inject(Router);

  constructor() {
    effect(() => {
      const errorTicketStore = this.ticketStore.ticketStoreError();
      if (errorTicketStore) {
        const message = errorTicketStore.error.detail || errorTicketStore.message;
        this.toast.error(message);
      }
    });
  }

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/']);
    }
  }
}
