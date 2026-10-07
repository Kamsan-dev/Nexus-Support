import { ChangeDetectionStrategy, Component, inject, signal, WritableSignal } from '@angular/core';
import { AuthService } from '../../../service/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HotToastService } from '@ngxpert/hot-toast';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-callback',
  imports: [FontAwesomeModule],
  templateUrl: './callback.html',
  styleUrl: './callback.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Callback {
  private authService = inject(AuthService);
  private activatedRoute = inject(ActivatedRoute);
  private toastService = inject(HotToastService);
  private router = inject(Router);

  error: WritableSignal<undefined | string> = signal(undefined);
  loading = signal(false);

  ngOnInit(): void {
    this.authenticate();
  }

  private async authenticate(): Promise<void> {
    const state = this.activatedRoute.snapshot.queryParamMap.get('state') ?? null;
    const code = this.activatedRoute.snapshot.queryParamMap.get('code') ?? null;
    console.log(state);
    console.log(code);
    this.loading.set(true);
    if (!state || !code) {
      this.toastService.error('Unable to authenticate');
    } else {
      try {
        await this.authService.handleCallback(code, state);
        console.log('waiting to be redirecting');
        this.router.navigate(['dashboard']);
      } catch (error) {
        this.toastService.error('Unable to authenticate');
        this.router.navigate(['']);
        throw error;
      } finally {
        this.loading.set(false);
      }
    }
  }
}
