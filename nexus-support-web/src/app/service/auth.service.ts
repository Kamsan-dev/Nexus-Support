import { Injectable, computed, inject, signal } from '@angular/core';
import { AuthSession } from '../core/model/auth-session.model';
import { OAuthService } from './oauth.service';

interface TokenResponse {
  access_token: string;
  refresh_token?: string;
  token_type: string;
  expires_in: number;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private oauth = inject(OAuthService);

  private readonly session = signal<AuthSession | null>(null);

  readonly accessToken = computed(() => this.session()?.accessToken ?? null);

  public isAuthenticated = computed(() => this.session() != null);

  async login(): Promise<void> {
    await this.oauth.login();
  }

  async handleCallback(code: string, state: string): Promise<void> {
    const tokenResponse = await this.oauth.handleCallback(code, state);
    console.log(tokenResponse);
    // this.session.set({
    //   accessToken: tokenResponse.access_token,
    //   refreshToken: tokenResponse.refresh_token,
    //   expiresAt: Date.now() + tokenResponse.expires_in * 1000,
    // });
  }
}
